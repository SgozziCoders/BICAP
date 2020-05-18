package it.unimib.bicap.utils;

import android.content.Context;
import android.os.AsyncTask;

public class Asyn_OpenFile extends AsyncTask<Void, Void, Void> {
    private String url, path, mime;
    private Context context;

    public Asyn_OpenFile(String url, String path, String mime, Context context){
        super();
        this.url = url;
        this.path = path;
        this.mime = mime;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FileManager.downloadFile(url, path);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        FileManager.openFile(path, mime, context);
    }
}