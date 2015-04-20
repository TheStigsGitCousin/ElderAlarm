package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-16.
 */


public class GyroscopeHandler implements SensorEventListener {

    public static SlidingWindow window = new SlidingWindow();

    public static final String GYROSCOPE_EVENT = "gyroscope";
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
        DataOut.writeToFile(Double.toString(SMV), "GYR.txt");
        //fireEvents(new Result(GYROSCOPE_EVENT, event.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
