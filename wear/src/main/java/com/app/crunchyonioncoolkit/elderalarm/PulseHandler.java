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
    public static final String PULSE_EVENT = "pulse";

    private static float heartRate;

    public static ArrayList<Event> listeners;

    public static void addEventListener(Event event) {
        if (listeners == null)
            listeners = new ArrayList<>();

        listeners.add(event);
    }

    public static void removeEventListener(Event event) {
        if (listeners != null)
            listeners.remove(event);
    }

    public static void fireEvents(Result value) {
        for (Event event : listeners) {
            event.onChange(value);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartRate = event.values[0];
        this.heartRate = heartRate;
        Log.d(TAG, "heart rate: " + Float.toString(heartRate));

        DecisionMaker.pulseChange(heartRate);
        fireEvents(new Result(PULSE_EVENT, event.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
