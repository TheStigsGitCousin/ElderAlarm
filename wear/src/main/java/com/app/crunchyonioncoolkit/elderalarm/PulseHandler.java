package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by David on 2015-04-14.
 */
public class PulseHandler implements SensorEventListener {

    private final String TAG = "PulseHandler";

    protected static ArrayList<Event> eventListeners;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartRate = event.values[0];
        Log.d(TAG, "heart rate: " + Float.toString(heartRate));

        DecisionMaker.pulseChange(heartRate);
        fireEvent(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void addEventListener(Event event) {
        if (eventListeners == null)
            eventListeners = new ArrayList<>();

        eventListeners.add(event);
    }

    public static void removeEventListener(Event event) {
        if (eventListeners != null)
            eventListeners.remove(event);
    }

    private static void fireEvent(float[] values){
        if(eventListeners!=null && !eventListeners.isEmpty()){
            for(Event event : eventListeners){
                event.onChange(values);
            }
        }
    }
}
