package com.app.crunchyonioncoolkit.elderalarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by David on 2015-04-14.
 */
public class PulseHandler implements SensorEventListener {

    private final String TAG = "PulseHandler";
    public static final String PULSE_EVENT = "pulse";
    private boolean writeToFile = false;

    // An instance of SlidingWindow to store sensor data
    public static SlidingWindow window = new SlidingWindow();
    // Last known accuracy
    private int accuracy = 0;

    // Sub
    public static EventListener listener = new EventListener();

    public PulseHandler(boolean writeToFile) {
        // Initialize EventListener for receiving pulse data in AlarmActivity

        this.writeToFile = writeToFile;
    }

    // Fired when senor data changed
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check if accuracy is sufficient
        if (accuracy != SensorManager.SENSOR_STATUS_NO_CONTACT && accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE) {
            window.newValue(event.values[0]);

            // Write event data to file for test purposes
            if (writeToFile)
                DataOut.writeToFile(Double.toString(event.values[0]), "PUL.txt");

            // Fire event with last float array with pulse data
            listener.fireEvents(new Result(PULSE_EVENT, event.values));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Save the changed accuracy
        this.accuracy = accuracy;
    }
}
