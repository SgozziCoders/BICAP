package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndagineAdapter.OnCardListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://code.tutsplus.com/it/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        RecyclerView rv = (RecyclerView)findViewById(R.id.indaginiRecycleView);
        rv.setHasFixedSize(true); // Se si è certi che le dimensioni del RecyclerView non cambieranno, è possibile aggiungere la seguente stringa per migliorare le prestazioni:

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();

        IndagineAdapter adapter = new IndagineAdapter(indaginiHeadList, this);
        rv.setAdapter(adapter);

        verifyStoragePermissions(this);
    }

    //https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android
    public static void verifyStoragePermissions(Activity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //PROVVISORIO
    public IndaginiHeadList getIndaginiHeadList() {
/*
        IndaginiHeadList indaginiHeadList = new IndaginiHeadList();
        List<IndagineHead> lista = new ArrayList<IndagineHead>();

        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));
        lista.add(new IndagineHead("Droghe","Alessio Villani", "https://mangadex.org/images/avatars/default2.jpg", 830075));
        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));
        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));
        lista.add(new IndagineHead("Droghe","Alessio Villani", "https://mangadex.org/images/avatars/default2.jpg", 830075));
        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));


        indaginiHeadList.setHeads(lista);
*/
        try {
            String PATH = getApplicationInfo().dataDir + "/listaIndagini.json";
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(PATH));
            IndaginiHeadList indaginiHeadList = gson.fromJson(br, IndaginiHeadList.class);
            return indaginiHeadList;
        } catch (Exception ex){
            return  null;
        }
    }

    @Override
    public void onCardClick(int position) {
            IndagineHead indagineHead = getIndaginiHeadList().getHeads().get(position);
            Intent intent = new Intent(MainActivity.this, IndagineActivity.class);
            intent.putExtra("Indagine", indagineHead);
            startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bicap_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                PackageInfo pInfo = null;
                try {
                    //https://stackoverflow.com/questions/42491652/send-mail-intent-extra-email-does-not-work
                    pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"receiver@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "[Bicap" + version + "]");
                    startActivity(intent);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
            case R.id.item2:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
