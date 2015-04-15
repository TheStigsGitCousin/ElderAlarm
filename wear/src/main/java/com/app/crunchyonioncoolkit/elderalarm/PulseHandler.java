package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-14.
 */
public class PulseHandler implements SensorEventListener {

    private final String TAG = "PulseHandler";

    public static ArrayList<Event> listeners;

    private static float heartRate;

    public float getHeartRate() {
        return heartRate;
    }

    public static void addEventListener(Event event){
        if(listeners==null)
            listeners=new ArrayList<>();

        listeners.add(event);
    }

    public static void removeEventListener(Event event){
        if(listeners!=null)
            listeners.remove(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartRate = event.values[0];
        this.heartRate = heartRate;
        Log.d(TAG, "heart rate: " + Float.toString(heartRate));

        DecisionMaker.pulseChange(heartRate);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
