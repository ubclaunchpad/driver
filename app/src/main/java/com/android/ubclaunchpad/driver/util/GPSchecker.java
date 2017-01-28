package com.android.ubclaunchpad.driver.util;

import android.location.LocationManager;

/**
 * Created by Navjashan on 28/01/2017.
 */

public class GPSchecker {
    public LocationManager locationManager;
        public GPSchecker(LocationManager locationManager){
                this.locationManager = locationManager;
           }


                public boolean isLocationEnabled() {
                    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }


}
