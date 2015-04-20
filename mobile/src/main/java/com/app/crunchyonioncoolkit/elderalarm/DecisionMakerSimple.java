package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

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

    public static boolean detectFall(){
        double [] SMV = window.getValueArray();
        Calendar[] Date = window.getTimeStampArray();
        int sum = 0;
        sum += AlgorithmsSimple.MPI(SMV)? Constants.MPI_SIMPLE_SCORE:0;
        sum += AlgorithmsSimple.MGI(SMV)? Constants.MGI_SIMPLE_SCORE:0;
        // HeartRate

        //Print out data if threshold reached
        if(sum >= Constants.FALL_SCORE_RESUALT_THRESHOLD ){
            DataOut.simpleTestPrint(SMV, Date,"Simpel_Test_Data.txt");
        }
        return sum >= Constants.FALL_SCORE_RESUALT_THRESHOLD;
        }
    }


