package com.app.crunchyonioncoolkit.elderalarm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by David on 2015-04-15.
 */
public class BackgroundService extends IntentService {

    // Accelerometer
    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private AccelerometerHandler accelerometerHandler;

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initializeSensors();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(accelerometerHandler);
    }

    void initializeSensors() {

        // Accelerometer
        accelerometerHandler = new AccelerometerHandler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
