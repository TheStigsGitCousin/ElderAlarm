package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by David on 2015-04-14.
 */
public class PulseHandler implements SensorEventListener {

    private final String TAG = "PulseHandler";

    private static float heartRate;

    public float getHeartRate() {
        return heartRate;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartRate = event.values[0];
        this.heartRate = heartRate;
        Log.d(TAG, "heart rate: " + Float.toString(heartRate));

        Algorithms.pulseChange(heartRate);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
