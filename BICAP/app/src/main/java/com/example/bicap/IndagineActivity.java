package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class IndagineActivity extends AppCompatActivity implements InformazioneAdapter.OnInfoCardListener, QuestionarioAdapter.OnQuestionarioCardListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indagine);

        //IndagineHead indagineHead = getIntent().getParcelableExtra("Indagine");
        //this.setTitle(indagineHead.getTitoloIndagine());

        RecyclerView rv = (RecyclerView)findViewById(R.id.infoScrollRecycleView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);

        List<Informazione> informazioneList = getInformazioniList();

        InformazioneAdapter adapter = new InformazioneAdapter(informazioneList, this);
        rv.setAdapter(adapter);

        //================QUESTIONARI=====================
        RecyclerView rvQuestionari = (RecyclerView) findViewById(R.id.questionariRecycleView);
        //rvQuestionari.setNestedScrollingEnabled(false);

        LinearLayoutManager llmQuestionari = new LinearLayoutManager(this);
        llmQuestionari.setOrientation(LinearLayoutManager.VERTICAL);
        rvQuestionari.setLayoutManager(llmQuestionari);

        List<Questionario> questionarioList = getQuestionariList();

        QuestionarioAdapter questionarioAdapter = new QuestionarioAdapter(questionarioList, this);
        rvQuestionari.setAdapter(questionarioAdapter);

    }

    private List<Questionario> getQuestionariList() {
        List<Questionario> lista = new ArrayList<>();

        lista.add(new Questionario("QUESTIONARIO A", "url", null));
        lista.add(new Questionario("QUESTIONARIO B", "url", null));
        lista.add(new Questionario("QUESTIONARIO C", "url", null));
        lista.add(new Questionario("QUESTIONARIO D", "url", null));
        lista.add(new Questionario("QUESTIONARIO E", "url", null));
        lista.add(new Questionario("QUESTIONARIO F", "url", null));
        lista.add(new Questionario("QUESTIONARIO G", "url", null));

        return lista;
    }

    private List<Informazione> getInformazioniList() {
        List<Informazione> lista = new ArrayList<>();

        lista.add(new Informazione("Informazione.pdf", "url_file", "document/pdf", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.mp3", "url_file", "audio/mp3", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.txt", "url_file", "document/txt", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.avi", "url_file", "video/avi", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.txt", "url_file", "document/txt", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.mp4", "url_file", "document/pdf", "https://mangadex.org/images/avatars/default2.jpg"));

        return lista;
    }

    @Override
    public void onInfoCardClick(int position) {
        Toast.makeText(this, getInformazioniList().get(position).getNomeFile(), Toast.LENGTH_LONG).show();


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //http://www.lia.deis.unibo.it/Staff/LucaFoschini/htmlDocs/resources/laTex/scrivereTesiConLaTeX.pdf
                    String file_path = downloadFile("https://image.shutterstock.com/image-photo/butterfly-grass-on-meadow-night-260nw-1111729556.jpg");
                    openFile(file_path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onQuestionarioCardClick(int position) {
        //-------------
    }

    private String downloadFile(String url){
        try {
            URL u = new URL(url);
            URLConnection urlcon = u.openConnection();
            InputStream is = urlcon.getInputStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/informazione2.png";
            FileOutputStream fos = new FileOutputStream(new File( PATH ));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
            return PATH;
        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
            return null;
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
            return null;
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
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


}
