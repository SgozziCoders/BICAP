package it.unimib.bicap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import it.unimib.bicap.adapter.InformazioneAdapter;
import it.unimib.bicap.adapter.QuestionarioAdapter;
import it.unimib.bicap.databinding.ActivityIndagineBinding;
import it.unimib.bicap.dialog.DownloadingDialog;
import it.unimib.bicap.dialog.LoadingDialog;
import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.Informazione;
import it.unimib.bicap.repository.IndaginiRepository;
import it.unimib.bicap.utils.Asyn_OpenFile;
import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.utils.FileManager;
import it.unimib.bicap.viewmodel.CardsViewModel;
import it.unimib.bicap.viewmodel.IndagineBodyViewModel;
import it.unimib.bicap.viewmodel.IndagineHeadListViewModel;
import it.unimib.bicap.viewmodel.ProgressBarViewModel;
import it.unimib.bicap.wrapper.DataWrapper;

public class IndagineActivity extends AppCompatActivity implements InformazioneAdapter.OnInfoCardListener,
        QuestionarioAdapter.OnSubmitClickListener, QuestionarioAdapter.InformazioneRowReciver,
        Asyn_OpenFile.OnDownloadListener, IndaginiRepository.OnCallBackListener {

    private IndagineBody mIndagineBody;
    private ActivityIndagineBinding binding;
    private CardsViewModel cardsViewModel;
    private IndagineHeadListViewModel indaginiHeadListViewModel;
    private LoadingDialog mLoadingDialog;
    private DownloadingDialog mDownloadingDialog;
    private String mEmail;
    // static perchè erve accedervi anche dopo la distruzione dell'activity
    private static Asyn_OpenFile asyn_openFile;
    private boolean downloadingDialogVisibilityState;
    private ProgressBarViewModel progressBarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            downloadingDialogVisibilityState = savedInstanceState.getBoolean(Constants.DOWNLOADING_DIALOG_VISIBILITY_ARG);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gloabalInit();
        getEmailFromPreferences();
        viewModelInit();
    }

    private void gloabalInit(){
        binding = ActivityIndagineBinding.inflate(getLayoutInflater());
        indaginiHeadListViewModel = new ViewModelProvider(this).get(IndagineHeadListViewModel.class);
        cardsViewModel = new ViewModelProvider(this).get(CardsViewModel.class);
        mLoadingDialog = new LoadingDialog(this);
        mDownloadingDialog = new DownloadingDialog(this);
        if(downloadingDialogVisibilityState){
            /**
             * Se non si aggiorna il context e il listener viene generato un crash causato dalla
             * ricerca del vecchio context
             **/
            asyn_openFile.restoreListenerAndContext(this, this);
            mDownloadingDialog.startDialog(getString(R.string.dialog_downloading), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    asyn_openFile.cancel(true);
                    mDownloadingDialog.dismissDialog();
                }
            });
        }else{
            mLoadingDialog.startDialog(getString(R.string.dialog_loading_generic), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    private void viewModelInit(){
        /** Indagini ViewModel **/
        IndagineBodyViewModel indagineBodyViewModel = new ViewModelProvider(this).get(IndagineBodyViewModel.class);
        IndagineHead mIndagineHead = getIntent().getParcelableExtra(Constants.INDAGINE_HEAD_ARG);

        /** Observer unico, cambia come vengono ottenuti i dati */
        final IndagineBodyObserver indagineBodyObserver = new IndagineBodyObserver(mIndagineHead);
        if(mIndagineHead.isIndagineInCorso()){
            indagineBodyViewModel.loadLoacalIndagineBody(mIndagineHead.getId(),
                    getApplicationInfo().dataDir).observe(this, indagineBodyObserver);
        }else{
            indagineBodyViewModel.loadRemoteIndagineBody(mIndagineHead.getId())
                    .observe(this, indagineBodyObserver);
        }

        /** ProgressBarViewModel **/
        progressBarViewModel = new ViewModelProvider(this).get(ProgressBarViewModel.class);
        progressBarViewModel.getProgressValue().observe(this, new Observer<Integer>(){
            @Override
            public void onChanged(Integer integer) {
                if(mDownloadingDialog.isVisible())
                    mDownloadingDialog.setProgress(integer);
            }
        });

    }

    /** In questa acrivity l'Email viene utilizzata per il PUT (submit dell'indagine) **/
    private void getEmailFromPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE);
        mEmail = sharedPreferences.getString(Constants.EMAIL_SHARED_PREF_KEY, null);
    }

    private void loadUI(){
        mLoadingDialog.dismissDialog();
        View v = binding.getRoot();
        setContentView(v);
        loadInformazioniScroll(mIndagineBody);
        loadQuestionari(mIndagineBody, cardsViewModel.getVisibilityList(mIndagineBody.getQuestionari().size()));
        setEneableSubmitAll();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(Constants.DOWNLOADING_DIALOG_VISIBILITY_ARG, mDownloadingDialog.isVisible());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            /**
             * Riceve il risultato della WebViewActivity, ovvero della compilazione del questionario:
             * si occupa di aggiornare lo stato del modello (questionario corrente) e del layout
             */
            case(Constants.WEB_ACTIVITY_REQUEST_CODE) :
                if(resultCode == Activity.RESULT_OK){
                    //Modifiche al bottone del questionario corrente
                    int mQuestionarioPosition = data.getExtras().getInt(Constants.QUESTIONARIO_POSITION_ARG);
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
                    String path = this.getApplicationInfo().dataDir + Constants.INDAGINI_IN_CORSO_PATH + mIndagineBody.getHead().getId() + ".json";
                    FileManager.writeToFile(mJsonIndagineBody, path);
                }
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
                                        /**
                                         * Viene eseguito il put sul backend dell'indagine terminata
                                         * e viene rimossa l'indagine dai LiveData del ViewModel
                                         * delle indaginiHeadList
                                         */
                                        IndaginiRepository.getInstance().putIndagineTerminata(mEmail, mIndagineBody.getHead().getId(), IndagineActivity.this);
                                        mLoadingDialog.startDialog(getString(R.string.dialog_sending), null);
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }

    private void loadInformazioniScroll(IndagineBody indagineBody){
        this.setTitle(indagineBody.getHead().getTitoloIndagine());
        binding.descrizioneTextView.setText(indagineBody.getTematica());

        if(indagineBody.getInformazioni().size() != 0) {
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

    private  void loadQuestionari(IndagineBody indagineBody, List<Boolean> viewModelVisibilityList){
        binding.questionariRecycleView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManagerQuestionari = new LinearLayoutManager(this);
        mLinearLayoutManagerQuestionari.setOrientation(LinearLayoutManager.VERTICAL);
        binding.questionariRecycleView.setLayoutManager(mLinearLayoutManagerQuestionari);

        QuestionarioAdapter mQuestionarioAdapter = new QuestionarioAdapter(indagineBody.getQuestionari(),
                this, this, this, viewModelVisibilityList,
                cardsViewModel);
        binding.questionariRecycleView.setAdapter(mQuestionarioAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoCardClick(final int position) {
        if(FileManager.hasStoragePermissions(this)) {
            FileManager.verifyStoragePermissions(this);
            String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mIndagineBody.getInformazioni().get(position).getNomeFile();
            String mUrl = mIndagineBody.getInformazioni().get(position).getFileUrl();
            String mMime = mIndagineBody.getInformazioni().get(position).getTipoFile();
            mDownloadingDialog.startDialog(getString(R.string.dialog_downloading), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    asyn_openFile.cancel(true);
                    mDownloadingDialog.dismissDialog();
                }
            });
            asyn_openFile = new Asyn_OpenFile(mUrl, mPath, mMime, this, this, progressBarViewModel.getProgressValue() );
            asyn_openFile.execute();
        } else {
            FileManager.verifyStoragePermissions(this);
        }
    }

    /** Click ricevuto da un' informazione all'interno di un questionario */
    @Override
    public void OnReciveClick(final int questionarioPosition, final int infoPosition) {
        if(FileManager.hasStoragePermissions(this)) {
            Informazione mInfo = mIndagineBody.getQuestionari().get(questionarioPosition).getInformazioni().get(infoPosition);
            String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mInfo.getNomeFile();
            String mUrl = mInfo.getFileUrl();
            String mMime = mInfo.getTipoFile();
            mDownloadingDialog.startDialog(getString(R.string.dialog_downloading), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    asyn_openFile.cancel(true);
                    mDownloadingDialog.dismissDialog();
                }
            });
            asyn_openFile = new Asyn_OpenFile(mUrl, mPath, mMime, this, this, progressBarViewModel.getProgressValue());
            asyn_openFile.execute();
        } else {
            FileManager.verifyStoragePermissions(this);
        }
    }

    @Override
    public void OnSubmitClick(int position) {
        Intent mWebViewIntent = new Intent(IndagineActivity.this, WebViewActivity.class);
        mWebViewIntent.putExtra(Constants.URL, mIndagineBody.getQuestionari().get(position).getQualtricsUrl());
        mWebViewIntent.putExtra(Constants.TITOLO_QUESTIONARIO_ARG, mIndagineBody.getQuestionari().get(position).getTitolo());
        mWebViewIntent.putExtra(Constants.QUESTIONARIO_POSITION_ARG, position);
        startActivityForResult(mWebViewIntent, Constants.WEB_ACTIVITY_REQUEST_CODE);
    }

    /** Evento di avvenuto download dell' Asyn_OpenFile */
    @Override
    public void onDownloadFinished() {
        mDownloadingDialog.dismissDialog();
    }

    @Override
    public void onDownloadFailed(String error) {
        /**
         * Possibile futuro utilizzo della string error per analizzare l'errore e notificare
         * l'utente della causa
         */
        mDownloadingDialog.dismissDialog();
        new AlertDialog.Builder(IndagineActivity.this)
                .setMessage(R.string.dialog_connection_error)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_close, null)
                .show();
    }

    /**
     * Evento di avvenuta cancellazione dell'indagine lato server: utilizzato per essere sicuri di
     * eliminare l'indagine localmente solo se lo stato della distribuzione è stato correttamente
     * aggiornato lato server, in modo da essere sicuri che non ricompaia nelle indagini disponibili
     **/
    @Override
    public void onPutFinished() {
        indaginiHeadListViewModel.RemoveById(mIndagineBody.getHead().getId());
        FileManager.deleteFile(getApplicationInfo().dataDir +
                Constants.INDAGINI_IN_CORSO_PATH +
                mIndagineBody.getHead().getId() + ".json");
        mLoadingDialog.dismissDialog();
        finish();
    }

    /** Evento di fallimento di comunicazione con il server **/
    @Override
    public void onPutFail(String error) {
        /**
         * Possibile futuro utilizzo della string error per analizzare l'errore e notificare
         * l'utente della causa
         */
        mLoadingDialog.dismissDialog();
        new AlertDialog.Builder(IndagineActivity.this)
                .setMessage(R.string.dialog_connection_error)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_close, null)
                .show();
    }

    /**
     * Observer custom per gestire i dati del ViewModel: creato per evitare duplicate code,
     * le azioni da eseguire con i dati sono le stesse, cambia solo come ottenerli (se localmente
     * o da remoto), ovvero la chiamata al metodo .observe(...)
     */
    private class IndagineBodyObserver implements Observer<DataWrapper<IndagineBody>> {

        private IndagineHead mIndagineHead;

        public IndagineBodyObserver(IndagineHead mIndagineHead){
            super();
            this.mIndagineHead = mIndagineHead;
        }

        @Override
        public void onChanged(DataWrapper<IndagineBody> indagineBodyDataWrapper) {
            if(indagineBodyDataWrapper.getData() != null){
                mIndagineBody = indagineBodyDataWrapper.getData();
                mIndagineBody.setHead(mIndagineHead);
                loadUI();
            }else{
                /**
                 * Possibile analisi dell'errore (classe Exception):
                 * controllare se l'eccezione viene dalla richiesta http di retrofit o da una
                 * lettura dei file locale non andata a buon fine
                 * **/
                mLoadingDialog.dismissDialog();
                new android.app.AlertDialog.Builder(IndagineActivity.this)
                        .setMessage(R.string.dialog_connection_error)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }
}
