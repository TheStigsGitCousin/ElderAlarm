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

    private Sensor mSensor;
    // Accelerometer
    private SensorManager mSensorManager;
    private AccelerometerHandler accelerometerHandler;
    // Pulse
    private PulseHandler pulseHandler;
    // Gyroscope
    private GyroscopeHandler gyroscopeHandler;

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
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(accelerometerHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Pulse
        pulseHandler = new PulseHandler();
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(pulseHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Gyroscope
        gyroscopeHandler = new GyroscopeHandler();
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(gyroscopeHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
