package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-16.
 */


public class GyroscopeHandler implements SensorEventListener {

    public static SlidingWindow window = new SlidingWindow();

    @Override
    public void onSensorChanged(SensorEvent event) {
        double SMV = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        window.newValue(SMV);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
