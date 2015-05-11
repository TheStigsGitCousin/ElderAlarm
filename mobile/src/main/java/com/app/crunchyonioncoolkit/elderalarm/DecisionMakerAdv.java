package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Jack on 2015-05-05.
 */
public class DecisionMakerAdv {


    public static void fallDetection() {
        double Score = startEval();
        if (Score >= 25) {
            Log.d("DecisionMaker", "FALL DETECTED");
            AccelerometerHandler.window = new SlidingWindow();
            GyroscopeHandler.window = new SlidingWindow();

        }
    }

    public static int startEval(){
        double[] samples = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();
        double[] samplesGYR = GyroscopeHandler.window.getValueArray();
        Calendar[] timeArrayGYR = GyroscopeHandler.window.getTimeStampArray();
        //Check for peakTime
        Calendar peakTime = AlgorithmsAdv.peakTime(samples, timeArray);
        //If no peakTime return, decision: NO FALL
        if (peakTime == null) {
            //Log.d("DecisionMaker", "Inget fall");
            return -1;
        }


        // Calculate impact end time
        Calendar impactEnd = AlgorithmsAdv.impactEnd(samples, timeArray, peakTime);
        // Calculate impact start time
        Calendar impactStart = AlgorithmsAdv.impactStart(samples, impactEnd, timeArray, peakTime);
        //If no Start or End return, decision: NO FALL
        if (impactEnd == null || impactStart == null) {
            return -1;
        }

        int Score = firstStage(samplesGYR,timeArrayGYR, samples, timeArray, peakTime,impactStart,impactEnd);
        return Score;
    }

    private static int firstStage(double[] samplesGYR, Calendar[] timeArrayGYR, double[] samples, Calendar[] timeArray, Calendar peakTime, Calendar impactStart, Calendar impactEnd){
        int sum = 0;
        double IDI = impactEnd.getTimeInMillis() - impactStart.getTimeInMillis();
        Log.d("DecisionMakerAdv", "IDI: " + Double.toString(IDI));
        double FFI = AlgorithmsAdv.FreeFallIndex(samples, timeArray, peakTime);
        double AAMV = AlgorithmsAdv.AAMV(samples, timeArray, impactStart, impactEnd);
        double MPI = AlgorithmsAdv.MaximumPeakIndex(samples);
        double MVI = AlgorithmsAdv.MinimumValleyIndex(samples, timeArray, impactStart, impactEnd);
        long PDI = AlgorithmsAdv.PeakDurationIndex(samples, timeArray);
        double ARI = AlgorithmsAdv.ActivityRatioIndex(samples, timeArray);

        double Temp = 0;

        if(IDI < ConstantsAdv.IDI_MAX && IDI > ConstantsAdv.IDI_MIN){
            sum = sum +5;
        }
        Log.d("DecisionMakerAdv", "sum IDI: " + Integer.toString(sum));
        if(AAMV < ConstantsAdv.AAMV_MAX && AAMV > ConstantsAdv.AAMV_MIN){
            sum = sum +5;
        }
        Log.d("DecisionMakerAdv", "sum AAMV: " + Integer.toString(sum));
        if(MPI < ConstantsAdv.MPI_MAX && MPI > ConstantsAdv.MPI_MIN){
            sum = sum + 5;
        }
        Log.d("DecisionMakerAdv", "sum MPI: " + Integer.toString(sum));
        if(MVI < ConstantsAdv.MVI_MAX && MVI > ConstantsAdv.MVI_MIN){
            sum = sum + 5;
        }
        Log.d("DecisionMakerAdv", "sum MVI: " + Integer.toString(sum));
        if(PDI < ConstantsAdv.PDI_MAX && PDI > ConstantsAdv.PDI_MIN){
            sum = sum + 5;
        }
        Log.d("DecisionMakerAdv", "sum PDI: " + Integer.toString(sum));
        if(ARI < ConstantsAdv.ARI_MAX && ARI > ConstantsAdv.ARI_MIN){
            sum = sum + 5;
        }
        Log.d("DecisionMakerAdv", "sum ARI: " + Integer.toString(sum));

        if(FFI > 0){
            sum = sum -5;
        }
        Log.d("DecisionMakerAdv", "sum After FFI: " + Integer.toString(sum));
        double GYR = AlgorithmsAdv.GyroArea(samplesGYR, timeArrayGYR, peakTime);
        Log.d("DecisionMakerAdv", "GYR: " + Double.toString(GYR));
        if(GYR < 20 && sum > 15){ //Lika med
            sum = sum - 5;
        }
        else if(GYR > 20 && sum > 15){ //Lika med
            sum = sum + 5;
        }
        Log.d("DecisionMakerAdv", "sum After GYR: " + Integer.toString(sum));

        return sum;
    }

    private static int secondStage(int sum, double[] samplesGRY, Calendar[] timeArrayGYR, Calendar peakTime){

        return sum;
        //om vi inte kan bestämma mellan fall och slag, beräkna Area_GYRO och ta medel
    }


}
