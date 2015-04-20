package com.app.crunchyonioncoolkit.elderalarm;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by David on 2015-04-10.
 */
public class HTTPTask extends AsyncTask<Result, Boolean, Boolean> {
    static Calendar lastAcc = Calendar.getInstance();
    static Calendar lastGyr = Calendar.getInstance();
    static Calendar lastPul = Calendar.getInstance();

    @Override
    protected Boolean doInBackground(Result... values) {
        Log.d("TestActivity", "onChange");
        if (values[0].type.equals(PulseHandler.PULSE_EVENT)) {
            // If pulse changed
            if (PulseHandler.window.getWindow().getFirst().timeStamp.after(lastPul)) {
                DataOut.simpleTestPrint(PulseHandler.window.getValueArray(), PulseHandler.window.getTimeStampArray(), "pulse.txt");
                lastPul = PulseHandler.window.getWindow().getLast().timeStamp;
                lastPul.add(Calendar.SECOND, 1);
            }

        } else if (values[0].type.equals(AccelerometerHandler.ACCELERATION_EVENT)) {
            // If acceleration changed
            if (!AccelerometerHandler.window.getWindow().isEmpty() && AccelerometerHandler.window.getWindow().getFirst().timeStamp.after(lastPul)) {
                DataOut.simpleTestPrint(AccelerometerHandler.window.getValueArray(), AccelerometerHandler.window.getTimeStampArray(), "acceleration.txt");
                lastAcc = AccelerometerHandler.window.getWindow().getLast().timeStamp;
                lastAcc.add(Calendar.SECOND, 1);
            }
        } else if (values[0].type.equals(GyroscopeHandler.GYROSCOPE_EVENT)) {
            // If gyroscope changed
            if (!GyroscopeHandler.window.getWindow().isEmpty() &&GyroscopeHandler.window.getWindow().getFirst().timeStamp.after(lastGyr)) {
                DataOut.simpleTestPrint(GyroscopeHandler.window.getValueArray(), GyroscopeHandler.window.getTimeStampArray(), "gyroscope.txt");
                lastGyr = GyroscopeHandler.window.getWindow().getLast().timeStamp;
                lastGyr.add(Calendar.SECOND, 1);
            }
        }

        return true;
   }
}
