package com.app.crunchyonioncoolkit.elderalarm;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by David on 2015-04-15.
 */
public class BackgroundService extends Service {

    private String TAG = "BackgroundService";

    // Accelerometer
    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private AccelerometerHandler accelerometerHandler;
    // Pulse
    private PulseHandler pulseHandler;
    private Sensor pulseSensor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        initializeSensors();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStart");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mSensorManager.unregisterListener(accelerometerHandler);
        mSensorManager.unregisterListener(pulseHandler);
    }

    void initializeSensors() {

        // Accelerometer
        accelerometerHandler = new AccelerometerHandler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Pulse
        pulseHandler = new PulseHandler();
        pulseSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(pulseHandler, pulseSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
