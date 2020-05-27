package it.unimib.bicap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import it.unimib.bicap.utils.Constants;

public class WebViewActivity extends AppCompatActivity{
    private static String mQuestionarioUrl;
    private static int mQuestionarioPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView mWebview = findViewById(R.id.qualtricsWebView);

        mQuestionarioUrl = getIntent().getExtras().getString("url");
        this.setTitle(getIntent().getExtras().getString(Constants.TITOLO_QUESTIONARIO));
        mQuestionarioPosition = getIntent().getExtras().getInt(Constants.QUESTIONARIO_POSITION);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        mWebview.setWebViewClient(new QuestionarioWebClient());
        mWebview.loadUrl(mQuestionarioUrl);
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
                resultIntent.putExtra(Constants.QUESTIONARIO_POSITION, mQuestionarioPosition);
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
