package it.unimib.bicap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import it.unimib.bicap.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import it.unimib.bicap.adapter.InformazioneAdapter;
import it.unimib.bicap.adapter.QuestionarioAdapter;
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
    private ArrayList<ParcelableBoolean> mQuestionariVisibilityList;
    private RecyclerView mQuestionariRecyclerView;
    private Bundle global_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestionariRecyclerView = (RecyclerView) findViewById(R.id.questionariRecycleView);
        if(savedInstanceState != null){
            global_stat = savedInstanceState;
        }else{
            mQuestionariVisibilityList = new ArrayList<ParcelableBoolean>();
        }
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
        int mCount = mQuestionariRecyclerView.getAdapter().getItemCount();
        View mView;
        ConstraintLayout mConstraintLayout;
        for(int i = 0; i < mCount; i++){
            mView = mQuestionariRecyclerView.findViewHolderForAdapterPosition(i).itemView;
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
            case(Constants.WEB_ACTIVITY_REQUEST_CODE) :
                if(resultCode == Activity.RESULT_OK){
                    //Aggiornamento dello stato del questionario x
                    //Toast.makeText(this, "indagineBody.getInformazioni().get(position).getNomeFile()", Toast.LENGTH_LONG).show();
                    int mQuestionarioPosition = data.getExtras().getInt(Constants.QUESTIONARIO_POSITION);
                    mIndagineBody.getQuestionari().get(mQuestionarioPosition).setCompilato(true);
                    View mViewCurrent =mQuestionariRecyclerView.findViewHolderForAdapterPosition(mQuestionarioPosition).itemView;
                    TextView mCompilatoTextView = (TextView) mViewCurrent.findViewById(R.id.compilatoTextView);
                    Button mSubmitButton = (Button) mViewCurrent.findViewById(R.id.submitButton);
                    ImageView mCompilatoImageView = (ImageView) mViewCurrent.findViewById(R.id.compilatoImageView);
                    mCompilatoTextView.setVisibility(View.VISIBLE);
                    mCompilatoImageView.setVisibility(View.VISIBLE);
                    mSubmitButton.setVisibility(View.GONE);
                    if(mQuestionariRecyclerView.getAdapter().getItemCount() != mQuestionarioPosition + 1){
                        View mViewNext = mQuestionariRecyclerView.findViewHolderForAdapterPosition(mQuestionarioPosition + 1).itemView;
                        Button mSubmitButtonNext = (Button) mViewNext.findViewById(R.id.submitButton);
                        mSubmitButtonNext.setEnabled(true);
                        mSubmitButtonNext.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmit);
                    }else{
                        //Attiviamo termina indagine se è stato compilato l'ultimo questionario
                        Button mSubmitAllButton = (Button) findViewById(R.id.submitAllButton);
                        mSubmitAllButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmitIndagine);
                        mSubmitAllButton.setBackgroundResource(R.color.colorPrimary);
                        mSubmitAllButton.setEnabled(true);
                        mSubmitAllButton.setClickable(true);
                    }

                    mIndagineBody.getHead().setIndagineInCorso(true);
                    String mJsonIndagineBody = new  Gson().toJson(mIndagineBody);
                    String path = this.getApplicationInfo().dataDir + "/indagini/in_corso/" + mIndagineBody.getHead().getId() + ".json";
                    FileManager.writeToFile(mJsonIndagineBody, path);
                }
        }
    }

    private class Asyn_LoadAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IndagineHead mIndagineHead = getIntent().getParcelableExtra("Indagine");
            if(global_stat != null){
                mIndagineBody = global_stat.getParcelable(Constants.INDAGINE_BODY_STATE);
            }else{
                if(mIndagineHead.isIndagineInCorso()){
                    mIndagineBody = loadLocalIndagineBody(mIndagineHead);
                }else{
                    mIndagineBody = getIndagineBody(mIndagineHead);
                    mIndagineBody.setHead(mIndagineHead);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setContentView(R.layout.activity_indagine);
            loadInformazioniScroll(mIndagineBody);
            loadQuestionari(mIndagineBody);
            setEneableSubmitAll();
        }
    }

    private void setEneableSubmitAll(){
        int mCount = mIndagineBody.getQuestionari().size() - 1;
        if(mIndagineBody.getQuestionari().get(mCount).isCompilato()){
            Button mSubmitAllButton = (Button) findViewById(R.id.submitAllButton);
            mSubmitAllButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmitIndagine);
            mSubmitAllButton.setBackgroundResource(R.color.colorPrimary);
            mSubmitAllButton.setEnabled(true);
            mSubmitAllButton.setClickable(true);
        }
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
        TextView mTestoTematica = (TextView)findViewById(R.id.descrizioneTextView);
        mTestoTematica.setText(indagineBody.getTematica());

        if(indagineBody.getInformazioni() != null) {
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.infoScrollRecycleView);
            mRecyclerView.setHasFixedSize(true);

            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
            mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            InformazioneAdapter mAdapter = new InformazioneAdapter(indagineBody.getInformazioni(), this);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            TextView mInfoTextView = (TextView) findViewById(R.id.informazioniTextView);
            mInfoTextView.setVisibility(View.GONE);
        }
    }

    private  void loadQuestionari(IndagineBody indagineBody){
        mQuestionariRecyclerView = (RecyclerView) findViewById(R.id.questionariRecycleView);
        mQuestionariRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManagerQuestionari = new LinearLayoutManager(this);
        mLinearLayoutManagerQuestionari.setOrientation(LinearLayoutManager.VERTICAL);
        mQuestionariRecyclerView.setLayoutManager(mLinearLayoutManagerQuestionari);

        QuestionarioAdapter mQuestionarioAdapter = new QuestionarioAdapter(indagineBody.getQuestionari(),
                this, this, this, mQuestionariVisibilityList);
        mQuestionariRecyclerView.setAdapter(mQuestionarioAdapter);
    }

    private IndagineBody getIndagineBody(IndagineHead indagineHead) {
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
        mWebViewIntent.putExtra(Constants.URL, "https://psicologiaunimib.eu.qualtrics.com/jfe/form/SV_czRC4tlVKZDwbVr");
        mWebViewIntent.putExtra(Constants.TITOLO_QUESTIONARIO, mIndagineBody.getQuestionari().get(position).getTitolo());
        mWebViewIntent.putExtra(Constants.QUESTIONARIO_POSITION, position);
        startActivityForResult(mWebViewIntent, Constants.WEB_ACTIVITY_REQUEST_CODE);
    }
}
