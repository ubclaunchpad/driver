package com.android.ubclaunchpad.driver.models;

import android.app.Application;

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
    public String postalCode;
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
     * @param address
     * @param postalCode
     */
    public User(String name, String email, String address, String postalCode) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
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

    public void makePostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public String grabPostalCode() {
        return postalCode;
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
}
