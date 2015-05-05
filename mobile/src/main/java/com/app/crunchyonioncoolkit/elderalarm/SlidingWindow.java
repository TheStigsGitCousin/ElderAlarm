package com.app.crunchyonioncoolkit.elderalarm;

import java.util.Calendar;
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
        synchronized(window) {
            window.addLast(new Sample(SMV, Calendar.getInstance()));

            if (window.size() > Constants.WINDOW_WIDTH) {
                window.removeFirst();
            }
        }
    }

    public void newValue(double SMV, Calendar time) {
        synchronized(window) {
            window.addLast(new Sample(SMV, time));

            if (window.size() > Constants.WINDOW_WIDTH) {
                window.removeFirst();
            }
        }
    }

    public LinkedList<Sample> getWindow() {
        return window;
    }

    public double[] getValueArray(){

        double[] array = new double[0];
        synchronized(window) {
            array = new double[window.size()];
            int i = 0;
            for (Sample d : window) {
                array[i] = d.value;
                i++;
            }
        }
        return array;

    }

    public Calendar[] getTimeStampArray(){
        Calendar[] array = new Calendar[0];
        synchronized(window) {
            array = new Calendar[window.size()];
            int i = 0;
            for (Sample d : window) {
                array[i] = d.timeStamp;
                i++;
            }
        }

        return array;

    }

    public class Sample{
        public double value;
        public Calendar timeStamp;
        public Sample(double w, Calendar t){
            value = w;
            timeStamp = t;
        }

    }

}
