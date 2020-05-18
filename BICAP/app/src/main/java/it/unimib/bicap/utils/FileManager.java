package it.unimib.bicap.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileManager {
    public static boolean downloadFile(String url, String path){
        try {
            URL mUrl = new URL(url);
            URLConnection mUrlCon = mUrl.openConnection();
            int contentLength = mUrlCon.getContentLength();
            InputStream is = mUrlCon.getInputStream();

            DataInputStream mDataInputStream = new DataInputStream(is);
            byte[] mBuffer = new byte[contentLength];
            int mLength;
            FileOutputStream mFileOutputStream = new FileOutputStream(new File( path ));

            while ((mLength = mDataInputStream.read(mBuffer))>0) {
                mFileOutputStream.write(mBuffer, 0, mLength);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void openFile(String path, String mime, Context context){
        Intent mSendIntent = new Intent(Intent.ACTION_VIEW);
        mSendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mSendIntent.setDataAndType(Uri.parse(path),mime);
        try{
            context.startActivity(mSendIntent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();
        }
    }
}
