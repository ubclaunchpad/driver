package com.android.ubclaunchpad.driver.Model;

/**
 * Created by kelvinchan on 2016-05-28.
 */
public class GeoLocation {
    double latitude;
    double longitude;

    /*
        Default constructor with dummy lat/lng values
     */
    public GeoLocation(){
        latitude = 0;
        longitude = 0;
    }

    public GeoLocation(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    //Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
