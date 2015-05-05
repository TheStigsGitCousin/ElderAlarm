package com.app.crunchyonioncoolkit.elderalarm;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Jack on 2015-04-16.
 */
public class DecisionMakerSimple {

    private final static String TAG = "DecisionMakerSimple";

    public static boolean detectFall() {
        double[] SMVArray = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();
        int sum = 0;
        // Acceleration
        Calendar peakTime = AlgorithmsSimple.peakTime(SMVArray, timeArray);
        if (peakTime == null)
            return false;

        Log.d(TAG, "Peak time (ms): " + Long.toString(peakTime.getTimeInMillis()));
        sum += AlgorithmsSimple.MPI(SMVArray) ? Constants.MPI_SIMPLE_SCORE : 0;

        sum += AlgorithmsSimple.SMA(SMVArray) ? Constants.SMA_SIMPLE_SCORE : 0;
        // HeartRate
        //sum += AlgorithmsSimple.MHRI(PulseHandler.window.getValueArray(), PulseHandler.window.getTimeStampArray(), peakTime) ? Constants.MHRI_SIMPLE_SCORE : 0;
        // Gyroscope
        //sum += AlgorithmsSimple.MGI(GyroscopeHandler.window.getValueArray()) ? Constants.MGI_SIMPLE_SCORE : 0;

        Log.d(TAG, "Sum: " + Integer.toString(sum));
        if (sum >= Constants.FALL_SCORE_RESUALT_THRESHOLD) {

        }
        return sum >= Constants.FALL_SCORE_RESUALT_THRESHOLD;
    }

    public void TestAlgorithm() {
        String data = DataOut.readFromFile("ACC.txt");
        String[] array = data.split("\n");
        AccelerometerHandler accelerometerHandler = new AccelerometerHandler(false);
        String dataGyr = DataOut.readFromFile("GYR.txt");
        String[] arrayGyr = dataGyr.split("\n");
        String[] valueAndTime;
        for (int i = 0; i < array.length; i++) {
            valueAndTime = array[i].split(";");
            accelerometerHandler.testChange(Double.parseDouble(valueAndTime[0]));
            valueAndTime = arrayGyr[i].split(";");
            GyroscopeHandler.window.newValue(Double.parseDouble(valueAndTime[0]));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


