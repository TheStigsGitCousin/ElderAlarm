package com.app.crunchyonioncoolkit.elderalarm;

/**
 * Created by David on 2015-04-14.
 */
public class DecisionMaker {

    private static SlidingWindow window = new SlidingWindow();

    public static void accelerationChange(float[] acceleration) {
        fallDetection(acceleration);
    }

    public static void pulseChange(float heartRate) {

    }

    private static void fallDetection(float[] acceleration) {
        double SMV = Math.sqrt((acceleration[0] * acceleration[0]) + (acceleration[1] * acceleration[1]) + (acceleration[2] * acceleration[2]));
        window.newValue(SMV);
        double[] array = new double[window.getWindow().size()];
        int i = 0;
        for (double d : window.getWindow()) {
            array[i] = d;
            i++;
        }

        int keyEvent = keyEvents(array);
    }

    private static int keyEvents(double[] sample) {
        // Peak time
        int peakTime = Algorithms.peakTime(sample);
        if (peakTime == -1)
            return 0;

        // impact end
        int impactEnd = Algorithms.impactEnd(sample, peakTime);
        // impact start
        int impactStart = Algorithms.impactStart(sample, impactEnd, peakTime);
        // MPI
        int MPI = Algorithms.MaximumPeakIndex(sample, impactStart, impactEnd);
        // AAMV
        int AAMV = Algorithms.AAMV(sample, impactStart, impactEnd);

        return MPI + AAMV;
    }

}
