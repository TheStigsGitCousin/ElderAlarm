package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by David on 2015-04-08.
 */
public class ListenerService extends WearableListenerService {

    GoogleApiClient mGoogleApiClient;
    String TAG = "ListenerService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SERVICE STARTED");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        Log.d(TAG, "CONNECTED");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "MESSAGE RECEIVED");
        String path = messageEvent.getPath();
        Log.d(TAG, "MESSAGE: " + path);
        if (path == "GPS") {
            Position.startLocationTracking(this);
        } else if (path == "") {

        }
        showToast(path);
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d("MOBILE_CRUNCHY", "Connected peer name & ID: " + name + "|" + id);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}