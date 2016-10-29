package com.android.ubclaunchpad.driver.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

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

    public static User createUser(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public String serializeUser(){
        Gson gson = new Gson();
        return gson.toJson(this);
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

//    public JSONObject userToString() {
//
//        JSONObject userJSON = new JSONObject();
//
//        try {
//            userJSON.put("name", name);
//            userJSON.put("address", address);
//            userJSON.put("isDriver", isDriver);
//            userJSON.put("seatNum", seatNum);
//            userJSON.put("email", email);
//
//        }
//        catch (JSONException e) {
//        //
//        }
//
//        return userJSON;
//    }
}
