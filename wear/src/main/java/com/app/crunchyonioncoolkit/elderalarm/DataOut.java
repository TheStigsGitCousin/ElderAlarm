package com.app.crunchyonioncoolkit.elderalarm;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

/**
 * Created by Jack on 2015-04-15.
 */
public class DataOut {
    public static File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_NOTIFICATIONS), albumName);
        return file;
    }

    public static void writeToFile(String data, String Path) {
        try {
            FileOutputStream f = new FileOutputStream(getAlbumStorageDir(Path), true);
            OutputStreamWriter pw = new OutputStreamWriter(f);
            pw.append(data + ";" + Calendar.getInstance().getTimeInMillis() + "\n");
            pw.close();
            f.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void deleteFile(String path) {
        File file = getAlbumStorageDir(path);
        file.delete();
    }

    public static String readFromFile(String path) {

        String ret = "";

        try {
            FileInputStream inputStream = new FileInputStream(getAlbumStorageDir(path));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
