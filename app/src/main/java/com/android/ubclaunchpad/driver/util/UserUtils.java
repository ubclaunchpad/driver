package com.android.ubclaunchpad.driver.util;

import android.content.Context;
import android.util.Log;

import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherryuan on 16-10-15.
 */
public class UserUtils {

    private String TAG = getClass().getSimpleName();

    // finds latLngs within 30 meters of your current latLng
    public List<LatLng> findNearbyLatLngs(List<LatLng> latLngs){

        LatLng ownLatLng;
        try {
            ownLatLng = StringUtils.stringToLatLng(UserManager.getInstance().getUser().getCurrentLatLngStr());
        } catch (Exception e) {
            Log.d(TAG, "caught exception getting user" + e);
            ownLatLng = new LatLng(0, 0);
        }

        List<LatLng> nearbyLatLng = new ArrayList<LatLng>();

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
    public static List<User> contructFirstTestScenario() {
        ArrayList<User> users = new ArrayList<>();
        User driver1 = buildDriver("driver1", "49.279172, -123.117412", 3);
        users.add(driver1);
        User driver2 = buildDriver("driver2", "49.256545,-123.246030", 3);
        users.add(driver2);
        User driver3 = buildDriver("driver3", "49.210140,-122.986306", 3);
        users.add(driver3);
        User passenger1 = buildPassenger("passenger1", "49.202280, -123.013378");
        users.add(passenger1);
        User passenger2 = buildPassenger("passenger2", "49.261279,-123.223467");
        users.add(passenger2);
        User passenger3 = buildPassenger("passenger3", "49.272868,-123.010140");
        users.add(passenger3);
        User passenger4 = buildPassenger("passenger4", "49.206621,-123.113195");
        users.add(passenger4);
        User passenger5 = buildPassenger("passenger5", "49.226209,-123.197249");
        users.add(passenger5);
        User passenger6 = buildPassenger("passenger6", "49.236955,-123.003821");
        users.add(passenger6);
        User passenger7 = buildPassenger("passenger7", "49.255424,-123.083357");
        users.add(passenger7);
        User passenger8 = buildPassenger("passenger8", "49.275704, -123.129479");
        users.add(passenger8);
        return users;
    }
    public static List<User> constructSecondTestCase() {
        List<User> users = new ArrayList<>();
        User driver1 = buildDriver("driver1", "49.261203, -123.243026", 3);
        User driver2 = buildDriver("driver2", "49.197418, -123.111362", 3);
        User driver3 = buildDriver("driver3", "49.283394, -122.982468", 3);
        User passenger1 = buildPassenger("passenger1", "49.264669, -123.255404");
        User passenger2 = buildPassenger("passenger2", "49.205305, -123.023442");
        User passenger3 = buildPassenger("passenger3", "49.241653, -123.111566");
        User passenger4 = buildPassenger("passenger4", "49.257302, -123.153261");
        User passenger5 = buildPassenger("passenger5", "49.246742, -123.001179");
        users.add(driver1);
        users.add(driver2);
        users.add(driver3);
        users.add(passenger1);
        users.add(passenger2);
        users.add(passenger3);
        users.add(passenger4);
        users.add(passenger5);
        return users;
    }

    public static List<User> constructThirdTestCase() {

        List<User> users = new ArrayList<>();
        User driver1 = buildDriver("driver1", "49.256920, -123.244176", 4);
        User driver2 = buildDriver("driver2", "49.252701, -123.064430", 3);
        User driver3 = buildDriver("driver3l", "49.289151, -123.134583", 1);
        User driver4 = buildDriver("driver4", "49.287517, -122.988775", 2);

        User passenger1 = buildPassenger("passenger1", "49.219689, -123.165631");
        User passenger2 = buildPassenger("passenger2", "49.252483, -123.161686");
        User passenger3 = buildPassenger("passenger3", "49.215156, -123.047359");
        User passenger4 = buildPassenger("passenger4", "49.284518, -123.041547");
        User passenger5 = buildPassenger("passenger5", "49.238098, -123.106244");
        User passenger6 = buildPassenger("passenger6", "49.269551, -123.095222");
        User passenger7 = buildPassenger("passenger7", "49.268578, -123.242033");

        users.add(driver1);
        users.add(driver2);
        users.add(driver3);
        users.add(driver4);
        users.add(passenger1);
        users.add(passenger2);
        users.add(passenger3);
        users.add(passenger4);
        users.add(passenger5);
        users.add(passenger6);
        users.add(passenger7);

        return users;
    }


    public static User buildDriver(String name, String destination, int seatNum) {
        User driver = new User();
        driver.setName(name);
        driver.setSeatNum(seatNum);
        driver.setDestinationLatLngStr(destination);
        return driver;
    }
    public static User buildPassenger(String name, String destination) {
        User passenger = new User();
        passenger.setName(name);
        passenger.setDestinationLatLngStr(destination);
        passenger.setIsDriver(false);
        return passenger;
    }
}
