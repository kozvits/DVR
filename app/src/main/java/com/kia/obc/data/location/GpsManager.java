package com.kia.obc.data.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsManager {
    private static final String TAG = "GpsManager";
    private LocationManager locationManager;
    private GpsCallback callback;

    public interface GpsCallback {
        void onLocationChanged(Location location);
    }

    public void startTracking(GpsCallback callback) {
        this.callback = callback;
        locationManager = (LocationManager) android.content.Context.getSystemService(android.content.Context.LOCATION_SERVICE);
        
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1.0f, 
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (callback != null) callback.onLocationChanged(location);
                    }
                    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                    @Override public void onProviderDisabled(String provider) {}
                    @Override public void onProviderEnabled(String provider) {}
                }
            );
        } catch (SecurityException e) {
            Log.e(TAG, "GPS Permission missing");
        }
    }

    public void stopTracking() {
        if (locationManager != null) {
            // Implementation to remove updates
        }
    }
}
