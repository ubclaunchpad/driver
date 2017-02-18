package com.android.ubclaunchpad.driver.util;

import android.location.LocationProvider;

/**
 * Created by TheAhBooBoo on 2017-01-21.
 */
public class LocationManager {
    /** instance of LocationManager */
    private static LocationManager locationManager;

    /** location listener used to respond to changes in user location */
    private LocationListener locationListener;

    /** location provider used to provide changes in user location */
    private LocationProvider locnProvider;

    /**
     * Singleton instance of Location manager
     * @return Instance of user manager
     */
    public static LocationManager getInstance() {
        if(locationManager == null)
            locationManager = new LocationManager();
        return locationManager;
    }

    /**
     * Private constructor for singleton ensure only one instance is created
     */
    private LocationManager(){}
}
