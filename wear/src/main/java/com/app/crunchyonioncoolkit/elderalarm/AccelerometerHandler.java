package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by David on 2015-04-02.
 */
public class AccelerometerHandler implements SensorEventListener {

    @Override
    public void onSensorChanged(SensorEvent event) {
        // In thisa example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        float alpha = 0.8f;
        float[] gravity = new float[3];
        float[] linear_acceleration = new float[3];

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        String s = Float.toString(linear_acceleration[0]) + ", " + Float.toString(linear_acceleration[1]) + ", " + Float.toString(linear_acceleration[2]);
        Log.d("ElderAlarm", s);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
