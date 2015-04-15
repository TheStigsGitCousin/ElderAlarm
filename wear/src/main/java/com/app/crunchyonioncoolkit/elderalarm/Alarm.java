package com.app.crunchyonioncoolkit.elderalarm;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on 2015-04-13.
 */
public class Alarm {

    private final String TAG = "Alarm";
    public static final String ALARM_EVENT="alarm";

    Handler mHandler;
    private int cancelationTime=10000;

    public static ArrayList<Event> listeners;

    // Add listener to listener list
    public static void addEventListener(Event event) {
        if (listeners == null)
            listeners = new ArrayList<>();

        listeners.add(event);
    }

    // Remove listener from listener list
    public static void removeEventListener(Event event) {
        if (listeners != null)
            listeners.remove(event);
    }

    // Fire of an event to all listeners with the result
    public static void fireEvents(Result values) {
        for (Event event : listeners) {
            event.onChange(values);
        }
    }

    public void detectedFall() {
        startCancelationProcedure();
    }

    public void detectedCardiacArrest() {
        startCancelationProcedure();
    }

    // Cancel alarm procedure
    public void cancelAlarm() {
        startCancelationProcedure();
    }

    // Start countdown of cancelation
    public void startCountdown() {
        mHandler = new Handler();
        // Delay exectution of mRunnable (thread) by 'cancelationTime' seconds
        mHandler.postDelayed(mRunnable, cancelationTime);
    }

    // A thread running after a specified number of seconds which sets of alarm
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            Log.e("Handlers", "Calls");
            // Activate alarm, send event to listeners
            fireEvents(new Result("alarm", null));
        }
    };

    // Cancel alarm by reomving callback to the thread
    private void startCancelationProcedure() {
        mHandler.removeCallbacks(mRunnable);
        Log.d(TAG, "ALARM CANCELED");
    }
}
