package com.app.crunchyonioncoolkit.elderalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;
import java.util.List;

public class MainActivity extends Activity {

    private String TAG = "WEAR";
    public static Context currentContext;
    //private TextView mTextView;
    // Accelerometer
    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private AccelerometerHandler accelerometerHandler;
    private PulseHandler pulseHandler;
    private Sensor pulseSensor;

    private static final long CONNECTION_TIME_OUT_MS = 100;
    private static final String MESSAGE = "Hello Wear!";
    private GoogleApiClient client;
    private String nodeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("WEAR_CRUNCHY", "ON CREATE");
        initApi();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //mTextView = (TextView) stub.findViewById(R.id.text);
                setupWidgets();
            }
        });

        initializeSensors();

        Intent intent = new Intent(this, AlarmActivity.class);
        MyParcelable data = new MyParcelable(10, "cardiac arrest");
        intent.putExtra("message", data);
        startActivity(intent);
    }

    /**
     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
     */
    private void initApi() {
        Log.d("WEAR_CRUNCHY", "INITAPI");
        client = getGoogleApiClient(this);
        Log.d("WEAR_CRUNCHY", "DEVICENODE");
        retrieveDeviceNode();
    }

    /**
     * Sets up the button for handling click events.
     */
    private void setupWidgets() {
        findViewById(R.id.btn_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToast();
            }
        });
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
                Log.d("WEAR_CRUNCHY", "RETRIEVE NODES");
                client.connect();
                Log.d("WEAR_CRUNCHY", "1");
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                Log.d("WEAR_CRUNCHY", "2");
                List<Node> nodes = result.getNodes();
                Log.d("WEAR_CRUNCHY", "3");
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                    Log.d("WEAR_CRUNCHY", "A NODE " + nodeId);

                } else {
                    Log.d("WEAR_CRUNCHY", "NO NODES");
                }
                Log.d("WEAR_CRUNCHY", "4");
            }
        }).start();
    }

    /**
     * Sends a message to the connected mobile device, telling it to show a Toast.
     */
    private void sendToast() {
        Log.d("WEAR_CRUNCHY", "SEND TOAST");
        Log.d("WEAR_CRUNCHY", "NODE ID" + nodeId);
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, MESSAGE, new byte[0]).setResultCallback(
                            new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                    if (!sendMessageResult.getStatus().isSuccess()) {
                                        Log.e("WEAR_CRUNCHY", "Failed to send message with status code: "
                                                + sendMessageResult.getStatus().getStatusCode());
                                    }
                                }
                            }
                    );
                    client.disconnect();
                    Log.d("WEAR_CRUNCHY", "SENT MESSAGE");
                }
            }).start();
        }
    }


    void initializeSensors() {

        // Accelerometer
        accelerometerHandler = new AccelerometerHandler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mSensorManager.registerListener(accelerometerHandler, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Pulse
        pulseSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(pulseHandler, pulseSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
