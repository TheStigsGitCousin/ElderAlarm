package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class AlarmActivity extends Activity implements Event {

    private final String TAG = "AlarmActivity";

    Alarm alarm;

    Button cancelButton;
    RelativeLayout pulseLayout;
    TextView pulseTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        cancelButton.setVisibility(View.VISIBLE);
        pulseLayout.setVisibility(View.GONE);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                cancelAlarm();
                return false;
            }
        });

        pulseLayout = (RelativeLayout) findViewById(R.id.pulse_layout);
        pulseTextview = (TextView) findViewById(R.id.pulse_textView);

        Intent intent = getIntent();
        MyParcelable data = intent.getExtras().getParcelable("message");
        Log.d(TAG, "type: " + data.getType() + ", priority: " + Integer.toString(data.getPriority()));


        Alarm.addEventListener(this);

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
    public void onChange(Result values) {
        if (values.type.equals(PulseHandler.PULSE_EVENT)) {
            Log.d(TAG, "pulse changed");
            pulseTextview.setText(Float.toString(((float[]) values.value)[0]));

        } else if (values.type.equals(Alarm.ALARM_EVENT)) {
            Log.d(TAG, "alarm activated");
            PulseHandler.addEventListener(this);
            cancelButton.setVisibility(View.GONE);
            pulseLayout.setVisibility(View.VISIBLE);
        }
    }
}
