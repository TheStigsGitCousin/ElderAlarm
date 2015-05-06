package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Handles alarms
public class AlarmActivity extends Activity implements Event {

    private final String TAG = "AlarmActivity";
    private String SETTINGS = "settings";
    private String IS_ACTIVE = "alarmisactive";

    private Alarm alarm;
    private Calendar lastPulseUpdate;
//    private Bluetooth bluetooth;

    private Button cancelButton;
    private RelativeLayout pulseLayout;
    private TextView pulseTextView;

    private final int PULSE_UPDATE_FREQUENCY = 500;

    private static final long CONNECTION_TIME_OUT_MS = 100;
    private static final String MESSAGE = "alarm";
    private GoogleApiClient mGoogleApiClient;
    private String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

//        bluetooth = new Bluetooth(this);
        lastPulseUpdate = Calendar.getInstance();

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

//        bluetooth.startBluetooth();

        initApi();

        Log.d(TAG, "onCreate");
    }

    void setStartState() {
        if (!isBackgroundActive()) {

        }
    }

    boolean isBackgroundActive() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
        return settings.getBoolean(IS_ACTIVE, false);
    }

    void setBackgroundActive(boolean isActive) {
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
//        bluetooth.stopBluetooth();
        // Activate fall detection again
        DecisionMaker.activateAlarm();
        // Cancel pulse updates
        PulseHandler.listener.removeEventListener(this);
        // Set the background active variable to false
        setBackgroundActive(false);

        // Navigate to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void cancelAlarm() {
        alarm.cancelAlarm();
        Vibration.stopVibration();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public void onChange(Result values) {

        if (values.type.equals(PulseHandler.PULSE_EVENT)) {
            // If pulse changed
            Calendar currentTime = Calendar.getInstance();
            if ((currentTime.getTimeInMillis() - lastPulseUpdate.getTimeInMillis()) > PULSE_UPDATE_FREQUENCY) {
                //Log.d(TAG, "pulse changed: " + Float.toString(((float[]) values.value)[0]));
                pulseTextView.setText(Float.toString(((float[]) values.value)[0]));
                lastPulseUpdate = currentTime;
            }

        } else if (values.type.equals(Alarm.ALARM_EVENT)) {
            // If alarm state changed to 'Active'
            Log.d(TAG, "alarm activated");
            sendAlarmMessage();
            // Register for pulse updates
            PulseHandler.listener.addEventListener(this);
            setBackgroundActive(true);
            // Hide the cancel button
            cancelButton.setVisibility(View.GONE);
            // Show the pulse display layout
            pulseLayout.setVisibility(View.VISIBLE);
            // Stop vibration
            Vibration.stopVibration();
        }
    }

    /**
     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
     */
    private void initApi() {
        mGoogleApiClient = getGoogleApiClient(this);
        retrieveDeviceNode();
    }


    /**
     * Returns a GoogleApiClient that can access the Wear API.
     *
     * @param context
     * @return A GoogleApiClient that can make calls to the Wear API
     */
    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.connect();
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                    Log.d(TAG, "NODE Id: " + nodeId);

                } else {
                    Log.d(TAG, "NO NODES FOUND");
                }
                Log.d(TAG, "4");
            }
        }).start();
    }

    /**
     * Sends a message to the connected mobile device, telling it to show a Toast.
     */
    private void sendAlarmMessage() {
        Log.d(TAG, "NODE Id" + nodeId);
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, MESSAGE, new byte[0]).setResultCallback(
                            new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                    if (!sendMessageResult.getStatus().isSuccess()) {
                                        Log.e(TAG, "Failed to send message with status code: "
                                                + sendMessageResult.getStatus().getStatusCode());
                                    }
                                }
                            }
                    );
                    mGoogleApiClient.disconnect();
                    Log.d(TAG, "MESSAGE SENT");
                }
            }).start();
        }
    }
}
