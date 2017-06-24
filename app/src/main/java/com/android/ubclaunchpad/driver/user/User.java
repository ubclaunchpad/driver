package com.android.ubclaunchpad.driver.user;

import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


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
    public List<User> passengers;
    public String userUid;
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

    public User(User another) {
        this.name = another.getName();
        this.seatNum = another.getSeatNum();
        this.destinationLatLngStr = another.getDestinationLatLngStr();
        this.currentLatLngStr = another.getCurrentLatLngStr();
        this.isDriver = another.getIsDriver();
        this.passengers = another.getPassengers();
    }

    public static User createUser(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
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
        passengers = new ArrayList<>();
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

    public List<User> getPassengers() {
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

    @Exclude
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    /**
     * A bunch of getter methods
     */

    public Boolean getIsDriver() {
        return isDriver;
    }

    @Exclude
    public String getUserUid() {
        return userUid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name != null ? name.equals(user.name) : user.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
