package com.android.ubclaunchpad.driver.models;

public class Driver extends User{

    private int numSeats;

    public Driver() {

    }

    public Driver(String username, String email, int numSeats) {
        super(username, email);
        this.numSeats = numSeats;
    }

    public int getNumSeats() {
        return numSeats;
    }
}
