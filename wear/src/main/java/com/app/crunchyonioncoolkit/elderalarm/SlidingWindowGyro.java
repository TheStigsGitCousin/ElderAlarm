package com.app.crunchyonioncoolkit.elderalarm;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Jack on 2015-05-06.
 */
public class SlidingWindowGyro {

    private LinkedList<RawSample> window;


    public SlidingWindowGyro() {
        window = new LinkedList<>();
    }

    public void newRawValue(double x, double y, double z){
        window.addLast(new RawSample(x,y,z, Calendar.getInstance()));

        if(window.getLast().timeStamp.getTimeInMillis() - window.getFirst().timeStamp.getTimeInMillis() > Constants.WINDOW_WIDTH) {
            window.removeFirst();
        }

    }
    public class RawSample{
        public double xVal;
        public double yVal;
        public double zVal;
        public Calendar timeStamp;

        public RawSample(double x, double y, double z, Calendar t){
            xVal = x;
            yVal = y;
            zVal = z;
            timeStamp = t;
        }
    }


}
