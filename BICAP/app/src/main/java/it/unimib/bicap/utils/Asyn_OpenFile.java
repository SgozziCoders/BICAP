package it.unimib.bicap.utils;

import android.content.Context;
import android.os.AsyncTask;

public class Asyn_OpenFile extends AsyncTask<Void, Void, Void> {
    private String mUrl, mPath, mMime;
    private Context mContext;
    private OnPostListener mOnPostListener;

    public Asyn_OpenFile(String mUrl, String mPath, String mMime, Context mContext, OnPostListener mOnPostListener){
        super();
        this.mUrl = mUrl;
        this.mPath = mPath;
        this.mMime = mMime;
        this.mContext = mContext;
        this.mOnPostListener = mOnPostListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FileManager.downloadFile(mUrl, mPath);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        FileManager.openFile(mPath, mMime, mContext);
        mOnPostListener.onFinished();
    }

    /**
     * Listener che riceve la notifica di avvenuto download : usato dall'activity per svolgere
     * operazione dopo l'avvenuto download, nel nostro caso per fermare il LoadingDilog
     *
     * NOTA: il LoadingDialog non può essere maneggiato direttamente nell'Async task per via della
     *       presenza di un thread, il quale impedisce di utilizzare un handler;
     *
     * */
    public interface OnPostListener {
        public void onFinished();
    }
}