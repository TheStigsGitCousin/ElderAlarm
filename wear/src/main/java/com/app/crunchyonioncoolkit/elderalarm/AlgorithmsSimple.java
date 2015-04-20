package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Jack on 2015-04-16.
 */
public class AlgorithmsSimple {

    private final static String TAG = Algorithms.class.getClass().getSimpleName();

    public static Calendar peakTime(double[] samples, Calendar[] timeSamples) {
        Calendar peakTime = timeSamples[timeSamples.length - 1];
        for (int i = 0; i < samples.length; i++) {
            if (samples[i] > Constants.PEAK_TIME_THRESHOLD) {
                peakTime = timeSamples[i];
            }
        }

        return (timeSamples[timeSamples.length - 1].getTimeInMillis() - peakTime.getTimeInMillis()) >= Constants.TIME_WITHOUT_PEAKS ? peakTime : null;
    }

    public static boolean MPI(double[] samples) {
        double CurrentPeak = 0;
        for (int i = 0; i < samples.length; i++) {
            if (samples[i] > Constants.MAXIMUM_PEAK_THRESHOLD && samples[i] > CurrentPeak) {
                CurrentPeak = samples[i];
            }
        }
        return CurrentPeak > 0;
    }

    // Maximum Gyroscope Index
    public static boolean MGI(double[] samples) {
        double CurrentPeak = 0;
        for (int i = 0; i < samples.length; i++) {
            if (samples[i] > Constants.MAXIMUM_GYROSCOPE_PEAK_THRESHOLD && samples[i] > CurrentPeak) {
                CurrentPeak = samples[i];
            }
        }
        return CurrentPeak > 0;
    }

    // Minimum Heart Rate Index
    public static boolean MHRI(double[] valueSamples, Calendar[] timeSamples, Calendar timeOfFall) {
        double num = 0;
        double averageBeforeFall = 0;
        int i = 0;
        for (i = 0; i < timeSamples.length; i++) {
            if (timeSamples[i].getTimeInMillis() < timeOfFall.getTimeInMillis()) {
                averageBeforeFall += valueSamples[i];
                num++;
            } else {
                break;
            }
        }

        averageBeforeFall = averageBeforeFall / num;
        double averageAfterFall = 0;
        for (; i < timeSamples.length; i++) {
            averageAfterFall += valueSamples[i];
        }

        averageAfterFall = averageAfterFall / (timeSamples.length - num);

        Log.d(TAG,"MHRI average: "+Double.toString(averageAfterFall / averageBeforeFall));

        return averageAfterFall / averageBeforeFall >= Constants.MINIMUM_PULSE_THRESHOLD ? true : false;
    }

}


