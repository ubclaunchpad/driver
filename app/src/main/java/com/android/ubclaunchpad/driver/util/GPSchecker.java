package com.android.ubclaunchpad.driver.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;

/**
 * Created by Navjashan on 29/10/2016.
 */

public class GPSchecker{

    public LocationManager locationManager;

    public GPSchecker(LocationManager locationManager){
        this.locationManager = locationManager;
    }


    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    /*
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            makeUseOfNewLocation(location);
        }
*/
}
