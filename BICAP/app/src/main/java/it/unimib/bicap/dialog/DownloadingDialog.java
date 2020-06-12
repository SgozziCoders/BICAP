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

    public DownloadingDialog(Activity activity){
        this.activity = activity;
    }

    public void startDialog(String loadingMessage, DialogInterface.OnClickListener cancelListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.downloading_progress_dialog, null);
        TextView loadingTextview = (TextView) v.findViewById(R.id.downloadTextView);
        progressBar = (ProgressBar) v.findViewById(R.id.horizontalProgressBar);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        loadingTextview.setText(loadingMessage);
        builder.setView(v);
        builder.setCancelable(false);
        builder.setNegativeButton(activity.getString(R.string.dialog_download_cancel), cancelListener);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    public void setProgress(int value){
        progressBar.setProgress(value);
    }

}
