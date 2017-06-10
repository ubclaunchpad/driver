package com.android.ubclaunchpad.driver.util;

import android.location.LocationManager;

/**
 * Created by Navjashan on 28/01/2017.
 */

public class GPSchecker {
    public LocationManager mGPSchecker;

    public GPSchecker(LocationManager mGPSchecker) {
        this.mGPSchecker = mGPSchecker;
    }


    public boolean isLocationEnabled() {
        return mGPSchecker.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mGPSchecker.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}
