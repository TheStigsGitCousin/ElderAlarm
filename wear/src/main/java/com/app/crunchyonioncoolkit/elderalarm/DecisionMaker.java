package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

    private static final String TAG = "DecisionMaker";
    private static boolean isDetectionActive = true;

    public static void activateAlarm() {
        isDetectionActive = true;
    }

    public static void deactivateAlarm() {
        isDetectionActive = false;
    }

    public static void fallDetection() {
        if (isDetectionActive) {
            int testSum = runTests();
            if (testSum >= 30) {
                isDetectionActive = false;
                Intent intent = new Intent(BackgroundService.context, AlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyParcelable data = new MyParcelable(10, "cardiac arrest");
                intent.putExtra("message", data);
                BackgroundService.context.startActivity(intent);
                Log.d("DecisionMaker", "FALL DETECTED");

            }
        }
    }


    private static int runTests() {
        double[] samples = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();
        Calendar peakTime = Algorithms.peakTime(samples, timeArray);

        if (peakTime == null) {
            return 0;
        }
//        Log.d(TAG, " PeakTime: " + Long.toString(timeArray[timeArray.length - 1].getTimeInMillis() - peakTime.getTimeInMillis()) + " (" + Long.toString(peakTime.getTimeInMillis())+")");
//        Log.d(TAG, " PeakTime: " + Long.toString(peakTime.getTimeInMillis()));
        int sum = 0;
        // impact end
        Calendar impactEnd = Algorithms.impactEnd(samples, timeArray, peakTime);
        // impact start
        Calendar impactStart = Algorithms.impactStart(samples, impactEnd, timeArray, peakTime);

        if (impactEnd == null || impactStart == null) {
            return 0;
        }
//        Log.d(TAG, "ImpactStart: " + Long.toString(impactStart.getTimeInMillis()) + "   ImpactEnd: " + Long.toString(impactEnd.getTimeInMillis()));
        // AAMV
        sum += Algorithms.AAMV(samples, timeArray, impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
//        Log.d(TAG, "AAMV sum: " + Integer.toString(sum));
        // IDI
        sum += Algorithms.ImpactDurationIndex(impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
//        Log.d(TAG, "IDI sum: " + Integer.toString(sum));
        // MPI
        sum += Algorithms.MaximumPeakIndex(samples) ? Constants.MPI_SCORE : 0;
//        Log.d(TAG, "MPI sum: " + Integer.toString(sum));
        //MVI
        sum += Algorithms.MinimumValleyIndex(samples, timeArray, impactStart, impactEnd) ? Constants.MVI_SCORE : 0;
//        Log.d(TAG, "MVI sum: " + Integer.toString(sum));
        //PDI
        sum += Algorithms.PeakDurationIndex(samples, timeArray) ? Constants.PDI_SCORE : 0;
//        Log.d(TAG, "PDI sum: " + Integer.toString(sum));
        //ARI
        sum += Algorithms.ActivityRatioIndex(samples, timeArray) ? Constants.ARI_SCORE : 0;
//        Log.d(TAG, "ARI sum: " + Integer.toString(sum));
        //FFI
        sum += Algorithms.FreeFallIndex(samples, timeArray, peakTime) ? Constants.FFI_SCORE : 0;
//        Log.d(TAG, "FFI sum: " + Integer.toString(sum));
        return sum;
    }
}
