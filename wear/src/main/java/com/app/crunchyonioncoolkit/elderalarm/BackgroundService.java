package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by David on 2015-04-15.
 */
public class BackgroundService extends Service {
    public static Context context;
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
        context = this;
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean writeToFile = intent.getBooleanExtra("writeToFile", false);
        super.onStartCommand(intent, flags, startId);
        if (mSensorManager != null) {
            unregisterLiteners();
        }
        initializeSensors(writeToFile);
        Log.d(TAG, "onStart");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        unregisterLiteners();
    }

    void unregisterLiteners() {
        mSensorManager.unregisterListener(accelerometerHandler);
        mSensorManager.unregisterListener(pulseHandler);
        mSensorManager.unregisterListener(gyroscopeHandler);
    }

    void initializeSensors(boolean writeToFile) {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Accelerometer
        accelerometerHandler = new AccelerometerHandler(writeToFile);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(accelerometerHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

//        for(Sensor sensor:mSensorManager.getSensorList(Sensor.TYPE_ALL)){
//            Log.d(TAG,"Sensor: "+sensor.getName()+", "+sensor.getStringType()+", "+sensor.getVendor()+", "+sensor.getResolution()+", "+sensor.getType()+", "+sensor.getVersion()+", "+sensor.getPower()+", "+sensor.getReportingMode());
//        }

        // Pulse
        pulseHandler = new PulseHandler(writeToFile);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(pulseHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Gyroscope
        gyroscopeHandler = new GyroscopeHandler(writeToFile);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(gyroscopeHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
