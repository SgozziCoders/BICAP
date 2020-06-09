package it.unimib.bicap.utils;

import android.content.Context;
import android.os.AsyncTask;

public class Asyn_OpenFile extends AsyncTask<Void, Void, Void> {
    private String mUrl, mPath, mMime;
    private Context mContext;
    private OnDownloadListener mOnDownloadListener;
    private String error;

    public Asyn_OpenFile(String mUrl, String mPath, String mMime, Context mContext, OnDownloadListener mOnDownloadListener){
        super();
        this.mUrl = mUrl;
        this.mPath = mPath;
        this.mMime = mMime;
        this.mContext = mContext;
        this.mOnDownloadListener = mOnDownloadListener;
        this.error = null;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            FileManager.downloadFile(mUrl, mPath);
        }catch (Exception ex){
            error = ex.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(error != null){
            mOnDownloadListener.onDownloadFailed(error);
        }else{
            FileManager.openFile(mPath, mMime, mContext);
            mOnDownloadListener.onFinished();
        }
    }

    /**
     * Listener che riceve la notifica di avvenuto download : usato dall'activity per svolgere
     * operazione dopo l'avvenuto download, nel nostro caso per fermare il LoadingDilog
     *
     * NOTA: il LoadingDialog non può essere maneggiato direttamente nell'Async task per via della
     *       presenza di un thread, il quale impedisce di utilizzare un handler;
     *
     * */
    public interface OnDownloadListener {
        public void onFinished();
        public void onDownloadFailed(String errorMessage);
    }
}