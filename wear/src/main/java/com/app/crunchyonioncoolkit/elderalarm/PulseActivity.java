package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


public class PulseActivity extends Activity implements Event {

    private final String TAG = "PulseActivity";
    private final int PULSE_UPDATE_FREQUENCY = 500;
    private Calendar lastPulseUpdate;
    TextView pulseTextView;
    Button button;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);

        button = (Button) findViewById(R.id.pulse_goback_button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                goBack();
                return true;
            }
        });

        lastPulseUpdate=Calendar.getInstance();

        pulseTextView = (TextView) findViewById(R.id.pulse_display_textView);

        serviceIntent = new Intent(this, BackgroundService.class);

        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PulseHandler.listener.addEventListener(this);
        startService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PulseHandler.listener.removeEventListener(this);
        stopService(serviceIntent);

    }

    void goBack() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pulse, menu);
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
            Calendar currentTime = Calendar.getInstance();
            if ((currentTime.getTimeInMillis() - lastPulseUpdate.getTimeInMillis()) > PULSE_UPDATE_FREQUENCY) {
                StringBuilder stringBuilder = new StringBuilder();
                for(float f:((float[]) values.value)){
                    stringBuilder.append(f).append(" | ");
                }
                Log.d(TAG, "pulse changed: " + stringBuilder.toString());
                pulseTextView.setText(stringBuilder.toString());
                lastPulseUpdate = currentTime;
            }
        }
    }
}
