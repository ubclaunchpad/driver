package com.android.ubclaunchpad.driver.models;

import com.android.ubclaunchpad.driver.util.HardwareUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * User model. Used to serialize and deserialize data to/from
 * POJOs (Plain Old Java Objects).
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String address;
    public String latLngAsString;
    public Boolean isDriver;
    public Integer seatNum;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
        // empty
    }

    /**
     * User contructor
     * @param name
     * @param email
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.address = "";
        this.latLngAsString = "";
        isDriver = false;
        seatNum = null;
    }

    /**
     * Setter methods
     * @param
     */

    public void setSeatNum(Integer seatNum) {
        isDriver = true;
        this.seatNum = seatNum;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setLatLngAsString(LatLng latLng) {
        Double lat = latLng.latitude;
        Double lng = latLng.longitude;
        this.latLngAsString = lat.toString() + "," + lng.toString();
    }

    /**
     * Just a bunch of getter methods
     * @return
     */

    public Boolean isDriver() {
        return isDriver;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public String getUserName(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getLatLngAsString() {
        return latLngAsString;
    }

    public JSONObject userToString() {

        JSONObject userJSON = new JSONObject();

        try {
            userJSON.put("name", name);
            userJSON.put("address", address);
            userJSON.put("isDriver", isDriver);
            userJSON.put("seatNum", seatNum);
            userJSON.put("email", email);

        }
        catch (JSONException e) {
        //
        }

        return userJSON;
    }

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
