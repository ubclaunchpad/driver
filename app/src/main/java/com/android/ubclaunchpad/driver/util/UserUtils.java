package com.android.ubclaunchpad.driver.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sherryuan on 16-10-15.
 */
public class UserUtils {

    public List<LatLng> findNearbyLatLngs(List<LatLng> latLngs){

        LatLng ownLatLng = HardwareUtils.getGPS();

        List<LatLng> nearbyLatLng = new LinkedList<LatLng>();

        for (LatLng next: latLngs){
            double lon1 = next.longitude;
            double lon2 = ownLatLng.longitude;
            double lat1 = next.latitude;
            double lat2 = ownLatLng.latitude;

            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lon2-lon1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            float dist = (float) (earthRadius * c);
            if (dist < 30){
                nearbyLatLng.add(next);
            }
        }

        return nearbyLatLng;
    }
}
