package com.example.bicap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class IndagineActivity extends AppCompatActivity implements InformazioneAdapter.OnInfoCardListener,
        QuestionarioAdapter.OnSubmitClickListener, QuestionarioAdapter.InformazioneRowReciver {
    private IndagineBody indagineBody;
    private ArrayList<ParcelableBoolean> questionariVisibilityList;
    private RecyclerView questionariRecyclerView;
    private static final int WEB_ACTIVITY_REQUEST_CODE = 1;
    private static final String VISIBILITY_CARDS__STATE = "visibility_cards_state";
    private static final String INDAGINE_BODY_STATE = "indagine_body_state";
    private static final String URL = "url";
    private static final String TITOLO_QUESTIONARIO = "titolo_questionario";
    private static final String QUESTIONARIO_POSITION = "questionario_position";
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
        new Asyn_DownLoadFile().execute(null, null, null);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(global_stat != null){
            questionariVisibilityList = global_stat.getParcelableArrayList(VISIBILITY_CARDS__STATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INDAGINE_BODY_STATE, indagineBody);
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
        outState.putParcelableArrayList(VISIBILITY_CARDS__STATE, questionariVisibilityList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(WEB_ACTIVITY_REQUEST_CODE) :
                if(resultCode == Activity.RESULT_OK){
                    //Aggiornamento dello stato del questionario x
                    Toast.makeText(this, "indagineBody.getInformazioni().get(position).getNomeFile()", Toast.LENGTH_LONG).show();
                    int questionarioPosition = data.getExtras().getInt(QUESTIONARIO_POSITION);
                    indagineBody.getQuestionari().get(questionarioPosition).setCompilato(true);
                    if(questionariRecyclerView.getAdapter().getItemCount() != questionarioPosition + 1){
                        View v = questionariRecyclerView.findViewHolderForAdapterPosition(questionarioPosition + 1).itemView;
                        Button submitButton = (Button) v.findViewById(R.id.submitButton);
                        submitButton.setEnabled(true);
                        submitButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmit);
                    }else{
                        //Attiviamo termina indagine
                        Button submitAllButton = (Button) findViewById(R.id.submitAllButton);
                        submitAllButton.setTextAppearance(this.getApplicationContext(), R.style.EnableSubmitIndagine);
                        submitAllButton.setBackgroundResource(R.color.colorPrimary);
                        submitAllButton.setEnabled(true);
                        submitAllButton.setClickable(true);
                    }

                }
        }
    }

    private class Asyn_DownLoadFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IndagineHead indagineHead = getIntent().getParcelableExtra("Indagine");
            if(global_stat != null){
                indagineBody = global_stat.getParcelable(INDAGINE_BODY_STATE);
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

        downloadFile(url, path);

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

    private String downloadFile(String url, String path){
        try {
            URL u = new URL(url);
            URLConnection urlcon = u.openConnection();
            int contentLength = urlcon.getContentLength();
            InputStream is = urlcon.getInputStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[contentLength];
            int length;

            FileOutputStream fos = new FileOutputStream(new File( path ));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
            return path;
        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
            return null;
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
            return null;
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private void openFile(String path, String mime){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setDataAndType(Uri.parse(path),mime);
        startActivity(sendIntent);



        /*File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Uri uri = FileProvider.getUriForFile(IndagineActivity.this, this.getApplicationContext().getPackageName() + ".provider", file);
        target.setDataAndType(Uri.parse(path),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Apri con");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "NOOOO SI SGHEEEE", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onInfoCardClick(final int position) {
        //Toast.makeText(this, getInformazioniList().get(position).getNomeFile(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, indagineBody.getInformazioni().get(position).getNomeFile(), Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    //http://www.lia.deis.unibo.it/Staff/LucaFoschini/htmlDocs/resources/laTex/scrivereTesiConLaTeX.pdf
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + indagineBody.getInformazioni().get(position).getNomeFile();
                    String file_path = downloadFile(indagineBody.getInformazioni().get(position).getFileUrl(), path);
                    openFile(file_path, indagineBody.getInformazioni().get(position).getTipoFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void OnReciveClick(final int questionarioPosition, final int infoPosition) {
        Toast.makeText(this, "Questionario " + questionarioPosition + ", info: " + infoPosition, Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    //http://www.lia.deis.unibo.it/Staff/LucaFoschini/htmlDocs/resources/laTex/scrivereTesiConLaTeX.pdf
                    Informazione info = indagineBody.getQuestionari().get(questionarioPosition).getInformazioni().get(infoPosition);
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + info.getNomeFile();
                    String file_path = downloadFile(info.getFileUrl(), path);
                    openFile(file_path, info.getTipoFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void OnSubmitClick(int position) {
        Intent webViewIntent = new Intent(IndagineActivity.this, WebViewActivity.class);
        //webViewIntent.putExtra("URL", indagineBody.getQuestionari().get(position).getQualtricsUrl());
        webViewIntent.putExtra(URL, "https://psicologiaunimib.eu.qualtrics.com/jfe/form/SV_czRC4tlVKZDwbVr");
        webViewIntent.putExtra(TITOLO_QUESTIONARIO, indagineBody.getQuestionari().get(position).getTitolo());
        webViewIntent.putExtra(QUESTIONARIO_POSITION, position);
        startActivityForResult(webViewIntent, WEB_ACTIVITY_REQUEST_CODE);
    }
}
