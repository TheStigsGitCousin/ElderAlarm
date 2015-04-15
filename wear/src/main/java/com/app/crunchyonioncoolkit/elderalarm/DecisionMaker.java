package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Intent;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

    private static SlidingWindow window = new SlidingWindow();

    public static void accelerationChange(float[] acceleration) {
        double SMV = Math.sqrt((acceleration[0] * acceleration[0]) + (acceleration[1] * acceleration[1]) + (acceleration[2] * acceleration[2]));
        window.newValue(SMV);
        fallDetection();
    }

    public static void pulseChange(float heartRate) {

    }

    private static void fallDetection() {
        int resualt = allTests(window.getArray());
        if(resualt > Constants.RESUALT_TRESHOLD){
            Intent intent = new Intent(MainActivity.currentContext, AlarmActivity.class);
            MainActivity.currentContext.startActivity(intent);
        }
    }


    private static int allTests(double[] sample) {
        int peakTime = Algorithms.peakTime(sample);
        if (peakTime == -1)
            return 0;
        int sum = 0;
        // impact end
        int impactEnd = Algorithms.impactEnd(sample, peakTime);
        // impact start
        int impactStart = Algorithms.impactStart(sample, impactEnd, peakTime);
        // MPI
        sum += Algorithms.MaximumPeakIndex(sample, impactStart, impactEnd) ? Constants.MPI_SCORE : 0;
        // AAMV
        sum += Algorithms.AAMV(sample, impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        if(sum == 0){
            return 0;
        }
        //MVI
        sum += Algorithms.MinimumValleyIndex(sample, impactStart, impactEnd) ? Constants.MVI_SCORE : 0;
        //PDI
        sum += Algorithms.PeakDurationIndex(sample, peakTime) ? Constants.PDI_SCORE : 0;
        //ARI
        sum += Algorithms.ActivityRatioIndex(sample, impactStart, impactEnd) ? Constants.ARI_SCORE : 0;
        //FFI
        sum += Algorithms.FreeFallIndex(sample, peakTime) ? Constants.FFI_SCORE : 0;
        return sum;
    }
}
