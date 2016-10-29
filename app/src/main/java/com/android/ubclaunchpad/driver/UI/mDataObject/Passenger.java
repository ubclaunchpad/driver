package com.android.ubclaunchpad.driver.UI.mDataObject;


// Passenger class
public class Passenger {

    private String name;
    private String pickUpLocation;
    private String dropOffLocation;
    private int iconID;
    private int passengerRating;
    private int distance;

    public Passenger(String name, Integer iconID, String pickUpLocation, String dropOffLocation, Integer passengerRating, Integer distance) {
        this.name = name;
        this.iconID = iconID;
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
        this.passengerRating = passengerRating;
        this.distance = distance;
    }

    public String getPName() {
        return name;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public int getIconID() {
        return iconID;
    }

    public int getPassengerRating() {
        return passengerRating;
    }

    public int getDistance() {
        return distance;
    }
}
