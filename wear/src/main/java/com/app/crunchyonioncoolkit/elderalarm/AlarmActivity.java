package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// Handles alarms
public class AlarmActivity extends Activity implements Event {

    private final String TAG = "AlarmActivity";
    private String SETTINGS = "settings";
    private String IS_ACTIVE = "alarmisactive";

    private Alarm alarm;
    private Date lastPulseUpdate;
    private Bluetooth bluetooth;

    private Button cancelButton;
    private RelativeLayout pulseLayout;
    private TextView pulseTextView;

    private final int PULSE_UPDATE_FREQUENCY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        bluetooth = new Bluetooth(this);
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
        pulseTextView = (TextView) findViewById(R.id.pulse_textView);

        Button gobackButton = (Button) findViewById(R.id.gobackButton);
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

        bluetooth.startBluetooth();

        Log.d(TAG,"onCreate");

    }

    void setStartState(){

    }
    boolean isBackgroundActive(){
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        return settings.getBoolean(IS_ACTIVE, false);
    }

    void setBackgroundActive(boolean isActive){
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_ACTIVE, isActive);

        // Commit the edits!
        editor.commit();
    }
    // Navigate to MainActivity
    private void goToMainActivity() {
        // Disconnect potential bluetooth connection
        bluetooth.stopBluetooth();
        // Cancel pulse updates
        PulseHandler.listener.removeEventListener(this);

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
        PulseHandler.listener.removeEventListener(this);
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
                pulseTextView.setText(Float.toString(((float[]) values.value)[0]));
            }

        } else if (values.type.equals(Alarm.ALARM_EVENT)) {
            // If alarm state changed to 'Active'
            Log.d(TAG, "alarm activated");
            // Register for pulse updates
            PulseHandler.listener.addEventListener(this);
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
