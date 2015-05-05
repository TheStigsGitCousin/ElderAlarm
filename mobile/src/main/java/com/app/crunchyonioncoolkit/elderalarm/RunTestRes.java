package com.app.crunchyonioncoolkit.elderalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jack on 2015-04-28.
 */
public class RunTestRes {

    public void readRes(String Path) {
        String dataAcc = DataOut.readFromFile(Path + "_ACC.txt");
        String dataGyr = DataOut.readFromFile(Path + "_GYR.txt");
        String dataPul = DataOut.readFromFile(Path + "_PUL.txt");
        final List<DataAndTime> listAcc = parseDataAndTime(dataAcc);
        final List<DataAndTime> listGyr = parseDataAndTime(dataGyr);
        final List<DataAndTime> listPul = parseDataAndTime(dataPul);


        // Thread for accelerometer
        new Thread(new Runnable() {
            public void run() {

                Calendar time= Calendar.getInstance();
                for (int i = 0; i < listAcc.size() - 1; i++) {
                    long TimeDiff = listAcc.get(i + 1).Time - listAcc.get(i).Time;
                    long Start = Calendar.getInstance().getTimeInMillis();
                    while ((Calendar.getInstance().getTimeInMillis() - Start) < TimeDiff) {

                    }
                    time.setTimeInMillis(listAcc.get(i).Time);
                    AccelerometerHandler.testChange(listAcc.get(i + 1).Data, time);
                }
            }
        }).start();

        // Thread for gyroscope
        new Thread(new Runnable() {
            public void run() {

                for (int i = 0; i < listGyr.size() - 1; i++) {
                    long TimeDiff = listGyr.get(i + 1).Time - listGyr.get(i).Time;
                    long Start = Calendar.getInstance().getTimeInMillis();
                    while ((Calendar.getInstance().getTimeInMillis() - Start) < TimeDiff) {

                    }
                    GyroscopeHandler.testChange(listGyr.get(i + 1).Data);
                }
            }
        }).start();

        // Thread for heart rate
        new Thread(new Runnable() {
            public void run() {

                for (int i = 0; i < listPul.size() - 1; i++) {
                    long TimeDiff = listPul.get(i + 1).Time - listPul.get(i).Time;
                    long Start = Calendar.getInstance().getTimeInMillis();
                    while ((Calendar.getInstance().getTimeInMillis() - Start) < TimeDiff) {

                    }
                    PulseHandler.testChange(listPul.get(i + 1).Data);
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
