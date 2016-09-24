package com.android.ubclaunchpad.driver.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User model. Used to serialize and deserialize data to/from
 * POJOs (Plain Old Java Objects).
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String address;
    public LatLng latLng;
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
        this.latLng = new LatLng(0, 0);
        isDriver = false;
        seatNum = null;
    }

    /**
     * Setter methods
     * @param
     */

    public void makeSeatNum(Integer seatNum) {
        isDriver = true;
        this.seatNum = seatNum;
    }

    public void makeName(String name){
        this.name = name;
    }

    public void makeEmail(String email) {
        this.email = email;
    }

    public void makeAddress(String address){
        this.address = address;
    }

    public void makeLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /**
     * Just a bunch of getter methods
     * @return
     */

    public Boolean isDriver() {
        return isDriver;
    }

    public Integer grabSeatNum() {
        return seatNum;
    }

    public String grabUserName(){
        return name;
    }

    public String grabEmail() {
        return email;
    }

    public String grabAddress() {
        return address;
    }

    public LatLng grabLatLng() {
        return latLng;
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

    public String getEmail() {
        return email;
    }
}
