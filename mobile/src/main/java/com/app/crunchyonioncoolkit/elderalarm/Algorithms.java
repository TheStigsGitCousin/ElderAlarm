package com.app.crunchyonioncoolkit.elderalarm;

/**
 * Created by David on 2015-04-14.
 */
public class Algorithms {
    public static int peakTime(double[] samples) {
        int samplesAfterPeak = 0;
        int peakTime = 0;
        for (int i = 0; i < samples.length; i++) {
            if (samples[i] > Constants.PEAK_TIME_THRESHOLD) {
                samplesAfterPeak = 0;
                peakTime = i;
            }
            samplesAfterPeak++;
        }

        return samplesAfterPeak >= Constants.TIME_WITHOUT_PEAKS ? peakTime : -1;
    }

    // Calculate last significant impact with the ground
    public static int impactEnd(double[] samples, int peakTime) {
        int samplesAfterImpact = -1;
        int startPoint = peakTime + Constants.IMPACT_END_INTERVAL;

        if (startPoint > samples.length)
            return -1;

        for (int i = startPoint; i >= peakTime; i--) {
            if (samples[i] >= Constants.IMPACT_END_MAGNITUDE_THRESHOLD) {
                samplesAfterImpact = i;
                break;
            }
        }

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

        return samplesAfterImpact;
    }

    public static boolean AAMV(double[] samples, int impactStart, int impactEnd) {
        int middle = impactStart + ((impactEnd - impactStart) / 2);
        double sum = 0;

        for (int i = middle - (Constants.WIN_INTERVAL / 2); i <= middle + (Constants.WIN_INTERVAL / 2); i++) {
            sum += (Math.abs(samples[i + 1] - samples[i]) / Constants.WIN_INTERVAL);
        }

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

        return (maxAcceleration >= Constants.MAXIMUM_PEAK_THRESHOLD);

    }

    public static boolean MinimumValleyIndex(double[] samples, int impactStart, int impactEnd) {
        double minAcceleration = samples[impactStart - Constants.MVI_Interval];
        for (int i = impactStart - Constants.MVI_Interval; i <= impactEnd; i++) {
            if (samples[i] < minAcceleration)
                minAcceleration = samples[i];
        }

        return (minAcceleration >= Constants.MVI_AVERAGE_MAGNITUDE_LOW && minAcceleration <= Constants.MVI_AVERAGE_MAGNITUDE_HIGH);
    }

    public static boolean PeakDurationIndex(double[] samples, int peakTime) {
        int PDI = PDIEnd(samples, peakTime) - PDIStart(samples, peakTime);

        return (PDI < Constants.PDI_MAGNITUDE);
    }

    public static boolean ActivityRatioIndex(double[] samples, int impactStart, int impactEnd) {
        int middle = impactStart + ((impactEnd - impactStart) / 2);
        double count = 0;
        for (int i = middle - (Constants.ARI_INTERVAL / 2); i <= middle + (Constants.ARI_INTERVAL / 2); i++) {
            if (samples[i] < Constants.ARI_LOW || samples[i] > Constants.ARI_HIGH)
                count++;
        }

        return ((count / Constants.ARI_INTERVAL) > Constants.ARI_THRESHOLD);
    }

    public static boolean FreeFallIndex(double[] samples, int peakTime) {
        int end = FFIEnd(samples, peakTime);
        if (end == -1)
            return false;

        int start = end - Constants.FFI_INTERVAL;
        double average = average(samples, start, end);
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
