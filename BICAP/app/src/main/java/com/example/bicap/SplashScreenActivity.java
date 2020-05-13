package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setSplashScreenAnimation();
        setVersionText();
        new Asyn_SplashScreenDownLoadFile().execute(null, null, null);
    }

    private void openMainActivity() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private void setSplashScreenAnimation() {
        ImageView bImageView = (ImageView) findViewById(R.id.bImageView);
        ImageView icapImageView = (ImageView) findViewById(R.id.icapImageView);
        Animation bAnimation = AnimationUtils.loadAnimation(this, R.anim.b_animation);
        bAnimation.setFillAfter(true);
        Animation icapAnimation = AnimationUtils.loadAnimation(this, R.anim.icap_animation);
        icapAnimation.setFillAfter(true);
        bImageView.setAnimation(bAnimation);
        icapImageView.setAnimation(icapAnimation);
    }

    private void setVersionText() {
        TextView versionTextView = (TextView) findViewById(R.id.versionSplashTextView);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionTextView.setText(versionTextView.getText() + " " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void downLoadFile() {
        try {
            //String url = "https://raw.githubusercontent.com/SgozziCoders/BICAP/master/Json/listaIndagini.json";
            String url = "http://files.bicap.quarzo.stream/listaIndagini.json";
            URL u = new URL(url);
            URLConnection urlcon = u.openConnection();
            InputStream is = urlcon.getInputStream();

            DataInputStream dis = new DataInputStream(is);
            byte[] buffer = new byte[1024];
            int length;

            String PATH = getApplicationInfo().dataDir + "/listaIndagini.json";
            FileOutputStream fos = new FileOutputStream(new File( PATH ));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }

    private class Asyn_SplashScreenDownLoadFile extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            //Controllare se il telefono è connesso ad internet
            downLoadFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            openMainActivity();
        }

    }
}
