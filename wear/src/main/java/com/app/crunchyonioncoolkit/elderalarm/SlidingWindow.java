package com.app.crunchyonioncoolkit.elderalarm;

import java.util.LinkedList;

/**
 * Created by David on 2015-04-14.
 */
public class SlidingWindow {

    private LinkedList<Double> window;

    public SlidingWindow() {
        window = new LinkedList<>();
    }

    public void newValue(double SMV) {
        window.addLast(SMV);

        if (window.size() > Constants.WINDOW_WIDTH) {
            window.removeFirst();
        }
    }

    public LinkedList<Double> getWindow(){
        return window;
    }
}
