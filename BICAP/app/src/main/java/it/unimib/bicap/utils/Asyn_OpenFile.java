package it.unimib.bicap.utils;

import android.content.Context;
import android.os.AsyncTask;

public class Asyn_OpenFile extends AsyncTask<Void, Void, Void> {
    private String mUrl, mPath, mMime;
    private Context mContext;

    public Asyn_OpenFile(String mUrl, String mPath, String mMime, Context mContext){
        super();
        this.mUrl = mUrl;
        this.mPath = mPath;
        this.mMime = mMime;
        this.mContext = mContext;
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
    }
}