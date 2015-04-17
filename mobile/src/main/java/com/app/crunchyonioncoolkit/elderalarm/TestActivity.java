package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Date;


public class TestActivity extends Activity implements Event {

    Button startButton;
    Button stopButton;

    Intent serviceIntent;

    Date lastAcc;
    Date lastGyr;
    Date lastPul;
    private Sensor mSensor;
    // Accelerometer
    private SensorManager mSensorManager;
    private AccelerometerHandler accelerometerHandler;
    // Pulse
    private PulseHandler pulseHandler;
    // Gyroscope
    private GyroscopeHandler gyroscopeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.d("TestActivity", "Starting");
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        stopButton = (Button) findViewById(R.id.stop_button2);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestActivity", "STOP!!");
                DataOut.writeToFile("STOP\n", "ACC.txt");
                DataOut.writeToFile("STOP\n", "GYR.txt");
                stop();
            }
        });

        serviceIntent = new Intent(this, BackgroundService.class);

        Date now = new Date();
        lastAcc = now;
        lastGyr = now;
        lastPul = now;


    }
    void initializeSensors() {

        // Accelerometer
        accelerometerHandler = new AccelerometerHandler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(accelerometerHandler, mSensor, 500000000);

        /*// Pulse
        pulseHandler = new PulseHandler();
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(pulseHandler, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
*/
        // Gyroscope
        gyroscopeHandler = new GyroscopeHandler();
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(gyroscopeHandler, mSensor, 500000000);
    }
    void start() {

        initializeSensors();
    }

    void stop() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(accelerometerHandler);
            mSensorManager.unregisterListener(pulseHandler);
            mSensorManager.unregisterListener(gyroscopeHandler);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(accelerometerHandler);
            mSensorManager.unregisterListener(pulseHandler);
            mSensorManager.unregisterListener(gyroscopeHandler);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange(Result values) {
        HTTPTask Task = new HTTPTask();
        Task.execute(values);
    }
}
