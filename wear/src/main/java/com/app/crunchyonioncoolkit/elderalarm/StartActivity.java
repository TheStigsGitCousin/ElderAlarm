package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class StartActivity extends Activity {
    private String TAG = "StartActivity";
    private String SETTINGS = "settings";
    private String IS_ACTIVE = "isactive";

    public static Context currentContext;
    public static Activity currentActivity;
    private Intent serviceIntent;

    private static final long CONNECTION_TIME_OUT_MS = 100;
    private static final String MESSAGE = "Hello Wear!";
    private GoogleApiClient mGoogleApiClient;
    private String nodeId;

    private Button powerButton;
    private static final String DATA_KEY = "com.example.key.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        currentContext = this;
        currentActivity = this;

        initApi();
        powerButton = (Button) findViewById(R.id.powerButton);
        powerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchPowerState();
                return true;
            }
        });

        setPowerState();

        serviceIntent = new Intent(this, BackgroundService.class);
        serviceIntent.putExtra("writeToFile", false);

    }

    private void setPowerState() {
        if (isBackgroundActive()) {
            powerButton.setText(getString(R.string.turn_off_button_text));
        } else {
            powerButton.setText(getString(R.string.turn_on_button_text));
        }
    }

    private void switchPowerState() {
        if (powerButton.getText().toString().equals(getString(R.string.turn_on_button_text))) {
            powerButton.setText(getString(R.string.turn_off_button_text));
            powerOn();
            setBackgroundActive(true);
        } else if (powerButton.getText().equals(getString(R.string.turn_off_button_text))) {
            powerButton.setText(getString(R.string.turn_on_button_text));
            powerOff();
            setBackgroundActive(false);
        }
    }

    // Create a data map and put data in it
    private void sendData(String path, String data) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(path);
        putDataMapReq.getDataMap().putString(DATA_KEY, data);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    void powerOn() {
        startService(serviceIntent);

    }

    void powerOff() {
        stopService(serviceIntent);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        GattServer.stopServer();
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
    private void sendToast() {
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
