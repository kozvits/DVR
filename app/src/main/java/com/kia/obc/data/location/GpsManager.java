package com.kia.obc.data.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsManager {
    private static final String TAG = "GpsManager";
    private LocationManager locationManager;
    private GpsCallback callback;
    private LocationListener activeListener;

    public interface GpsCallback {
        void onLocationChanged(Location location);
    }

    public void startTracking(Context context, GpsCallback callback) {
        this.callback = callback;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        
        try {
            activeListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (callback != null) callback.onLocationChanged(location);
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderDisabled(String provider) {}
                @Override public void onProviderEnabled(String provider) {}
            };

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1.0f, 
                activeListener
            );
        } catch (SecurityException e) {
            Log.e(TAG, "GPS Permission missing");
        }
    }

    public void stopTracking() {
        if (locationManager != null && activeListener != null) {
            locationManager.removeUpdates(activeListener);
        }
    }
}
