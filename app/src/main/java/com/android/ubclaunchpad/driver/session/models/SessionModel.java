package com.android.ubclaunchpad.driver.session.models;

import com.android.ubclaunchpad.driver.user.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.user.UserManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sherryuan on 2017-02-04.
 */

public class SessionModel {

    // list of strings representing driver users' unique IDs
    private List<String> drivers;
    // list of strings representing passenger users' unique IDs
    private List<String> passengers;
    // location of the starting location
    private String location;
    private String name;

    // the person who runs the algorithm
    private String sessionHostUid;
    
    private Map<User, List<User>> driverPassengers;

    public SessionModel() {
        drivers = new ArrayList<>();
        passengers = new ArrayList<>();
        location = "";
    }

    public SessionModel(LatLng latLng) {
        drivers = new ArrayList<>();
        passengers = new ArrayList<>();
        this.location = StringUtils.latLngToString(latLng);
    }

    public void setDrivers(List<String> drivers) {
        this.drivers = drivers;
    }

    public void addDriver(String driver) {
        this.drivers.add(driver);
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(String passenger) {
        this.passengers.add(passenger);
    }

    public void setLocation(String latLngString) {
        this.location = latLngString;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDriverPassengers(Map<User, List<User>> driverPassengers) {
        this.driverPassengers = driverPassengers;
    }

    public void setSessionHostUid(String sessionHostUid) {
        this.sessionHostUid = sessionHostUid;
    }

    public List<String> getDrivers() {
        return drivers;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getSessionHostUid() {
        return sessionHostUid;
    }

    public Map<User, List<User>> getDriverPassengers() {
        return driverPassengers;
    }

    // creates new Session with the creator in the drivers list if they're a driver
    // or passengers list if they're a passenger.
    // other than the creator lists are empty, and latlng should be calculated based on creator's location
    public static SessionModel createNewSession(String name, LatLng latLng) {

        SessionModel sessionModel = new SessionModel(latLng);
        sessionModel.setName(name);

        try {
            User user = UserManager.getInstance().getUser();

            String uid = FirebaseUtils.getFirebaseUser().getUid();

            sessionModel.setSessionHostUid(uid);
            if (user.getIsDriver()) {

                sessionModel.addDriver(uid);
            } else {
                sessionModel.addPassenger(uid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionModel;
    }
}
