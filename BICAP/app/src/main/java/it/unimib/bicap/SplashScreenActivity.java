package it.unimib.bicap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import it.unimib.bicap.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import it.unimib.bicap.utils.FileManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setSplashScreenAnimation();
        setVersionText();
        FileManager.checkNeededFolders(this);
        checkConnection();
    }

    private void openMainActivity() {
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
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            //Controllare se il telefono è connesso ad internet
            new Asyn_SplashScreenDownLoadFile().execute(null, null, null);

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
            connected = false;
        }
    }
    private class Asyn_SplashScreenDownLoadFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Controllare se il telefono è connesso ad internet
            String mUrl = "https://files.bicap.quarzo.stream/listaIndagini.json";
            String mPath = getApplicationInfo().dataDir + "/tmp/listaIndagini.json";
            FileManager.downloadFile(mUrl, mPath);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            File email_file = new File(getApplicationInfo().dataDir + "/email.txt");
            if(!email_file.exists())
                openEmailActivity();
            else
                openMainActivity();
        }
    }

}
