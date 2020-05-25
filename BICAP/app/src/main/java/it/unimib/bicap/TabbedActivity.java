package it.unimib.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import it.unimib.bicap.adapter.ViewPagerAdapter;
import it.unimib.bicap.fragment.FragmentDisponibili;
import it.unimib.bicap.fragment.FragmentInCorso;
import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.IndaginiHeadList;

public class TabbedActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private IndaginiHeadList headsDisponibili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.tabbedViewPager);
        viewPager.setSaveEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        headsDisponibili = getIndaginiHeadList();



    }

    @Override
    protected void onResume() {
        super.onResume();
        IndagineHead tmp;
        IndaginiHeadList  headsInCorso;

        headsInCorso = getIndaginiInCorso();
        for(IndagineHead h : headsInCorso.getHeads()){
            try{
                tmp = headsDisponibili.getIndagineHeadFromId(h.getId());
                headsDisponibili.getHeads().remove(tmp);
            }catch(Exception e){
                //h deve essere eliminata dal database locale
            }
        }

        if(viewPagerAdapter.getCount() != 0){
            //diocane
        }else{
            viewPagerAdapter.AddFragment(new FragmentDisponibili(headsDisponibili), "Disponibili");
            viewPagerAdapter.AddFragment(new FragmentInCorso(headsInCorso), "In corso");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
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

    public IndaginiHeadList getIndaginiInCorso() {
        ArrayList<IndagineHead> mListaIndaginiHeadIncorso = new ArrayList<IndagineHead>();

        File[] mListIndaginiIncorsoFile = new File(getApplicationInfo().dataDir+ "/indagini/in_corso").listFiles();
        try {
            for(int i=0; i<mListIndaginiIncorsoFile.length; i++) {
                BufferedReader mBufferedReader = new BufferedReader(new FileReader(mListIndaginiIncorsoFile[i].getAbsolutePath()));
                IndagineBody mIndagineBody = new Gson().fromJson(mBufferedReader, IndagineBody.class);
                mListaIndaginiHeadIncorso.add(mIndagineBody.getHead());
            }

            return new IndaginiHeadList(mListaIndaginiHeadIncorso);
        } catch (Exception ex){
            return  null;
        }
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
                Intent mAbout = new Intent(TabbedActivity.this, AboutActivity.class);
                startActivity(mAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
