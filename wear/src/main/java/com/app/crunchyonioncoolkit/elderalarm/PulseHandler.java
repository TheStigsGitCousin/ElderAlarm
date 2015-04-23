package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-14.
 */
public class PulseHandler implements SensorEventListener {

    private final String TAG = "PulseHandler";
    public static final String PULSE_EVENT = "pulse";

    public static SlidingWindow window = new SlidingWindow();
    private int accuracy = 0;

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
        if (listeners != null) {

            for (Event event : listeners) {
                event.onChange(value);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (accuracy != SensorManager.SENSOR_STATUS_NO_CONTACT && accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE) {
            window.newValue(event.values[0]);

//            Log.d(TAG, "---------------");
//            for (int i=0;i<3;i++) {
//                Log.d(TAG, "pulse: " + Float.toString(event.values[i]));//+", accuracy = "+Integer.toString(accuracy));
//            }
//            Log.d(TAG, "---------------");

            fireEvents(new Result(PULSE_EVENT, event.values));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        this.accuracy = accuracy;
    }
}
