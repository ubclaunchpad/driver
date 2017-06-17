package com.android.ubclaunchpad.driver.user;

import android.util.Log;

import com.android.ubclaunchpad.driver.user.User;
import com.android.ubclaunchpad.driver.user.UserManager;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherryuan on 16-10-15.
 */
public class UserUtils {

    private String TAG = getClass().getSimpleName();

    public static User buildDriver(String name, String destination, int seatNum) {
        User driver = new User();
        driver.setName(name);
        driver.setSeatNum(seatNum);
        driver.setDestinationLatLngStr(destination);
        return driver;
    }

    public static int getNumFreeSeats(User user) {
        return user.getSeatNum() - user.getPassengers().size();
    }

    public static User buildPassenger(String name, String destination) {
        User passenger = new User();
        passenger.setName(name);
        passenger.setDestinationLatLngStr(destination);
        passenger.setIsDriver(false);
        return passenger;
    }

    // finds latLngs within 30 meters of your current latLng
    public List<LatLng> findNearbyLatLngs(List<LatLng> latLngs) {

        LatLng ownLatLng;
        try {
            ownLatLng = StringUtils.stringToLatLng(UserManager.getInstance().getUser().getCurrentLatLngStr());
        } catch (Exception e) {
            Log.d(TAG, "caught exception getting user" + e);
            ownLatLng = new LatLng(0, 0);
        }

        List<LatLng> nearbyLatLng = new ArrayList<LatLng>();

        for (LatLng next : latLngs) {
            double lon1 = next.longitude;
            double lon2 = ownLatLng.longitude;
            double lat1 = next.latitude;
            double lat2 = ownLatLng.latitude;

            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            float dist = (float) (earthRadius * c);
            if (dist < 30) {
                nearbyLatLng.add(next);
            }
        }

        return nearbyLatLng;
    }
}
