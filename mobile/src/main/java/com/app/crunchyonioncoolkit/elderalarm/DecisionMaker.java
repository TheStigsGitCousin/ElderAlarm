package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

    private static SlidingWindow window = new SlidingWindow();

    public static void accelerationChange(float[] acceleration) {
        double SMV = Math.sqrt((acceleration[0] * acceleration[0]) + (acceleration[1] * acceleration[1]) + (acceleration[2] * acceleration[2]));
        Log.d("DecisionMaker", "New acceleration: "+ Double.toString(SMV));
        window.newValue(SMV);
        fallDetection();
    }

    public static void pulseChange(float heartRate) {

    }

    private static void fallDetection() {
        int resualt = allTests(window.getArray());
        if(resualt > Constants.RESUALT_THRESHOLD){
            Log.d("DecisionMaker", "FALL DETECTED");
        }
    }


    private static int allTests(double[] sample) {
        int peakTime = Algorithms.peakTime(sample);
        Log.d("DecisionMaker", " PeakTime: " + Integer.toString(peakTime));
        if (peakTime == -1)
            return 0;
        if (peakTime < Constants.WINDOW_WIDTH/6){
            for(int i = 0; i < sample.length; i++){
                DataOut.writeToFile(Double.toString(sample[i]), "SMV_Data.txt");
            }
            DataOut.writeToFile("1000", "SMV_Data.txt");
        }
        int sum = 0;
        // impact end
        int impactEnd = Algorithms.impactEnd(sample, peakTime);
        // impact start
        int impactStart = Algorithms.impactStart(sample, impactEnd, peakTime);
        Log.d("DecisionMaker","ImpactStart: " + Integer.toString(impactStart) + "   ImpactEnd: " + Integer.toString(impactEnd));
        if(impactEnd < 0 || impactStart < 0){
            return 0;
        }
        // MPI
        sum += Algorithms.MaximumPeakIndex(sample, impactStart, impactEnd) ? Constants.MPI_SCORE : 0;
        Log.d("DecisionMaker", "MPI sum: "+Integer.toString(sum));
        // AAMV
        sum += Algorithms.AAMV(sample, impactStart, impactEnd) ? Constants.AAMV_SCORE : 0;
        Log.d("DecisionMaker", "AAMV sum: "+Integer.toString(sum));
        if(sum == 0){
            return 0;
        }
        //MVI
        sum += Algorithms.MinimumValleyIndex(sample, impactStart, impactEnd) ? Constants.MVI_SCORE : 0;
        Log.d("DecisionMaker", "MVI sum: "+Integer.toString(sum));
        //PDI
        sum += Algorithms.PeakDurationIndex(sample, peakTime) ? Constants.PDI_SCORE : 0;
        Log.d("DecisionMaker", "PDI sum: "+Integer.toString(sum));
        //ARI
        sum += Algorithms.ActivityRatioIndex(sample, impactStart, impactEnd) ? Constants.ARI_SCORE : 0;
        Log.d("DecisionMaker", "ARI sum: "+Integer.toString(sum));
        //FFI
        sum += Algorithms.FreeFallIndex(sample, peakTime) ? Constants.FFI_SCORE : 0;
        Log.d("DecisionMaker", "FFI sum: "+Integer.toString(sum));
        return sum;
    }
}
