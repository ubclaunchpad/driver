package com.android.ubclaunchpad.driver.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TheAhBooBoo on 2017-01-21.
 */

public interface LocationListener {

    /**
     * Called when user's location has changed
     */
    void onLocationChanged(LatLng location);

}
