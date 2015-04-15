package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class AlarmActivity extends Activity implements Event{

    private final String TAG = "AlarmActivity";

    Alarm alarm;

    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                cancelAlarm();
                return false;
            }
        });

        Intent intent = getIntent();
        MyParcelable data = intent.getExtras().getParcelable("message");
        Log.d(TAG, "type: " + data.getType() + ", priority: " + Integer.toString(data.getPriority()));

        PulseHandler.addEventListener(this);

        alarm = new Alarm();
        alarm.startCountdown();
    }

    private void cancelAlarm() {
        alarm.cancelAlarm();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PulseHandler.removeEventListener(this);
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
    public void onChange(float[] values) {

    }
}
