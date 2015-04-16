package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-02.
 */
public class AccelerometerHandler implements SensorEventListener {

    public static SlidingWindow window = new SlidingWindow();

    public static final String ACCELERATION_EVENT = "acceleration";
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
        double SMV = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        window.newValue(SMV);

        fireEvents(new Result(ACCELERATION_EVENT, event.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
