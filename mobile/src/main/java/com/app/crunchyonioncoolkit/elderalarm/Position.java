package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by David on 2015-04-10.
 */
public class Position {

    private static Location lastKnownLocation = null;

    private static LocationListener locationListener;

    public static void startLocationTracking(Context context) {
        // Acquire a reference to the system Position Manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 15) {
                    lastKnownLocation = location;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Position Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void stopLocationTracking(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }

    public static Location GetLastKnownLocation() {
        return lastKnownLocation;
    }
}
