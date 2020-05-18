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
import android.widget.TextView;

import com.example.bicap.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
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
    private IndagineBody indagineBody;
    private ArrayList<ParcelableBoolean> questionariVisibilityList;
    private RecyclerView questionariRecyclerView;
    private static Bundle global_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionariRecyclerView = (RecyclerView) findViewById(R.id.questionariRecycleView);
        if(savedInstanceState != null){
            global_stat = savedInstanceState;
        }else{
            questionariVisibilityList = new ArrayList<ParcelableBoolean>();
        }
        new Asyn_LoadAll().execute(null, null, null);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(global_stat != null){
            questionariVisibilityList = global_stat.getParcelableArrayList(Constants.VISIBILITY_CARDS__STATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.INDAGINE_BODY_STATE, indagineBody);
        questionariVisibilityList.clear();
        int count = questionariRecyclerView.getAdapter().getItemCount();
        View v;
        ConstraintLayout cl;
        for(int i = 0; i < count; i++){
            v = questionariRecyclerView.findViewHolderForAdapterPosition(i).itemView;
            cl = (ConstraintLayout) v.findViewById(R.id.expandableView);
            if(cl.getVisibility() == View.GONE){
                questionariVisibilityList.add(new ParcelableBoolean(false));
            }else{
                questionariVisibilityList.add(new ParcelableBoolean(true));
            }
        }
        outState.putParcelableArrayList(Constants.VISIBILITY_CARDS__STATE, questionariVisibilityList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(Constants.WEB_ACTIVITY_REQUEST_CODE) :
                if(resultCode == Activity.RESULT_OK){
                    //Aggiornamento dello stato del questionario x
                    //Toast.makeText(this, "indagineBody.getInformazioni().get(position).getNomeFile()", Toast.LENGTH_LONG).show();
                    int questionarioPosition = data.getExtras().getInt(Constants.QUESTIONARIO_POSITION);
                    indagineBody.getQuestionari().get(questionarioPosition).setCompilato(true);
                    if(questionariRecyclerView.getAdapter().getItemCount() != questionarioPosition + 1){
                        View v = questionariRecyclerView.findViewHolderForAdapterPosition(questionarioPosition + 1).itemView;
                        Button submitButton = (Button) v.findViewById(R.id.submitButton);
                        submitButton.setEnabled(true);
                        submitButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmit);
                    }else{
                        //Attiviamo termina indagine se è stato compilato l'ultimo questionario
                        Button submitAllButton = (Button) findViewById(R.id.submitAllButton);
                        submitAllButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmitIndagine);
                        submitAllButton.setBackgroundResource(R.color.colorPrimary);
                        submitAllButton.setEnabled(true);
                        submitAllButton.setClickable(true);
                    }
                }
        }
    }

    private class Asyn_LoadAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IndagineHead indagineHead = getIntent().getParcelableExtra("Indagine");
            if(global_stat != null){
                indagineBody = global_stat.getParcelable(Constants.INDAGINE_BODY_STATE);
            }else{
                indagineBody = getIndagineBody(indagineHead);
            }
            indagineBody.setHead(indagineHead);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setContentView(R.layout.activity_indagine);
            loadInformazioniScroll(indagineBody);
            loadQuestionari(indagineBody);
            setEneableSubmitAll();
        }
    }



    private void setEneableSubmitAll(){
        int count = indagineBody.getQuestionari().size() - 1;
        if(indagineBody.getQuestionari().get(count).isCompilato()){
            Button submitAllButton = (Button) findViewById(R.id.submitAllButton);
            submitAllButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmitIndagine);
            submitAllButton.setBackgroundResource(R.color.colorPrimary);
            submitAllButton.setEnabled(true);
            submitAllButton.setClickable(true);
        }
    }

    private void loadInformazioniScroll(IndagineBody indagineBody){
        this.setTitle(indagineBody.getHead().getTitoloIndagine());
        TextView mTestoTematica = (TextView)findViewById(R.id.descrizioneTextView);
        mTestoTematica.setText(indagineBody.getTematica());

        if(indagineBody.getInformazioni() != null) {
            RecyclerView rv = (RecyclerView) findViewById(R.id.infoScrollRecycleView);
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(llm);

            InformazioneAdapter adapter = new InformazioneAdapter(indagineBody.getInformazioni(), this);
            rv.setAdapter(adapter);
        }else{
            TextView infoTextView = (TextView) findViewById(R.id.informazioniTextView);
            infoTextView.setVisibility(View.GONE);
        }
    }

    private  void loadQuestionari(IndagineBody indagineBody){
        questionariRecyclerView = (RecyclerView) findViewById(R.id.questionariRecycleView);
        questionariRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager llmQuestionari = new LinearLayoutManager(this);
        llmQuestionari.setOrientation(LinearLayoutManager.VERTICAL);
        questionariRecyclerView.setLayoutManager(llmQuestionari);

        QuestionarioAdapter questionarioAdapter = new QuestionarioAdapter(indagineBody.getQuestionari(),
                this, this, this, questionariVisibilityList);

        questionariRecyclerView.setAdapter(questionarioAdapter);
    }

    private IndagineBody getIndagineBody(IndagineHead indagineHead) {
        int id = indagineHead.getId();
        String fileName = "Indagine" + id + ".json";
        //String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/" + fileName;
        String url = "https://files.bicap.quarzo.stream/"+ indagineHead.getId() + "/indagine.json";
        String path = getApplicationInfo().dataDir + "/" +fileName;

        FileManager.downloadFile(url, path);

        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(path));
            IndagineBody indagineBody = gson.fromJson(br, IndagineBody.class);
            return indagineBody;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void onInfoCardClick(final int position) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + indagineBody.getInformazioni().get(position).getNomeFile();
        String url = indagineBody.getInformazioni().get(position).getFileUrl();
        String mime = indagineBody.getInformazioni().get(position).getTipoFile();
        new Asyn_OpenFile(url, path, mime, this).execute();
    }

    @Override
    public void OnReciveClick(final int questionarioPosition, final int infoPosition) {
        Informazione info = indagineBody.getQuestionari().get(questionarioPosition).getInformazioni().get(infoPosition);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + info.getNomeFile();
        String url = info.getFileUrl();
        String mime = info.getTipoFile();
        new Asyn_OpenFile(url, path, mime, this).execute();
    }

    @Override
    public void OnSubmitClick(int position) {
        Intent webViewIntent = new Intent(IndagineActivity.this, WebViewActivity.class);
        //webViewIntent.putExtra("URL", indagineBody.getQuestionari().get(position).getQualtricsUrl());
        webViewIntent.putExtra(Constants.URL, "https://psicologiaunimib.eu.qualtrics.com/jfe/form/SV_czRC4tlVKZDwbVr");
        webViewIntent.putExtra(Constants.TITOLO_QUESTIONARIO, indagineBody.getQuestionari().get(position).getTitolo());
        webViewIntent.putExtra(Constants.QUESTIONARIO_POSITION, position);
        startActivityForResult(webViewIntent, Constants.WEB_ACTIVITY_REQUEST_CODE);
    }
}
