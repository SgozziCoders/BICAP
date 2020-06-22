package it.unimib.bicap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import it.unimib.bicap.utils.Constants;

public class WebViewActivity extends AppCompatActivity{
    private  String mQuestionarioUrl;
    private int mQuestionarioPosition;
    private WebView mWebView;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.qualtricsWebView);

        mQuestionarioUrl = getIntent().getExtras().getString("url");
        this.setTitle(getIntent().getExtras().getString(Constants.TITOLO_QUESTIONARIO_ARG));
        mQuestionarioPosition = getIntent().getExtras().getInt(Constants.QUESTIONARIO_POSITION_ARG);

        webViewInit(mWebView);

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
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, null)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
    }

    private void webViewInit(WebView mWebView){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);

        mWebView.setWebChromeClient(new WebChromeClient() {

            protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });

        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        mWebView.setWebViewClient(new QuestionarioWebClient());
        mWebView.loadUrl(mQuestionarioUrl);
    }



    /**
     * Classe la cui istanza verrà registrata come un interfaccia JavaScript
     */
    private class MyJavaScriptInterface {

        /**
         * Metodo utilizzato per analizzare il contenuto della pagina per gestire il caso in cui
         * il questionario è terminato.
         */
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(String content)  {
            if(content.contains("<div id=\"EndOfSurvey\"")) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                resultIntent.putExtra(Constants.QUESTIONARIO_POSITION_ARG, mQuestionarioPosition);
                finish();
            }
        }
    }

    private class QuestionarioWebClient extends WebViewClient{

        /**
         * Metodo che impedisce il caricamento di pagine che non facciano parte del questionario.
         * api ≥ 21
         */
        @Override
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public boolean shouldOverrideUrlLoading (WebView view, WebResourceRequest request) {
            return stopReindirizzamento(request.getUrl().toString());
        }

        /**
         * Metodo che impedisce il caricamento di pagine che non facciano parte del questionario.
         * api ≤ 21
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            return stopReindirizzamento(url);
        }

        /**
         * Metodo di supporto per capire se bisogna stoppare il reindirizzamento
         */
        private boolean stopReindirizzamento(String url){
            return !url.contains(mQuestionarioUrl);
        }

        /**
         * Metodo che ad ogni caricamento di una risorsa ignetta un codice javascript per ottenere
         * il conntenuro del body della pagina
         */
        @Override
        public void  onLoadResource(WebView view, String url){
            super.onLoadResource(view, url);
            view.loadUrl("javascript:window.INTERFACE.processContent(document.body.innerHTML);");
        }
    }
}
