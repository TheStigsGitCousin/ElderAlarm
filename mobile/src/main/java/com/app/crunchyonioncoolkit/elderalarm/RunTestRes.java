package com.app.crunchyonioncoolkit.elderalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jack on 2015-04-28.
 */
public class RunTestRes {

    public void readRes(String Path) {
        String DataAcc = DataOut.readFromFile(Path + "_ACC.txt");
        String DataGyr = DataOut.readFromFile(Path + "_GYR.txt");
        String DataPul = DataOut.readFromFile(Path + "_PUL.txt");
        final List<DataAndTime> ListAcc = parseDataAndTime(DataAcc);
        final List<DataAndTime> ListGyr = parseDataAndTime(DataGyr);
        final List<DataAndTime> ListPul = parseDataAndTime(DataPul);


        // Thread for accelerometer
        new Thread(new Runnable() {
            public void run() {

                for (int i = 0; i < ListAcc.size() - 1; i++) {
                    long TimeDiff = ListAcc.get(i + 1).Time - ListAcc.get(i).Time;
                    long Start = Calendar.getInstance().getTimeInMillis();
                    while ((Calendar.getInstance().getTimeInMillis() - Start) < TimeDiff) {

                    }
                    AccelerometerHandler.testChange(ListAcc.get(i + 1).Data);
                }
            }
        }).start();

        // Thread for gyroscope
        new Thread(new Runnable() {
            public void run() {

                for (int i = 0; i < ListGyr.size() - 1; i++) {
                    long TimeDiff = ListGyr.get(i + 1).Time - ListGyr.get(i).Time;
                    long Start = Calendar.getInstance().getTimeInMillis();
                    while ((Calendar.getInstance().getTimeInMillis() - Start) < TimeDiff) {

                    }
                    GyroscopeHandler.testChange(ListGyr.get(i + 1).Data);
                }
            }
        }).start();

        // Thread for heart rate
        new Thread(new Runnable() {
            public void run() {

                for (int i = 0; i < ListPul.size() - 1; i++) {
                    long TimeDiff = ListPul.get(i + 1).Time - ListPul.get(i).Time;
                    long Start = Calendar.getInstance().getTimeInMillis();
                    while ((Calendar.getInstance().getTimeInMillis() - Start) < TimeDiff) {

                    }
                    PulseHandler.testChange(ListPul.get(i + 1).Data);
                }
            }
        }).start();
    }

    public List<DataAndTime> parseDataAndTime(String Data) {


        String[] array = Data.split("\n");
        String[] valueAndTime;
        List<DataAndTime> List = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            //Log.d(TAG, Integer.toString(i));
            valueAndTime = array[i].split(";");
            if (valueAndTime.length == 2)
                List.add(new DataAndTime(valueAndTime[0], valueAndTime[1]));
        }

        return List;
    }

    class DataAndTime {
        public double Data;
        public long Time;

        public DataAndTime(String Data, String Time) {
            this.Data = Double.parseDouble(Data);
            this.Time = Long.parseLong(Time);
        }

    }
}
