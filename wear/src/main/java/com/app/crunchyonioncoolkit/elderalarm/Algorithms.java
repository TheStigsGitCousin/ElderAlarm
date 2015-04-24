package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by David on 2015-04-14.
 */
public class Algorithms {

    private static final String TAG = "DecisionMaker";

    private static int peakTimeIndex;
    private static Calendar impactEndIndex;

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
    public static int impactEnd(double[] samples, Calendar[] timeArray, Calendar peakTime) {
        int samplesAfterImpact = -1;
        Calendar startPoint = Calendar.getInstance();
        startPoint.setTime(peakTime.getTime());
        startPoint.add(Calendar.MILLISECOND, Constants.IMPACT_END_INTERVAL);

        if (startPoint.getTimeInMillis() > timeArray[timeArray.length - 1].getTimeInMillis())
            return -1;


        for (int i = peakTimeIndex; i < samples.length; i++) {
            if (samples[i] >= Constants.IMPACT_END_MAGNITUDE_THRESHOLD) {
                samplesAfterImpact = i;
                impactEndIndex=timeArray[i];
            }
        }

        Log.d(TAG, "Impact end: " + Integer.toString(samplesAfterImpact));
        return samplesAfterImpact;
    }

    public static int impactStart(double[] samples, int impactEnd, int peakTime) {
        int samplesAfterImpact = -1;
        int startPoint = impactEnd - Constants.IMPACT_START_INTERVAL;



        if (startPoint < 0)
            return -1;

        for (int i = startPoint; i <= peakTime; i++) {
            if (samples[i] >= Constants.IMPACT_END_MAGNITUDE_THRESHOLD && samples[i + 1] <= Constants.IMPACT_START_LOW_THRESHOLD) {
                samplesAfterImpact = i;
                break;
            }
        }

        Log.d(TAG, "Impact start: " + Integer.toString(samplesAfterImpact));

        return samplesAfterImpact;
    }

    public static boolean AAMV(double[] samples, int impactStart, int impactEnd) {
        int middle = impactStart + ((impactEnd - impactStart) / 2);
        double sum = 0;

        for (int i = middle - (Constants.WIN_INTERVAL / 2); i <= middle + (Constants.WIN_INTERVAL / 2); i++) {
            sum += (Math.abs(samples[i + 1] - samples[i]) / Constants.WIN_INTERVAL);
        }
        Log.d(TAG, "AAMV: " + Double.toString(sum));

        return (sum > Constants.AAMV_THRESHOLD);

    }

    public static boolean ImpactDurationIndex(int impactStart, int impactEnd) {
        return ((impactEnd - impactStart) > Constants.IDI_THRESHOLD);
    }

    public static boolean MaximumPeakIndex(double[] samples, int impactStart, int impactEnd) {
        double maxAcceleration = samples[impactStart];
        for (int i = impactStart + 1; i <= impactEnd; i++) {
            if (samples[i] > maxAcceleration)
                maxAcceleration = samples[i];
        }
        Log.d(TAG, "MPI: " + Double.toString(maxAcceleration));

        return (maxAcceleration >= Constants.MAXIMUM_PEAK_THRESHOLD);

    }

    public static boolean MinimumValleyIndex(double[] samples, int impactStart, int impactEnd) {
        double minAcceleration = samples[impactStart - Constants.MVI_Interval];
        for (int i = impactStart - Constants.MVI_Interval; i <= impactEnd; i++) {
            if (samples[i] < minAcceleration)
                minAcceleration = samples[i];
        }
        Log.d(TAG, "MVI: " + Double.toString(minAcceleration));

        return (minAcceleration >= Constants.MVI_AVERAGE_MAGNITUDE_LOW && minAcceleration <= Constants.MVI_AVERAGE_MAGNITUDE_HIGH);
    }

    public static boolean PeakDurationIndex(double[] samples, int peakTime) {
        int PDI = PDIEnd(samples, peakTime) - PDIStart(samples, peakTime);
        Log.d(TAG, "PDI: " + Integer.toString(PDI));
        return (PDI < Constants.PDI_MAGNITUDE);
    }

    public static boolean ActivityRatioIndex(double[] samples, int impactStart, int impactEnd) {
        int middle = impactStart + ((impactEnd - impactStart) / 2);
        double count = 0;
        for (int i = middle - (Constants.ARI_INTERVAL / 2); i <= middle + (Constants.ARI_INTERVAL / 2); i++) {
            if (samples[i] < Constants.ARI_LOW || samples[i] > Constants.ARI_HIGH)
                count++;
        }

        Log.d(TAG, "ARI: " + Double.toString(((count / Constants.ARI_INTERVAL))));
        return ((count / Constants.ARI_INTERVAL) > Constants.ARI_THRESHOLD);
    }

    public static boolean FreeFallIndex(double[] samples, int peakTime) {
        int end = FFIEnd(samples, peakTime);
        if (end == -1)
            return false;

        int start = end - Constants.FFI_INTERVAL;
        double average = average(samples, start, end);
        Log.d(TAG, "FFI: " + Double.toString(average));
        return (average >= Constants.FFI_MINIMUM_FALL_THRESHOLD);
    }

    private static double average(double[] samples, int start, int end) {
        double sum = 0;
        for (int i = start; i <= end; i++) {
            sum += samples[i];
        }

        return (sum / Constants.FFI_INTERVAL);
    }

    private static int FFIEnd(double[] samples, int peakTime) {
        int samplesAfterImpact = peakTime - Constants.FFI_INTERVAL;

        if (samplesAfterImpact < Constants.FFI_INTERVAL)
            return -1;

        for (int i = peakTime; i >= peakTime - Constants.FFI_INTERVAL; i--) {
            if (samples[i] <= Constants.FFI_THRESHOLD) {
                samplesAfterImpact = i;
                break;
            }
        }

        return samplesAfterImpact;
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
