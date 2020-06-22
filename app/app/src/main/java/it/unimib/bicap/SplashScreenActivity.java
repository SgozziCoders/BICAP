package it.unimib.bicap;

import androidx.annotation.NonNull;
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
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.utils.FileManager;
import it.unimib.bicap.viewmodel.IndagineHeadListViewModel;
import it.unimib.bicap.wrapper.DataWrapper;

public class SplashScreenActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setSplashScreenAnimation();
        setVersionText();
        FileManager.checkNeededFolders(this);
        showSpinnerWithDelay();
        if(!getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE)
                .contains(Constants.EMAIL_SHARED_PREF_KEY)){
            openEmailActivity();
        }else{
            getEmailFromPreferences();
            indagineHeadListAPI();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /** Il contentView viene risettato per evitare glitch grafici dovuti alla rotazione **/
        setContentView(R.layout.activity_splash_screen);
        setVersionText();
        /** "Completa" l'animazione e porta il logo in posizione finale**/
        freezeAnimation();
    }

    private void getEmailFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.EMAIL_SHARED_PREF, MODE_PRIVATE);
        email = sharedPreferences.getString(Constants.EMAIL_SHARED_PREF_KEY, null);
    }

    private void showSpinnerWithDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar pb = (ProgressBar) findViewById(R.id.loadingProgressBar);
                pb.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /** Utilizza il ViewModel per caricare la lista di indagini destinate all'utente **/
    private void indagineHeadListAPI() {
        IndagineHeadListViewModel indaginiHeadListViewModel;

        indaginiHeadListViewModel = new ViewModelProvider(this).get(IndagineHeadListViewModel.class);
        final Observer<DataWrapper<IndaginiHeadList>> observer = new Observer<DataWrapper<IndaginiHeadList>>() {
            @Override
            public void onChanged(DataWrapper<IndaginiHeadList> indaginiHeadListDataWrapper) {
                /** Se l'errore è nullo la chiamata è andata a buon fine **/
                if(indaginiHeadListDataWrapper.getError() == null){
                    openTabbedActivity();
                }else{
                    /** Possibile analisi dell'errore (classe Exception) **/
                    indaginiHeadListViewModel.Clear();
                    new android.app.AlertDialog.Builder(SplashScreenActivity.this)
                            .setMessage(R.string.dialog_connection_error)
                            .setCancelable(false)
                            .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }
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
        }, 2000);
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
        }, 2000);
    }

    private void setSplashScreenAnimation() {
        ImageView mBImageView = (ImageView) findViewById(R.id.bImageView);
        ImageView mIcapImageView = (ImageView) findViewById(R.id.icapImageView);
        Animation mBAnimation = AnimationUtils.loadAnimation(this, R.anim.b_animation_start);
        mBAnimation.setFillAfter(true);
        Animation mIcapAnimation = AnimationUtils.loadAnimation(this, R.anim.icap_animation_start);
        mIcapAnimation.setFillAfter(true);
        mBImageView.setAnimation(mBAnimation);
        mIcapImageView.setAnimation(mIcapAnimation);
    }

    /** Posiziona la mano e la scritta nelle posizioni finali dell'animazione **/
    private void freezeAnimation(){
        ImageView mBImageView = (ImageView) findViewById(R.id.bImageView);
        ImageView mIcapImageView = (ImageView) findViewById(R.id.icapImageView);
        Animation mBAnimation = AnimationUtils.loadAnimation(this, R.anim.b_animation_freeze);
        mBAnimation.setFillAfter(true);
        Animation mIcapAnimation = AnimationUtils.loadAnimation(this, R.anim.icap_animation_freeze);
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
                    .setTitle(R.string.dialog_error)
                    .setMessage(R.string.dialog_connection_required_message)
                    .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }
}
