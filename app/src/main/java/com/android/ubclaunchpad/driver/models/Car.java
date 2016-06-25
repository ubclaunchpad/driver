package com.android.ubclaunchpad.driver.models;

/**
 * Created by Noor on 6/21/2016.
 */
public class Car {

    String make;
    String model;
    String colour;
    String seatNum;

    public Car() {
        // Default constructor required for calls to DataSnapshot.getValue(Car.class)
    }

    /**
     * Car constructor
     * @param seatNum
     * @param make
     * @param model
     * @param colour
     */
    public Car(String seatNum, String make, String model, String colour) {
        this.seatNum = seatNum;
        this.make = make;
        this.model = model;
        this.colour = colour;
    }
}
