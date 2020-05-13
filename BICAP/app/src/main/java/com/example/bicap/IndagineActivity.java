package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

public class IndagineActivity extends AppCompatActivity implements InformazioneAdapter.OnInfoCardListener, InformazioneRowAdapter.OnInformazioneRowListener {
    private IndagineBody indagineBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Asyn_DownLoadFile().execute(null, null, null);

    }

    private void loadLista(IndagineBody indagineBody){
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
        }
    }

    private  void loadQuestionari(IndagineBody indagineBody){
        RecyclerView rvQuestionari = (RecyclerView) findViewById(R.id.questionariRecycleView);
        rvQuestionari.setNestedScrollingEnabled(false);

        LinearLayoutManager llmQuestionari = new LinearLayoutManager(this);
        llmQuestionari.setOrientation(LinearLayoutManager.VERTICAL);
        rvQuestionari.setLayoutManager(llmQuestionari);

        QuestionarioAdapter questionarioAdapter = new QuestionarioAdapter(indagineBody.getQuestionari(), this, this);
        rvQuestionari.setAdapter(questionarioAdapter);
    }

    private IndagineBody getIndagineBody(IndagineHead indagineHead) {
        int id = indagineHead.getId();
        String fileName = "Indagine" + id + ".json";
        //String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/" + fileName;
        String url = "http://files.bicap.quarzo.stream/"+ indagineHead.getId() + "/indagine.json";
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

    @Override
    public void onInfoCardClick(int position) {
        //Toast.makeText(this, getInformazioniList().get(position).getNomeFile(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, indagineBody.getInformazioni().get(position).getNomeFile(), Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    //http://www.lia.deis.unibo.it/Staff/LucaFoschini/htmlDocs/resources/laTex/scrivereTesiConLaTeX.pdf
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/informazione2.png";
                    String file_path = downloadFile("https://image.shutterstock.com/image-photo/butterfly-grass-on-meadow-night-260nw-1111729556.jpg", path);
                    openFile(file_path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private String downloadFile(String url, String path){
        try {
            URL u = new URL(url);
            URLConnection urlcon = u.openConnection();
            InputStream is = urlcon.getInputStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
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

    private void openFile(String filename){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setDataAndType(Uri.parse(filename),"image/*");
        startActivity(sendIntent);



        /*File file = new File(filename);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //Uri uri = FileProvider.getUriForFile(IndagineActivity.this, this.getApplicationContext().getPackageName() + ".provider", file);
        target.setDataAndType(Uri.parse(filename),"application/pdf");
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
    public void OnInfoRowClick(int position) {
        Toast.makeText(this, "Click infoRow", Toast.LENGTH_LONG).show();
    }

    private class Asyn_DownLoadFile extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            IndagineHead indagineHead = getIntent().getParcelableExtra("Indagine");
            indagineBody = getIndagineBody(indagineHead);
            indagineBody.setHead(indagineHead);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setContentView(R.layout.activity_indagine);
            loadLista(indagineBody);
            loadQuestionari(indagineBody);
        }
    }
}
