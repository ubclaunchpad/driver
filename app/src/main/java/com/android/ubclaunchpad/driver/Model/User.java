package com.android.ubclaunchpad.driver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by kelvinchan on 2016-05-28.
 */
public class User {
    String firstName = "";
    String lastName = "";
    @JsonIgnore
    String fullName;

    String homeAddress;
    GeoLocation coordinate;

    boolean isDriver;

    public User (String firstName, String LastName, String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = getFullName();
        this.homeAddress = address;
        coordinate = new GeoLocation();
    }

    //Firebase constructor
    public User(String firstName, String lastName, String homeAddress, GeoLocation coordinate, boolean isDriver){
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = getFullName();
        this.homeAddress = homeAddress;
        this.coordinate = coordinate;
        this.isDriver = isDriver;
    }


    //Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    //Don't think this is necessary
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public GeoLocation getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoLocation coordinate) {
        this.coordinate = coordinate;
    }

    public boolean getIsDriver() { return isDriver; }

    public void setIsDriver(boolean isDriver){
        this.isDriver = isDriver;
    }

}
