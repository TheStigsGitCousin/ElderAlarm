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
        if (Score >= 3 && Score < 7) {
            BackgroundService.context.stopService(MainActivity.serviceIntent);
            Intent intent = new Intent(BackgroundService.context, AlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyParcelable data = new MyParcelable(10, "cardiac arrest");
            intent.putExtra("message", data);
            BackgroundService.context.startActivity(intent);
            Log.d("DecisionMaker", "FALL DETECTED");


        }
    }

    public static double startEval(){
        double[] samples = AccelerometerHandler.window.getValueArray();
        Calendar[] timeArray = AccelerometerHandler.window.getTimeStampArray();

        //Check for peakTime
        Calendar peakTime = Algorithms.peakTime(samples, timeArray);
        //If no peakTime return, decision: NO FALL
        if (peakTime == null) {
            //Log.d("DecisionMaker", "Inget fall");
            return -1;
        }


        // Calculate impact end time
        Calendar impactEnd = Algorithms.impactEnd(samples, timeArray, peakTime);
        // Calculate impact start time
        Calendar impactStart = Algorithms.impactStart(samples, impactEnd, timeArray, peakTime);
        //If no Start or End return, decision: NO FALL
        if (impactEnd == null || impactStart == null) {
            return -1;
        }

        double Score = firstStage(samples, timeArray, peakTime,impactStart,impactEnd);
        return Score;
    }

    private static double firstStage(double[] samples, Calendar[] timeArray, Calendar peakTime, Calendar impactStart, Calendar impactEnd){
        double FFI = AlgorithmsAdv.FreeFallIndex(samples, timeArray, peakTime);
        double AAMV = AlgorithmsAdv.AAMV(samples, timeArray, impactStart, impactEnd);
        double MPI = AlgorithmsAdv.MaximumPeakIndex(samples);
        double MVI = AlgorithmsAdv.MinimumValleyIndex(samples, timeArray, impactStart, impactEnd);
        long PDI = AlgorithmsAdv.PeakDurationIndex(samples, timeArray);
        double ARI = AlgorithmsAdv.ActivityRatioIndex(samples, timeArray);

        double Temp = 0;

        Temp = ConstantsAdv.AAMV_AVG/AAMV;
        Temp = Temp + ConstantsAdv.MPI_AVG/MPI;
        Temp = Temp + ConstantsAdv.MVI_AVG/MVI;
        Temp = Temp + ConstantsAdv.PDI_AVG/PDI;
        Temp = Temp + ConstantsAdv.ARI_AVG/ARI;

        return Temp;





    }

    private static void secondStage(double IDI, double AAMV, double MVI){
        if(IDI>700 && (ConstantsAdv.IDI_AAMV_UP_MIN < AAMV && ConstantsAdv.IDI_AAMV_UP_MAX > AAMV)){

        }
        if(IDI<700 && (ConstantsAdv.IDI_AAMV_DOWN_MIN < AAMV && ConstantsAdv.IDI_AAMV_DOWN_MAX > AAMV)){

        }

        if(MVI > 1){
            //Indikera fall
        }

    }


}
