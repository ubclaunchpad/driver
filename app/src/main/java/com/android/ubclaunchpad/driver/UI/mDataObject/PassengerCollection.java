package com.android.ubclaunchpad.driver.UI.mDataObject;


import com.android.ubclaunchpad.driver.R;

import java.util.ArrayList;

/**
 * Created by Noor on 7/31/2016.
 */

/**
 * TODO
 * This collection gets called when MyAdapter populates the recyclerview.
 * We have to populate this collection with real user profiles collected from bluetooth.
 */

public class PassengerCollection {

    public static ArrayList<Passenger> getPassengers() {

        ArrayList<Passenger> listPassengers = new ArrayList<>();

        listPassengers.add(new Passenger("Jessica", R.drawable.personicon, "2102 East Boulevard", "1234 West Broadway", 3, 33));
        listPassengers.add(new Passenger("Timothy", R.drawable.personicon, "222 Juniper St.", "222 Juniper St.", 2, 11));
        listPassengers.add(new Passenger("Hubert", R.drawable.personicon, "777 Beaverpoint Rd.", "345 Kensington Ave.", 5, 3));
        listPassengers.add(new Passenger("Muffin", R.drawable.personicon, "345 Kensington Ave.", "1234 West Broadway", 1, 21));
        listPassengers.add(new Passenger("Rob", R.drawable.personicon, "222 Juniper St.", "2102 East Boulevard", 4, 20));
        listPassengers.add(new Passenger("Alexis", R.drawable.personicon, "2102 East Boulevard", "1234 West Broadway", 3, 6));
        listPassengers.add(new Passenger("Emilia", R.drawable.personicon, "777 Beaverpoint Rd.", "666 CandyLand", 5, 4));

        return listPassengers;
    }
}