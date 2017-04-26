package com.android.ubclaunchpad.driver.models;

import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;


/**
 * User model. Used to serialize and deserialize data to/from
 * POJOs (Plain Old Java Objects).
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String destinationLatLngStr;
    public String currentLatLngStr;
    public Boolean isDriver;
    public Integer seatNum;
    public Set<User> passengers;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
        // empty
    }

    /**
     * User contructor
     *
     * @param name
     * @param email
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.destinationLatLngStr = "";
        this.currentLatLngStr = "";
        isDriver = false;
        seatNum = null;
    }

    public static User createUser(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public int getNumFreeSeats() {
        return seatNum - passengers.size();
    }

    public String serializeUser() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Setter methods
     *
     * @param
     */

    public void setSeatNum(Integer seatNum) {
        isDriver = true;
        this.seatNum = seatNum;
        passengers = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addPassenger(User passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(User passenger) {
        passengers.remove(passenger);
    }

    public Set<User> getPassengers() {
        return passengers;
    }

    @Exclude
    public void setDestinationLatLngStr(LatLng latLng) {
        this.destinationLatLngStr = StringUtils.latLngToString(latLng);
    }

    public void setDestinationLatLngStr(String latLng) {
        this.destinationLatLngStr = latLng;
    }

    @Exclude
    public void setCurrentLatLngStr(LatLng latLng) {
        this.currentLatLngStr = StringUtils.latLngToString(latLng);
    }

    public void setCurrentLatLngStr(String latLng) {
        this.currentLatLngStr = latLng;
    }

    public void setIsDriver(boolean isDriver) {
        this.isDriver = isDriver;
    }

    /**
     * A bunch of getter methods
     */

    public Boolean isDriver() {
        return isDriver;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDestinationLatLngStr() {
        return destinationLatLngStr;
    }

    public String getCurrentLatLngStr() {
        return currentLatLngStr;
    }
}
