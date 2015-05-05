package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by David on 2015-04-14.
 */
public class Algorithms {

    private static final String TAG = "Algorithms";

    private static int peakTimeIndex;
    private static int impactStartIndex;
    private static int impactEndIndex;
    private static int FFIEndIndex;

    public static Calendar peakTime(double[] samples, Calendar[] timeSamples) {
        Calendar peakTime = timeSamples[timeSamples.length - 1];
        for (int i = 0; i < samples.length; i++) {
            if (samples[i] > Constants.PEAK_TIME_THRESHOLD) {
                peakTime = timeSamples[i];
                peakTimeIndex = i;
            }
        }

        return (timeSamples[timeSamples.length - 1].getTimeInMillis() - peakTime.getTimeInMillis()) >= Constants.TIME_WITHOUT_PEAKS ? peakTime : null;
    }

    // Calculate last significant impact with the ground
    public static Calendar impactEnd(double[] samples, Calendar[] timeArray, Calendar peakTime) {
        Calendar afterImpact = null;
        Calendar startPoint = Calendar.getInstance();
        startPoint.setTimeInMillis(peakTime.getTimeInMillis() + Constants.IMPACT_START_INTERVAL);


        if (startPoint.getTimeInMillis() > timeArray[timeArray.length - 1].getTimeInMillis())
            return null;


        for (int i = peakTimeIndex; i < samples.length && timeArray[i].getTimeInMillis() < startPoint.getTimeInMillis(); i++) {
            if (samples[i] >= Constants.IMPACT_END_MAGNITUDE_THRESHOLD) {
                afterImpact = timeArray[i];
                impactEndIndex = i;
            }
        }
        if(afterImpact!=null)
        Log.d(TAG, "Impact end: " + Long.toString(afterImpact.getTimeInMillis()));
        return afterImpact;
    }

    public static Calendar impactStart(double[] samples, Calendar impactEnd, Calendar[] timeArray, Calendar peakTime) {
        Calendar afterImpact = null;

        Calendar startPoint = Calendar.getInstance();
        startPoint.setTimeInMillis(impactEnd.getTimeInMillis() - Constants.IMPACT_START_INTERVAL);


        //Log.d(TAG, "PeakTime: " + Long.toString(peakTime.getTimeInMillis()));
        if (startPoint.getTimeInMillis() < timeArray[0].getTimeInMillis()) {
            Log.d(TAG, "startpoint < timeA");
            return null;
        }

        for (int i = 0; i < samples.length; i++) {
            if (timeArray[i].getTimeInMillis() > startPoint.getTimeInMillis() && timeArray[i].getTimeInMillis() <= peakTime.getTimeInMillis()){
                //Log.d(TAG, "kom in lager 1");
                if(samples[i] >= Constants.IMPACT_END_MAGNITUDE_THRESHOLD && samples[i + 1] <= Constants.IMPACT_START_LOW_THRESHOLD) {

                afterImpact = timeArray[i];
                impactStartIndex = i;
                //Log.d(TAG, "impactStart Found");
                break;
            }
            }
        }
        if(afterImpact!=null)
        Log.d(TAG, "Impact start: " + Long.toString(afterImpact.getTimeInMillis()));

        return afterImpact;
    }

    public static boolean AAMV(double[] samples, Calendar[] timeArray, Calendar impactStart, Calendar impactEnd) {
        int middle = impactStartIndex + ((impactEndIndex - impactStartIndex) / 2);
        double sum = 0;


        for (int i = middle; timeArray[i].getTimeInMillis() > timeArray[middle].getTimeInMillis() - (Constants.WIN_INTERVAL / 2); i--) {
            sum += (Math.abs(samples[i - 1] - samples[i]) / Constants.WIN_INTERVAL);
        }
        for (int i = middle; timeArray[i].getTimeInMillis() < timeArray[middle].getTimeInMillis() + (Constants.WIN_INTERVAL / 2); i++) {
            sum += (Math.abs(samples[i + 1] - samples[i]) / Constants.WIN_INTERVAL);
        }
        Log.d(TAG, "AAMV: " + Double.toString(sum));

        return (sum > Constants.AAMV_THRESHOLD);

    }

    public static boolean ImpactDurationIndex(Calendar impactStart, Calendar impactEnd) {
        return ((impactEnd.getTimeInMillis() - impactStart.getTimeInMillis()) > Constants.IDI_THRESHOLD);
    }

