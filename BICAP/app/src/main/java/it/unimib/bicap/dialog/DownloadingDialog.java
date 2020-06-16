package it.unimib.bicap.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import it.unimib.bicap.R;

public class DownloadingDialog {

    private Activity activity;
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView percentageTextView, fractionTextView;
    private boolean visibility;

    public DownloadingDialog(Activity activity){
        this.activity = activity;
        this.visibility = false;
    }

    public void startDialog(String loadingMessage, DialogInterface.OnClickListener cancelListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.downloading_progress_dialog, null);
        TextView loadingTextview = (TextView) v.findViewById(R.id.downloadTextView);
        progressBar = (ProgressBar) v.findViewById(R.id.horizontalProgressBar);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        percentageTextView = (TextView) v.findViewById(R.id.percentageTextView);
        fractionTextView = (TextView) v.findViewById(R.id.fractionTextView);
        loadingTextview.setText(loadingMessage);
        builder.setView(v);
        builder.setCancelable(false);
        builder.setNegativeButton(activity.getString(R.string.dialog_download_cancel), cancelListener);
        dialog = builder.create();
        dialog.show();
        visibility = true;
    }

    public void startDialog(String loadingMessage, int startProgressValue, DialogInterface.OnClickListener cancelListener){
        startDialog(loadingMessage, cancelListener);
        setProgress(startProgressValue);
    }

    // Previene nullPointerException nel caso di chiamata a dismiss non preceduta da start
    public void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
            visibility = false;
        }
    }

    public void setProgress(int value){
        progressBar.setProgress(value);
        percentageTextView.setText(value + "%");
        fractionTextView.setText(value + "/100");
    }

    public int getProgress(){
        if(dialog != null)
            return progressBar.getProgress();
        return 0;
    }

    public boolean isVisible(){
        return visibility;
    }

}
