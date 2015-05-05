package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

//    private static SlidingWindow window = new SlidingWindow();

//    public static void accelerationChange(float[] acceleration) {
//        double SMV = Math.sqrt((acceleration[0] * acceleration[0]) + (acceleration[1] * acceleration[1]) + (acceleration[2] * acceleration[2]));
//        Log.d("DecisionMaker", "New acceleration: " + Double.toString(SMV));
//        window.newValue(SMV);
//        fallDetection();
//    }

//    public static void pulseChange(float heartRate) {
//
//    }

    private static boolean isDetectionActive = true;

    public static void activateAlarm() {
        isDetectionActive = true;
    }

    public static void deactivateAlarm() {
        isDetectionActive = false;
    }

    public static void fallDetection() {
        int testSum = allTests();
        if (testSum >= Constants.RESUALT_THRESHOLD) {
            BackgroundService.context.stopService(MainActivity.serviceIntent);
            Intent intent = new Intent(BackgroundService.context, AlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyParcelable data = new MyParcelable(10, "cardiac arrest");
            intent.putExtra("message", data);
            BackgroundService.context.startActivity(intent);
            Log.d("DecisionMaker", "FALL DETECTED");


        if (isDetectionActive) {
            int resualt = allTests(AccelerometerHandler.window.getValueArray());
            if (resualt > Constants.RESUALT_THRESHOLD) {
                Log.d("DecisionMaker", "FALL DETECTED");
            }
        }
    }


    private static int allTests() {
        double[] samples = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();
        Calendar peakTime = Algorithms.peakTime(samples, timeArray);

        if (peakTime == null) {
            Log.d("DecisionMaker", "Inget fall");
            return 0;
        }
        Log.d("DecisionMaker", " PeakTime: " + Long.toString(peakTime.getTimeInMillis()));
        int sum = 0;
        // impact end
        Calendar impactEnd = Algorithms.impactEnd(samples, timeArray, peakTime);
        // impact start
        Calendar impactStart = Algorithms.impactStart(samples, impactEnd, timeArray, peakTime);

        if (impactEnd == null || impactStart == null) {
            return 0;
        }
        Log.d("DecisionMaker", "ImpactStart: " + Long.toString(impactStart.getTimeInMillis()) + "   ImpactEnd: " + Long.toString(impactEnd.getTimeInMillis()));
        // AAMV
        sum += Algorithms.AAMV(samples, timeArray, impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        Log.d("DecisionMaker", "AAMV sum: " + Integer.toString(sum));
        // IDI
        sum += Algorithms.ImpactDurationIndex(impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        Log.d("DecisionMaker", "IDI sum: " + Integer.toString(sum));
        // MPI
        sum += Algorithms.MaximumPeakIndex(samples) ? Constants.MPI_SCORE : 0;
        Log.d("DecisionMaker", "MPI sum: " + Integer.toString(sum));
        //MVI
        sum += Algorithms.MinimumValleyIndex(samples, timeArray, impactStart, impactEnd) ? Constants.MVI_SCORE : 0;
        Log.d("DecisionMaker", "MVI sum: " + Integer.toString(sum));
        //PDI
        sum += Algorithms.PeakDurationIndex(samples, timeArray) ? Constants.PDI_SCORE : 0;
        Log.d("DecisionMaker", "PDI sum: " + Integer.toString(sum));
        //ARI
        sum += Algorithms.ActivityRatioIndex(samples, timeArray) ? Constants.ARI_SCORE : 0;
        Log.d("DecisionMaker", "ARI sum: " + Integer.toString(sum));
        //FFI
        sum += Algorithms.FreeFallIndex(samples, timeArray, peakTime) ? Constants.FFI_SCORE : 0;
        Log.d("DecisionMaker", "FFI sum: " + Integer.toString(sum));
        return sum;
    }
}
