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
            URL u = new URL(url);
            URLConnection urlcon = u.openConnection();
            int contentLength = urlcon.getContentLength();
            InputStream is = urlcon.getInputStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[contentLength];
            int length;

            FileOutputStream fos = new FileOutputStream(new File( path ));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void openFile(String path, String mime, Context context){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setDataAndType(Uri.parse(path),mime);
        try{
            context.startActivity(sendIntent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();
        }
    }
}
