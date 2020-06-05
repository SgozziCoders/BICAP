package it.unimib.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import it.unimib.bicap.adapter.ViewPagerAdapter;
import it.unimib.bicap.databinding.ActivityTabbedBinding;
import it.unimib.bicap.fragment.FragmentDisponibili;
import it.unimib.bicap.fragment.FragmentInCorso;
import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.viewmodel.IndagineHeadListViewModel;

public class TabbedActivity extends AppCompatActivity {
    private ViewPagerAdapter viewPagerAdapter;
    private ActivityTabbedBinding binding;
    private IndagineHeadListViewModel viewModel;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabbedBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Prendo la mail dalle SharedPreferences
        getEmailFromPreferences();

        verifyStoragePermissions(this);
        setSupportActionBar(binding.toolbar);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewModel = new ViewModelProvider(this).get(IndagineHeadListViewModel.class);
    }

    /**
     * Quando si torna sulla tabbed activity dall'indagineActivity gli elenchi devono essere
     * aggiornati, vengono quindi ricreati totalmente i due fragment e le loro recyclerview
     *
     * NOTA: IndaginiHeadList headsDisponibili = viewModel.loadIndaginiHeadList(email).getValue();
     *       passa un riferimento al viewmodel, di conseguenza i dati vengono aggiornati a livello
     *       globale; in questo modo nonostante i dati non vengano riscaricati ad ogni chiamata
     *       a .loadIndaginiHeadList() il view model resta aggiornato anche localmente.
     */
    @Override
    protected void onResume() {
        super.onResume();
        IndagineHead tmp;
        IndaginiHeadList headsInCorso;
        IndaginiHeadList headsDisponibili = viewModel.loadIndaginiHeadList(email).getValue();
        headsInCorso = getIndaginiInCorso();
        for(IndagineHead h : headsInCorso.getHeads()){
            try{
                tmp = headsDisponibili.getIndagineHeadFromId(h.getId());
                headsDisponibili.getHeads().remove(tmp);
            }catch(Exception e){
                return;
            }
        }
        if(viewPagerAdapter.getCount() != 0){
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.AddFragment(FragmentDisponibili.newInstance(), "Disponibili", headsDisponibili);
            viewPagerAdapter.AddFragment(FragmentInCorso.newInstance(), "In corso", headsInCorso);
            binding.tabbedViewPager.setAdapter(viewPagerAdapter);
        }else{
            viewPagerAdapter.AddFragment(FragmentDisponibili.newInstance(), "Disponibili", headsDisponibili);
            viewPagerAdapter.AddFragment(FragmentInCorso.newInstance(), "In corso", headsInCorso);
            binding.tabbedViewPager.setAdapter(viewPagerAdapter);
            binding.tabLayout.setupWithViewPager(binding.tabbedViewPager);
        }
    }

    private void getEmailFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE);
        email = sharedPreferences.getString(Constants.EMAIL_SHARED_PREF_KEY, null);
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
            // Prompt per richiedere i permessi di scrittura
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    // Legge i file Json delle indagini salvate sul dispositivo nell'apposita cartella
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
