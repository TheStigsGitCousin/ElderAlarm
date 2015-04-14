package com.app.crunchyonioncoolkit.elderalarm;

import android.os.Handler;
import android.util.Log;

/**
 * Created by David on 2015-04-13.
 */
public class Alarm {

    private final String TAG="Alarm";

    Handler mHandler;

    public void detectedFall(){
        startCancelationProcedure();
    }

    public void detectedCardiacArrest(){
        startCancelationProcedure();
    }

    public void cancelAlarm(){
        startCancelationProcedure();
    }

    public void startCountdown(){
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 10000);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            Log.e("Handlers", "Calls");
            /** Do something **/
            Log.d(TAG, "ALARM ACTIVATED");
        }
    };

    private void startCancelationProcedure(){
        mHandler.removeCallbacks(mRunnable);
        Log.d(TAG, "ALARM CANCELED");
    }
}
