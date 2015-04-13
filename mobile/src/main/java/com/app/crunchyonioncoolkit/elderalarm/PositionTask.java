package com.app.crunchyonioncoolkit.elderalarm;

import android.location.Location;
import android.os.AsyncTask;

import java.util.Calendar;

/**
 * Created by David on 2015-04-10.
 */
public class PositionTask extends AsyncTask<Void, Integer, Location> {
    @Override
    protected Location doInBackground(Void... params) {
        Location location;
        Long t = Calendar.getInstance().getTimeInMillis();
        while ((location = Position.GetLastKnownLocation()) == null && Calendar.getInstance().getTimeInMillis() - t < 15000) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return location;
    }

    @Override
    protected void onPostExecute(Location location) {
        super.onPostExecute(location);

        // Start function for doing work with current location
        // doWork();
    }
}
