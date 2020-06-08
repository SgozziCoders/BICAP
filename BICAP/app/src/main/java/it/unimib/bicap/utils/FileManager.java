package it.unimib.bicap.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import androidx.core.content.FileProvider;

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

        File file = new File(path);
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file );

        if (file.exists()) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri , mime);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Open with");

            try {
                context.startActivity(chooser);
            }
            catch (ActivityNotFoundException e) {
                Log.e("error","error"+e);
            }
        }
    }

    public static void checkFolder(String mDirectoryPath) {
        File mDirectory = new File(mDirectoryPath);
        if (!mDirectory.exists())
            mDirectory.mkdirs();
    }

    public static void checkNeededFolders(Context context){
        checkFolder(context.getApplicationInfo().dataDir + Constants.TMP_PATH);
        checkFolder(context.getApplicationInfo().dataDir + Constants.INDAGINI_PATH);
        checkFolder(context.getApplicationInfo().dataDir + Constants.INDAGINI_IN_CORSO_PATH);
    }

    public static void writeToFile(String data, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void deleteFile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }
}
