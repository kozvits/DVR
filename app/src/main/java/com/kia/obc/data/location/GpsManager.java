package com.kia.obc.data.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GpsManager {
    private final LocationManager locationManager;
    private final MutableLiveData<Location> currentLocation = new MutableLiveData<>();

    public GpsManager(Context context) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startTracking() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                1000, // 1 second
                1.0f,  // 1 meter
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        currentLocation.postValue(location);
                    }
                }, 
                Looper.getMainLooper()
            );
        } catch (SecurityException e) {
            // Handle permission error
        }
    }

    public LiveData<Location> getLocationFlow() {
        return currentLocation;
    }
}
