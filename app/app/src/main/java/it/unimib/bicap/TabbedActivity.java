package it.unimib.bicap;

import androidx.annotation.NonNull;
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
import it.unimib.bicap.fragment.EmptyListFragment;
import it.unimib.bicap.fragment.FragmentDisponibili;
import it.unimib.bicap.fragment.FragmentInCorso;
import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.utils.FileManager;
import it.unimib.bicap.viewmodel.IndagineHeadListViewModel;

public class TabbedActivity extends AppCompatActivity {
    public static final String BICAP_SUPPORT_EMAIL = "bicap.unimib+support@gmail.com";
    public static final String MESSAGE_RFC_822 = "message/rfc822";
    private ViewPagerAdapter viewPagerAdapter;
    private ActivityTabbedBinding binding;
    private IndagineHeadListViewModel viewModel;
    private String email;
    private int selectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabbedBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Prendo la mail dalle SharedPreferences
        getEmailFromPreferences();

        FileManager.verifyStoragePermissions(this);
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
        IndaginiHeadList headsDisponibili = viewModel.loadIndaginiHeadList(email).getValue().getData();
        headsInCorso = getIndaginiInCorso();
        for(IndagineHead h : headsInCorso.getHeads()){
            try{
                tmp = headsDisponibili.getIndagineHeadFromId(h.getId());
                headsDisponibili.getHeads().remove(tmp);
            }catch(Exception e){
                return;
            }
        }
        /**
         * Al ritorno su quest'activity bisogna aggiornare i fragment con le recycler view; si
         * controlla quindi se l'adapter è già popolato, in questo caso viene ricreato da zero;
         * se invece non è ancora popolato viene popolato e viene eseguito il set-up del
         * tabLayout;
         * */
        if(viewPagerAdapter.getCount() != 0){
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            loadTabbedLayout(headsDisponibili, headsInCorso);
        }else{
            loadTabbedLayout(headsDisponibili, headsInCorso);
            binding.tabLayout.setupWithViewPager(binding.tabbedViewPager);
        }
    }

    /**
     * Gestisce il caso in cui non ci siano indagini da mostrare, a questo punto sostituisce
     * i fragment con le recyclerView con un semplice fragment avente una label al centro
     * che mostra il messaggio informativo
     **/
    private void loadTabbedLayout(IndaginiHeadList headsDisponibili, IndaginiHeadList headsInCorso){
        String disponibili = getString(R.string.tab_disponibili),
                inCorso = getString(R.string.tab_in_corso);
        if(headsDisponibili != null && headsDisponibili.getHeads().size() > 0){
            viewPagerAdapter.AddFragment(FragmentDisponibili.newInstance(), disponibili, headsDisponibili);
        }else{
            viewPagerAdapter.AddFragment(EmptyListFragment.newInstance(), disponibili, getString(R.string.no_indagini_disponibili));
        }

        if(headsInCorso.getHeads().size() > 0){
            viewPagerAdapter.AddFragment(FragmentInCorso.newInstance(), inCorso, headsInCorso);
        }else{
            viewPagerAdapter.AddFragment(EmptyListFragment.newInstance(), inCorso, getString(R.string.no_indagini_in_corso));
        }
        binding.tabbedViewPager.setAdapter(viewPagerAdapter);
        binding.tabbedViewPager.setCurrentItem(selectedTab, false);
    }

    private void getEmailFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE);
        email = sharedPreferences.getString(Constants.EMAIL_SHARED_PREF_KEY, null);
    }

    /** Legge i file Json delle indagini salvate sul dispositivo nell'apposita cartella **/
    public IndaginiHeadList getIndaginiInCorso() {
        ArrayList<IndagineHead> mListaIndaginiHeadIncorso = new ArrayList<IndagineHead>();

        File[] mListIndaginiIncorsoFile = new File(getApplicationInfo().dataDir+ Constants.INDAGINI_IN_CORSO_PATH).listFiles();
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

    /** Salvataggio per quando l'activity viene distrutta e ricreata (l'utente gira lo schermo) **/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.SELECTED_TAB, binding.tabbedViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedTab = savedInstanceState.getInt(Constants.SELECTED_TAB);
    }

    /**
     * Salvataggio per il ciclo di passaggio di stato tra onPause e onResume; in questo caso
     * possiamo semplicemente assegnare il valore alla variabile prima che l'activity passi nello
     * stato di pausa
     * **/
    @Override
    protected void onPause() {
        super.onPause();
        selectedTab = binding.tabbedViewPager.getCurrentItem();
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
            // Contattaci
            case R.id.item1:
                PackageInfo mPackageInfo = null;
                try {
                    mPackageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    String mVersion = mPackageInfo.versionName;

                    Intent mIntent = new Intent(Intent.ACTION_SEND);
                    mIntent.setType(Constants.MESSAGE_RFC_822);
                    mIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.BICAP_SUPPORT_EMAIL});
                    mIntent.putExtra(Intent.EXTRA_SUBJECT, "[Bicap" + mVersion + "]");
                    startActivity(mIntent);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
            // About
            case R.id.item2:
                Intent mAbout = new Intent(TabbedActivity.this, AboutActivity.class);
                startActivity(mAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
