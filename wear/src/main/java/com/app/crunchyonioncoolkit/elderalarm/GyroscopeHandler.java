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
    public static SlidingWindowGyro windowRaw = new SlidingWindowGyro();

    @Override
    public void onSensorChanged(SensorEvent event) {
        double SMV = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        window.newValue(SMV);
        //windowRaw.newRawValue(event.values[0],event.values[1],event.values[2]);

       //Log.d(TAG, "Gyroscope: " + "X: " + Double.toString(event.values[0]) + "Y: " + Double.toString(event.values[1]) + "Z: " + Double.toString(event.values[2]));
        //DataOut.writeToFile(Double.toString(SMV),"GYR.txt");


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
