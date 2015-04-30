package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by David on 2015-04-02.
 */
public class AccelerometerHandler implements SensorEventListener {
    public static String ACC_PATH;
    public static String TAG = "AccelerometerHandler";
    public static SlidingWindow window = new SlidingWindow();


    @Override
    public void onSensorChanged(SensorEvent event) {
        double SMV = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        window.newValue(SMV);
        Log.d("Accelerom", "On change");
        DataOut.writeToFile(Double.toString(SMV), ACC_PATH);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void testChange(double SMV) {
        synchronized (window) {
            window.newValue(SMV);
        }
        Log.d(TAG, "On testChange, SMV:  " + SMV);
        DecisionMaker.fallDetection();
    }
}
