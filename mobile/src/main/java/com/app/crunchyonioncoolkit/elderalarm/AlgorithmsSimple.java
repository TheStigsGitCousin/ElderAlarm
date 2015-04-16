package com.app.crunchyonioncoolkit.elderalarm;

/**
 * Created by Jack on 2015-04-16.
 */
public class AlgorithmsSimple {

    public static boolean MPI(double[] samples){
        double CurrentPeak = 0;
        for(int i = 0; i<samples.length; i++){
            if(samples[i]>Constants.MAXIMUM_PEAK_THRESHOLD && samples[i]>CurrentPeak){
                CurrentPeak = samples[i];
            }
        }
        return CurrentPeak>0;
    }

    // Maximum Gyroscope Index
    public static boolean MGI(double [] samples){
        double CurrentPeak = 0;
        for(int i = 0; i<samples.length; i++){
            if(samples[i]>Constants.MAXIMUM_GYROSCOPE_PEAK_THRESHOLD && samples[i]>CurrentPeak){
                CurrentPeak = samples[i];
            }
        }
        return CurrentPeak>0;
    }

    }


