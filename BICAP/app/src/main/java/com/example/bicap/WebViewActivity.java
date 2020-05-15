package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView mWebview = findViewById(R.id.qualtricsWebView);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.setWebViewClient(new WebViewClient());
        mWebview.loadUrl(getIntent().getExtras().getString("URL"));
        //mWebview.loadUrl("http://www.google.com/");
    }

    @Override
    public void onBackPressed() {
        WebView mWebview = findViewById(R.id.qualtricsWebView);
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        }else{
            new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            WebViewActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, null)
                    .show();
        }
    }

}
