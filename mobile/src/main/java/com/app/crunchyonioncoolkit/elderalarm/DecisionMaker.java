package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

    public static void fallDetection() {
        int testSum = allTests();
        if (testSum >= Constants.RESUALT_THRESHOLD) {
            Log.d("DecisionMaker", "FALL DETECTED");
            AccelerometerHandler.window = new SlidingWindow();

        }
    }


    private static int allTests() {
        double[] samples = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();
        Calendar peakTime = Algorithms.peakTime(samples, timeArray);
        double[] samplesGYR = GyroscopeHandler.window.getValueArray();
        Calendar[] timeArrayGYR = GyroscopeHandler.window.getTimeStampArray();


        if (peakTime == null) {
            //Log.d("DecisionMaker", "Inget fall");
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
        Log.d("DecisionMaker", "IDI: " + Long.toString(impactEnd.getTimeInMillis()-impactStart.getTimeInMillis()));
        // AAMV
        sum += Algorithms.AAMV(samples, timeArray, impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        Log.d("DecisionMaker", "AAMV sum: " + Integer.toString(sum));
        // IDI
        sum += Algorithms.ImpactDurationIndex(impactStart, impactEnd) ? Constants.IDI_SCORE : 0;
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

        double area = Algorithms.GyroArea(samplesGYR, timeArrayGYR, peakTime);
        Log.d("DecisionMaker", "Gyro Area: " + Double.toString(area));
        return sum;
    }
}
