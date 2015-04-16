package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

/**
 * Created by Jack on 2015-04-16.
 */
public class DecisionMakerSimple {
    private static SlidingWindow window = new SlidingWindow();

    public static void accelerationChange(float[] acceleration) {
        double SMV = Math.sqrt((acceleration[0] * acceleration[0]) + (acceleration[1] * acceleration[1]) + (acceleration[2] * acceleration[2]));
        Log.d("DecisionMaker", "New acceleration: " + Double.toString(SMV));
        window.newValue(SMV);
        //fallDetection();
    }

    public static boolean detectFall(double [] sample){
        int sum = 0;
        sum += AlgorithmsSimple.MPI(sample)? Constants.MPI_SIMPLE_SCORE:0;
        sum += AlgorithmsSimple.MGI(sample)? Constants.MGI_SIMPLE_SCORE:0;
        // HeartRate
        return sum >= Constants.FALL_SCORE_RESUALT_THRESHOLD;
        }
    }


