package com.android.ubclaunchpad.driver.models;

import com.google.firebase.database.IgnoreExtraProperties;

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
    public Car car;

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
        car = null;
    }

    /**
     * Setter methods
     * @param
     */

    public void setCar(Car car) {
        isDriver = true;
        this.car = car;
    }

    public void resetName(String name){
        this.name = name;
    }

    public void resetEmail(String email) {
        this.email = email;
    }

    public void resetAddress(String address){
        this.address = address;
    }

    public void resetPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Just a bunch of getter methods
     * @return
     */

    public Boolean isDriver() {
        return isDriver;
    }

    public Car getCar() {
        return car;
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

    public String getPostalCode() {
        return postalCode;
    }
}
