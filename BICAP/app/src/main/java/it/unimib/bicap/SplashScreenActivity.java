package it.unimib.bicap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.utils.FileManager;
import it.unimib.bicap.viewmodel.IndagineHeadListViewModel;

public class SplashScreenActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setSplashScreenAnimation();
        setVersionText();
        FileManager.checkNeededFolders(this);
        checkConnection();

        if(!getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE)
                .contains(Constants.EMAIL_SHARED_PREF_KEY)){
            openEmailActivity();
        }else{
            getEmailFromPreferences();
            indagineHeadListAPI();
        }
    }
    private void getEmailFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE);
        email = sharedPreferences.getString(Constants.EMAIL_SHARED_PREF_KEY, null);
    }


    // Utilizza il ViewModel per caricare la lista di indagini destinate all'utente
    private void indagineHeadListAPI() {
        IndagineHeadListViewModel indaginiHeadListViewModel;

        indaginiHeadListViewModel = new ViewModelProvider(this).get(IndagineHeadListViewModel.class);
        final Observer<IndaginiHeadList> observer = new Observer<IndaginiHeadList>() {
            @Override
            public void onChanged(IndaginiHeadList indaginiHeadList) {
                openTabbedActivity();
            }
        };
        indaginiHeadListViewModel.loadIndaginiHeadList(email).observe(this,observer);
    }

    private void openTabbedActivity() {
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent mIntent = new Intent(SplashScreenActivity.this, TabbedActivity.class);
                startActivity(mIntent);
                finish();
            }
        }, 1500);
    }

    private void openEmailActivity() {
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent mIntent = new Intent(SplashScreenActivity.this, EmailActivity.class);
                startActivity(mIntent);
                finish();
            }
        }, 1500);
    }

    private void setSplashScreenAnimation() {
        ImageView mBImageView = (ImageView) findViewById(R.id.bImageView);
        ImageView mIcapImageView = (ImageView) findViewById(R.id.icapImageView);
        Animation mBAnimation = AnimationUtils.loadAnimation(this, R.anim.b_animation);
        mBAnimation.setFillAfter(true);
        Animation mIcapAnimation = AnimationUtils.loadAnimation(this, R.anim.icap_animation);
        mIcapAnimation.setFillAfter(true);
        mBImageView.setAnimation(mBAnimation);
        mIcapImageView.setAnimation(mIcapAnimation);
    }

    private void setVersionText() {
        TextView mVersionTextView = (TextView) findViewById(R.id.versionSplashTextView);
        try {
            PackageInfo mPackageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String mVersion = mPackageInfo.versionName;
            mVersionTextView.setText(mVersionTextView.getText() + " " + mVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return;
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Errore di Connessione")
                    .setMessage("Sembra tu non sia connesso ad Internet, perfavore apri l'applicazione solamente in presenza di una connessione stabile")
                    .setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }
}
