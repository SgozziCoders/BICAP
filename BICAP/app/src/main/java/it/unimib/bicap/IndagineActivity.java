package it.unimib.bicap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import it.unimib.bicap.adapter.InformazioneAdapter;
import it.unimib.bicap.adapter.QuestionarioAdapter;
import it.unimib.bicap.databinding.ActivityIndagineBinding;
import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.Informazione;
import it.unimib.bicap.utils.Asyn_OpenFile;
import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.utils.FileManager;
import it.unimib.bicap.utils.ParcelableBoolean;

public class IndagineActivity extends AppCompatActivity implements InformazioneAdapter.OnInfoCardListener,
        QuestionarioAdapter.OnSubmitClickListener, QuestionarioAdapter.InformazioneRowReciver {
    private IndagineBody mIndagineBody;

    //Array di booleani Parcelable per salvare lo stato delle card espanse
    private ArrayList<ParcelableBoolean> mQuestionariVisibilityList;
    private Bundle global_stat;
    private ActivityIndagineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndagineBinding.inflate(getLayoutInflater());
        if(savedInstanceState != null){
            global_stat = savedInstanceState;
        }else{
            mQuestionariVisibilityList = new ArrayList<ParcelableBoolean>();
        }
        /*
        * Permette di eseguire il downlaod del file su un thread separato, a fine esecuzione
        * avvengono tutte le operazioni per il caricamento del layout
        * */
        new Asyn_LoadAll().execute(null, null, null);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(global_stat != null){
            mQuestionariVisibilityList = global_stat.getParcelableArrayList(Constants.VISIBILITY_CARDS__STATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INDAGINE_BODY_STATE, mIndagineBody);
        mQuestionariVisibilityList.clear();
        int mCount = binding.questionariRecycleView.getAdapter().getItemCount();
        View mView;
        ConstraintLayout mConstraintLayout;
        /*
        * Vengono salvati gli stati delle card espanse
        * */
        for(int i = 0; i < mCount; i++){
            mView = binding.questionariRecycleView.findViewHolderForAdapterPosition(i).itemView;
            mConstraintLayout = (ConstraintLayout) mView.findViewById(R.id.expandableView);
            if(mConstraintLayout.getVisibility() == View.GONE){
                mQuestionariVisibilityList.add(new ParcelableBoolean(false));
            }else{
                mQuestionariVisibilityList.add(new ParcelableBoolean(true));
            }
        }
        outState.putParcelableArrayList(Constants.VISIBILITY_CARDS__STATE, mQuestionariVisibilityList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            /*
            * Riceve il risultato della WebViewActivity, ovvero della compilazione del questionario:
            * si occupa di aggiornare lo stato del modello (questionario corrente) e del layout
            * */
            case(Constants.WEB_ACTIVITY_REQUEST_CODE) :
                if(resultCode == Activity.RESULT_OK){
                    //Modifiche al bottone del questionario corrente
                    int mQuestionarioPosition = data.getExtras().getInt(Constants.QUESTIONARIO_POSITION);
                    mIndagineBody.getQuestionari().get(mQuestionarioPosition).setCompilato(true);
                    View mViewCurrent = binding.questionariRecycleView.findViewHolderForAdapterPosition(mQuestionarioPosition).itemView;
                    TextView mCompilatoTextView = (TextView) mViewCurrent.findViewById(R.id.compilatoTextView);
                    Button mSubmitButton = (Button) mViewCurrent.findViewById(R.id.submitButton);
                    ImageView mCompilatoImageView = (ImageView) mViewCurrent.findViewById(R.id.compilatoImageView);
                    mCompilatoTextView.setVisibility(View.VISIBLE);
                    mCompilatoImageView.setVisibility(View.VISIBLE);
                    mSubmitButton.setVisibility(View.GONE);
                    //Modifiche del layout del questionario successivo
                    //Controllo che son sia l'uiltimo
                    if(binding.questionariRecycleView.getAdapter().getItemCount() != mQuestionarioPosition + 1){
                        View mViewNext = binding.questionariRecycleView.findViewHolderForAdapterPosition(mQuestionarioPosition + 1).itemView;
                        Button mSubmitButtonNext = (Button) mViewNext.findViewById(R.id.submitButton);
                        mSubmitButtonNext.setEnabled(true);
                        mSubmitButtonNext.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmit);
                    }else{
                        //Attiviamo il bottone termina indagine se è stato compilato l'ultimo questionario
                        setEneableSubmitAll();
                    }

                    //Viene salvato su disco lo stato dell'indagine Body
                    mIndagineBody.getHead().setIndagineInCorso(true);
                    String mJsonIndagineBody = new  Gson().toJson(mIndagineBody);
                    String path = this.getApplicationInfo().dataDir + "/indagini/in_corso/" + mIndagineBody.getHead().getId() + ".json";
                    FileManager.writeToFile(mJsonIndagineBody, path);
                }
        }
    }

    private class Asyn_LoadAll extends AsyncTask<Void, Void, Void> {

        /*
        * Scarica dal web server l'indagine body se questa non è in corso
        * altrimenti la legge da file locale
        * */
        @Override
        protected Void doInBackground(Void... voids) {
            IndagineHead mIndagineHead = getIntent().getParcelableExtra("Indagine");
            if(global_stat != null){
                mIndagineBody = global_stat.getParcelable(Constants.INDAGINE_BODY_STATE);
            }else{
                if(mIndagineHead.isIndagineInCorso()){
                    mIndagineBody = loadLocalIndagineBody(mIndagineHead);
                }else{
                    mIndagineBody = loadRemoteIndagineBody(mIndagineHead);
                    mIndagineBody.setHead(mIndagineHead);
                }
            }
            return null;
        }

        //Caricamento di tutti i layout una volta che è stato eseguito il download
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            View v = binding.getRoot();
            setContentView(v);
            loadInformazioniScroll(mIndagineBody);
            loadQuestionari(mIndagineBody);
            setEneableSubmitAll();
        }
    }

    private void setEneableSubmitAll(){
        int mCount = mIndagineBody.getQuestionari().size() - 1;
        if(mIndagineBody.getQuestionari().get(mCount).isCompilato()){
            binding.submitAllButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmitIndagine);
            binding.submitAllButton.setBackgroundResource(R.color.colorPrimary);
            binding.submitAllButton.setEnabled(true);
            binding.submitAllButton.setClickable(true);
            binding.submitAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitAll();
                }
            });
        }
    }

    private void submitAll(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_submit_question)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        new AlertDialog.Builder(IndagineActivity.this)
                                .setMessage(R.string.dialog_submit_thank_you)
                                .setCancelable(false)
                                .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent resultIntent = new Intent();
                                        /*
                                        * Futuro update:
                                        * Si invia al server l'id dell'indagine e l'email dell'utente
                                        * così che non venga più proposta all'utente nel momento
                                        * della richiesta del file Json della lista indagini
                                        * disponibili
                                        * */
                                        FileManager.deleteFile(getApplicationInfo().dataDir + "/indagini/in_corso/" + mIndagineBody.getHead().getId() + ".json");
                                        finish();
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }

    private IndagineBody loadLocalIndagineBody(IndagineHead indagineHead) {
        try{
            File mIndagineBodyFile = new File(getApplicationInfo().dataDir + "/indagini/in_corso/" + indagineHead.getId() + ".json");
            IndagineBody mIndagineBodyLocal = new Gson().fromJson(new BufferedReader(new FileReader(mIndagineBodyFile.getAbsolutePath())), IndagineBody.class);
            return mIndagineBodyLocal;
        }catch(Exception ex){
            //EH BHO .... come cazzo fa a non esistere ???
            return null;
        }
    }

    private void loadInformazioniScroll(IndagineBody indagineBody){
        this.setTitle(indagineBody.getHead().getTitoloIndagine());
        binding.descrizioneTextView.setText(indagineBody.getTematica());

        if(indagineBody.getInformazioni() != null) {
            binding.infoScrollRecycleView.setHasFixedSize(true);

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
            mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.infoScrollRecycleView.setLayoutManager(mLinearLayoutManager);

            InformazioneAdapter mAdapter = new InformazioneAdapter(indagineBody.getInformazioni(), this);
            binding.infoScrollRecycleView.setAdapter(mAdapter);
        }else{
            binding.informazioniTextView.setVisibility(View.GONE);
        }
    }

    private  void loadQuestionari(IndagineBody indagineBody){
        binding.questionariRecycleView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManagerQuestionari = new LinearLayoutManager(this);
        mLinearLayoutManagerQuestionari.setOrientation(LinearLayoutManager.VERTICAL);
        binding.questionariRecycleView.setLayoutManager(mLinearLayoutManagerQuestionari);

        QuestionarioAdapter mQuestionarioAdapter = new QuestionarioAdapter(indagineBody.getQuestionari(),
                this, this, this, mQuestionariVisibilityList);
        binding.questionariRecycleView.setAdapter(mQuestionarioAdapter);
    }

    private IndagineBody loadRemoteIndagineBody(IndagineHead indagineHead) {
        int mId = indagineHead.getId();
        String mFileName = "Indagine" + mId + ".json";
        String mUrl = "https://files.bicap.quarzo.stream/"+ indagineHead.getId() + "/indagine.json";
        String mPath = getApplicationInfo().dataDir + "/" +mFileName;

        FileManager.downloadFile(mUrl, mPath);

        try {
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(mPath));
            IndagineBody mIndagineBody = new Gson().fromJson(mBufferedReader, IndagineBody.class);
            return mIndagineBody;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void onInfoCardClick(final int position) {
        String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mIndagineBody.getInformazioni().get(position).getNomeFile();
        String mUrl = mIndagineBody.getInformazioni().get(position).getFileUrl();
        String mMime = mIndagineBody.getInformazioni().get(position).getTipoFile();
        new Asyn_OpenFile(mUrl, mPath, mMime, this).execute();
    }

    //Click ricevuto da un informazione all'interno di un questionario
    @Override
    public void OnReciveClick(final int questionarioPosition, final int infoPosition) {
        Informazione mInfo = mIndagineBody.getQuestionari().get(questionarioPosition).getInformazioni().get(infoPosition);
        String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mInfo.getNomeFile();
        String mUrl = mInfo.getFileUrl();
        String mMime = mInfo.getTipoFile();
        new Asyn_OpenFile(mUrl, mPath, mMime, this).execute();
    }

    @Override
    public void OnSubmitClick(int position) {
        Intent mWebViewIntent = new Intent(IndagineActivity.this, WebViewActivity.class);
        mWebViewIntent.putExtra(Constants.URL, mIndagineBody.getQuestionari().get(position).getQualtricsUrl());
        mWebViewIntent.putExtra(Constants.TITOLO_QUESTIONARIO, mIndagineBody.getQuestionari().get(position).getTitolo());
        mWebViewIntent.putExtra(Constants.QUESTIONARIO_POSITION, position);
        startActivityForResult(mWebViewIntent, Constants.WEB_ACTIVITY_REQUEST_CODE);
    }
}
