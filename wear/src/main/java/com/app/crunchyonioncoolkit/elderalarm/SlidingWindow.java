package com.app.crunchyonioncoolkit.elderalarm;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by David on 2015-04-14.
 */
public class SlidingWindow {

    private LinkedList<Sample> window;

    public SlidingWindow() {
        window = new LinkedList<>();
    }

    public void newValue(double SMV) {
        window.addLast(new Sample(SMV, new Date()));

        if (window.size() > Constants.WINDOW_WIDTH) {
            window.removeFirst();
        }
    }

    public LinkedList<Sample> getWindow() {
        return window;
    }

    public double[] getArray(){

        double[] array = new double[window.size()];
        int i = 0;
        for (Sample d : window) {
            array[i] = d.value;
            i++;
        }
        return array;

    }

    public double[] getValueArray(){

        double[] array = new double[window.size()];
        int i = 0;
        for (Sample d : window) {
            array[i] = d.value;
            i++;
        }
        return array;

    }

    public Date[] getTimeStampArray(){

        Date[] array = new Date[window.size()];
        int i = 0;
        for (Sample d : window) {
            array[i] = d.timeStamp;
            i++;
        }
        return array;

    }

    public class Sample{
        public double value;
        public Date timeStamp;
        public Sample(double w, Date t){
            value = w;
            timeStamp = t;
        }

    }

}
