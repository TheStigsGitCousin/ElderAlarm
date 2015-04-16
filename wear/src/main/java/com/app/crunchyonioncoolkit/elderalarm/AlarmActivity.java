package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// Handles alarms
public class AlarmActivity extends Activity implements Event {

    private final String TAG = "AlarmActivity";

    private Alarm alarm;
    private Date lastPulseUpdate;

    private Button cancelButton;
    private Button gobackButton;
    private RelativeLayout pulseLayout;
    private TextView pulseTextview;

    private final int PULSE_UPDATE_FREQUENCY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        lastPulseUpdate = new Date();

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Cancel the alarm countdown
                cancelAlarm();
                goToMainActivity();
                // Return true if the callback consumed the long click
                return true;
            }
        });

        pulseLayout = (RelativeLayout) findViewById(R.id.pulse_layout);
        pulseTextview = (TextView) findViewById(R.id.pulse_textView);

        gobackButton = (Button) findViewById(R.id.gobackButton);
        gobackButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Go to MainActivity when a long click is performed on the heart rate display
                goToMainActivity();
                // Return true if the callback consumed the long click
                return true;
            }
        });

        // Show cancel button
        cancelButton.setVisibility(View.VISIBLE);
        // Hide pulse display layout
        pulseLayout.setVisibility(View.GONE);

        // Get message sent to this activity, contain type of emergency and a priority
        Intent intent = getIntent();
        // Parse message to MyParcable
        MyParcelable data = intent.getExtras().getParcelable("message");
        Log.d(TAG, "type: " + data.getType() + ", priority: " + Integer.toString(data.getPriority()));

        // Add alarm update, fires when alarm countdown reaches 0
        Alarm.addEventListener(this);
        // Create new alarm object
        alarm = new Alarm();
        // Start alarm countdown
        alarm.startCountdown();
        // Start vibration
        Vibration.startVibration(this);

    }

    private void goToMainActivity() {
        // Cancel pulse updates
        PulseHandler.removeEventListener(this);

        // Navigate to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        // Remove listeners
        PulseHandler.removeEventListener(this);
        Alarm.removeEventListener(this);
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
            Date currentTime = new Date();
            if (getDateDiff(lastPulseUpdate, currentTime) > PULSE_UPDATE_FREQUENCY) {
                Log.d(TAG, "pulse changed: " + Float.toString(((float[]) values.value)[0]));
                pulseTextview.setText(Float.toString(((float[]) values.value)[0]));
            }

        } else if (values.type.equals(Alarm.ALARM_EVENT)) {
            // If alarm state changed to 'Active'
            Log.d(TAG, "alarm activated");
            // Register for pulse updates
            PulseHandler.addEventListener(this);
            // Hide the cancel button
            cancelButton.setVisibility(View.GONE);
            // Show the pulse display layout
            pulseLayout.setVisibility(View.VISIBLE);
            // Stop vibration
            Vibration.stopVibration();
        }
    }

    public static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.MILLISECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
