package it.unimib.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import it.unimib.bicap.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;

import it.unimib.bicap.adapter.IndagineAdapter;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.IndaginiHeadList;

public class MainActivity extends AppCompatActivity implements IndagineAdapter.OnCardListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.indaginiRecycleView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        IndaginiHeadList mIndaginiHeadList = getIndaginiHeadList();

        IndagineAdapter mAdapter = new IndagineAdapter(mIndaginiHeadList, this);
        mRecyclerView.setAdapter(mAdapter);

        verifyStoragePermissions(this);
    }

    public static void verifyStoragePermissions(Activity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        //  Controllo se si hanno i permessi di scrittura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Pompt per richiedere i permessi di scrittura
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public IndaginiHeadList getIndaginiHeadList() {
        try {
            String mPath = getApplicationInfo().dataDir + "/tmp/listaIndagini.json";
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(mPath));
            IndaginiHeadList mIndaginiHeadList = new Gson().fromJson(mBufferedReader, IndaginiHeadList.class);
            return mIndaginiHeadList;
        } catch (Exception ex){
            return  null;
        }
    }

    @Override
    public void onCardClick(int position) {
            IndagineHead mIndagineHead = getIndaginiHeadList().getHeads().get(position);
            Intent mIntent = new Intent(MainActivity.this, IndagineActivity.class);
            mIntent.putExtra("Indagine", mIndagineHead);
            startActivity(mIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.bicap_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                PackageInfo mPackageInfo = null;
                try {
                    mPackageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    String mVersion = mPackageInfo.versionName;

                    Intent mIntent = new Intent(Intent.ACTION_SEND);
                    mIntent.setType("message/rfc822");
                    mIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bicap.unimib+support@gmail.com"});
                    mIntent.putExtra(Intent.EXTRA_SUBJECT, "[Bicap" + mVersion + "]");
                    startActivity(mIntent);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
            case R.id.item2:
                Intent mAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(mAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
