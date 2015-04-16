package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by David on 2015-04-16.
 */
public class GyroscopeHandler implements SensorEventListener {
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("GyroscopeHandler", "gyroscope change");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
