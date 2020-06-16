package it.unimib.bicap.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import it.unimib.bicap.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void startDialog(String loadingMessage, DialogInterface.OnClickListener cancelListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.loading_dialog, null);
        TextView loadingTextview = (TextView) v.findViewById(R.id.loadingTextView);
        loadingTextview.setText(loadingMessage);
        builder.setView(v);
        builder.setCancelable(false);
        builder.setNegativeButton(activity.getString(R.string.dialog_cancel), cancelListener);
        dialog = builder.create();
        dialog.show();
    }

    // Previene nullPointerException nel caso di chiamata a dismiss non preceduta da start
    public void dismissDialog(){
        if(dialog != null)
            dialog.dismiss();
    }


}
