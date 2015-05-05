package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

    public static void fallDetection() {
        int resualt = allTests();
        if (resualt > Constants.RESUALT_THRESHOLD) {
            Log.d("DecisionMaker", "FALL DETECTED");

        }
    }


    private static int allTests() {
        double[] samples = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();
        Calendar peakTime = Algorithms.peakTime(samples, timeArray);
        Log.d("DecisionMaker", " PeakTime: " + (peakTime == null ? "null" : Long.toString(peakTime.getTimeInMillis())));
        if (peakTime == null) {
            return 0;
        }

        int sum = 0;
        // impact end
        Calendar impactEnd = Algorithms.impactEnd(samples, timeArray, peakTime);
        // impact start
        Calendar impactStart = Algorithms.impactStart(samples, impactEnd, timeArray, peakTime);
        //Log.d("DecisionMaker", "ImpactStart: " + (impactStart == null ? "null" : Long.toString(impactStart.getTimeInMillis())) + "   ImpactEnd: " + (impactEnd == null ? "null" : Long.toString(impactEnd.getTimeInMillis())));
        if (impactEnd == null || impactStart == null) {
            return 0;
        }
        // AAMV
        sum += Algorithms.AAMV(samples, timeArray, impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        Log.d("DecisionMaker", "AAMV sum: " + Integer.toString(sum));
        // IDI
        sum += Algorithms.ImpactDurationIndex(impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        Log.d("DecisionMaker", "AAMV sum: " + Integer.toString(sum));
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
