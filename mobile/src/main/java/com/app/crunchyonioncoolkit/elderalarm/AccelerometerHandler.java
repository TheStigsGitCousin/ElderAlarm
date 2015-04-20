package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by David on 2015-04-02.
 */
public class AccelerometerHandler implements SensorEventListener {

    public static SlidingWindow window = new SlidingWindow();

    @Override
    public void onSensorChanged(SensorEvent event) {
        double SMV = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        window.newValue(SMV);

        DecisionMakerSimple.detectFall();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void testChange(double SMV){
        window.newValue(SMV);

        DecisionMakerSimple.detectFall();
    }
}
