package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by David on 2015-04-16.
 */


public class GyroscopeHandler implements SensorEventListener {

    private final String TAG = "GyroscopeHandler";

    public static SlidingWindow window = new SlidingWindow();

    @Override
    public void onSensorChanged(SensorEvent event) {
        double SMV = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        window.newValue(SMV);

//        Log.d(TAG, "Gyroscope: " + Double.toString(SMV));
        DataOut.writeToFile(Double.toString(SMV),"GYR.txt");


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
