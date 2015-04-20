package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Date;


public class TestActivity extends Activity  {
/*
    Button startButton;
    Button stopButton;

    Intent serviceIntent;

    Date lastAcc;
    Date lastGyr;
    Date lastPul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        serviceIntent = new Intent(this, BackgroundService.class);

        Date now = new Date();
        lastAcc = now;
        lastGyr = now;
        lastPul = now;

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
        if (values.type.equals(PulseHandler.PULSE_EVENT)) {
            // If pulse changed
            if (PulseHandler.window.getWindow().getFirst().timeStamp.after(lastPul)) {
                DataOut.simpleTestPrint(PulseHandler.window.getValueArray(), PulseHandler.window.getTimeStampArray(), "pulse");
                lastPul = PulseHandler.window.getWindow().getLast().timeStamp;
            }

        } else if (values.type.equals(AccelerometerHandler.ACCELERATION_EVENT)) {
            // If acceleration changed
            if (AccelerometerHandler.window.getWindow().getFirst().timeStamp.after(lastPul)) {
                DataOut.simpleTestPrint(AccelerometerHandler.window.getValueArray(), AccelerometerHandler.window.getTimeStampArray(), "acceleration");
                lastAcc = AccelerometerHandler.window.getWindow().getLast().timeStamp;
            }
        } else if (values.type.equals(GyroscopeHandler.GYROSCOPE_EVENT)) {
            // If gyroscope changed
            if (GyroscopeHandler.window.getWindow().getFirst().timeStamp.after(lastGyr)) {
                DataOut.simpleTestPrint(GyroscopeHandler.window.getValueArray(), GyroscopeHandler.window.getTimeStampArray(), "gyroscope");
                lastGyr = GyroscopeHandler.window.getWindow().getLast().timeStamp;
            }
        }
    }*/
}