    public static boolean MaximumPeakIndex(double[] samples) {
        double maxAcceleration = samples[impactStartIndex];
        for (int i = impactStartIndex + 1; i <= impactEndIndex; i++) {
            if (samples[i] > maxAcceleration)
                maxAcceleration = samples[i];
        }
        Log.d(TAG, "MPI: " + Double.toString(maxAcceleration));

        return (maxAcceleration >= Constants.MAXIMUM_PEAK_THRESHOLD);

    }

    public static boolean MinimumValleyIndex(double[] samples, Calendar[] timeArray, Calendar impactStart, Calendar impactEnd) {
        double minAcceleration = samples[impactEndIndex];
        for (int i = impactEndIndex; timeArray[i].getTimeInMillis() > impactStart.getTimeInMillis() - Constants.MVI_Interval; i--) {
            if (samples[i] < minAcceleration)
                minAcceleration = samples[i];
        }
        Log.d(TAG, "MVI: " + Double.toString(minAcceleration));

        return (minAcceleration >= Constants.MVI_AVERAGE_MAGNITUDE_LOW && minAcceleration <= Constants.MVI_AVERAGE_MAGNITUDE_HIGH);
    }

    public static boolean PeakDurationIndex(double[] samples, Calendar[] timeArray) {
        long PDI = timeArray[PDIEnd(samples, peakTimeIndex)].getTimeInMillis() - timeArray[PDIStart(samples, peakTimeIndex)].getTimeInMillis();
        Log.d(TAG, "PDI: " + Long.toString(PDI));
        return (PDI < Constants.PDI_INTERVAL);
    }

    public static boolean ActivityRatioIndex(double[] samples, Calendar[] timeArray) {
        int middle = impactStartIndex + ((impactEndIndex - impactStartIndex) / 2);
        double count = 0;
        for (int i = middle; timeArray[i].getTimeInMillis() > timeArray[middle].getTimeInMillis() - (Constants.ARI_INTERVAL / 2); i--) {
            if (samples[i] < Constants.ARI_LOW || samples[i] > Constants.ARI_HIGH)
                count++;
        }
        for (int i = middle; timeArray[i].getTimeInMillis() < timeArray[middle].getTimeInMillis() + (Constants.ARI_INTERVAL / 2); i++) {
            if (samples[i] < Constants.ARI_LOW || samples[i] > Constants.ARI_HIGH)
                count++;
        }

        Log.d(TAG, "ARI: " + Double.toString(((count / Constants.ARI_INTERVAL))));
        return ((count / Constants.ARI_INTERVAL) > Constants.ARI_THRESHOLD);
    }

    public static boolean FreeFallIndex(double[] samples, Calendar[] timeArray, Calendar peakTime) {
        Calendar end = FFIEnd(samples, timeArray, peakTime);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(end.getTimeInMillis() - Constants.FFI_INTERVAL);
        double average = average(samples, timeArray, start);
        Log.d(TAG, "FFI: " + Double.toString(average));
        return (average >= Constants.FFI_MINIMUM_FALL_THRESHOLD);
    }

    private static double average(double[] samples, Calendar[] timeArray, Calendar start) {
        double sum = 0;
        int num = 0;
        for (int i = FFIEndIndex; timeArray[i].getTimeInMillis() > start.getTimeInMillis(); i--) {
            sum += samples[i];
            num++;
        }

        return (sum / num);
    }

    private static Calendar FFIEnd(double[] samples, Calendar[] timeArray, Calendar peakTime) {
        Calendar afterImpact = Calendar.getInstance();
        afterImpact.setTimeInMillis(peakTime.getTimeInMillis() - Constants.FFI_INTERVAL);

        for (int i = peakTimeIndex; timeArray[i].getTimeInMillis() >= afterImpact.getTimeInMillis() - Constants.FFI_INTERVAL; i--) {
            if (samples[i] <= Constants.FFI_THRESHOLD) {
                afterImpact = timeArray[i];
                FFIEndIndex = i;
            }
        }

        return afterImpact;
    }

    private static int PDIStart(double[] samples, int peakTime) {
        int samplesAfterImpact = -1;

        for (int i = peakTime; i >= 0; i--) {
            if (samples[i] <= Constants.PDI_THRESHOLD) {
                samplesAfterImpact = i;
                break;
            }
        }

        return samplesAfterImpact;
    }

    private static int PDIEnd(double[] samples, int peakTime) {
        int samplesAfterImpact = -1;

        for (int i = peakTime; i <= samples.length; i++) {
            if (samples[i] <= Constants.PDI_THRESHOLD) {
                samplesAfterImpact = i;
                break;
            }
        }

        return samplesAfterImpact;
    }
}
