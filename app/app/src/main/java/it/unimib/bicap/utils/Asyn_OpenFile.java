package it.unimib.bicap.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;

import androidx.lifecycle.MutableLiveData;

public class Asyn_OpenFile extends AsyncTask<Void, Void, Void> {
    private String mUrl, mPath, mMime;
    private Context mContext;
    private OnDownloadListener mOnDownloadListener;
    private String error;
    private MutableLiveData<Integer> progress;

    public Asyn_OpenFile(String mUrl, String mPath, String mMime, Context mContext, OnDownloadListener mOnDownloadListener,
                         MutableLiveData<Integer> progress){
        super();
        this.mUrl = mUrl;
        this.mPath = mPath;
        this.mMime = mMime;
        this.mContext = mContext;
        this.mOnDownloadListener = mOnDownloadListener;
        this.error = null;
        this.progress = progress;
    }

    public void restoreListenerAndContext(Context context, OnDownloadListener onDownloadListener){
        this.mContext = context;
        this.mOnDownloadListener = onDownloadListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            FileManager.downloadFile(mUrl, mPath, progress);
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
            mOnDownloadListener.onDownloadFinished();
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
        public void onDownloadFinished();
        public void onDownloadFailed(String errorMessage);
    }
}