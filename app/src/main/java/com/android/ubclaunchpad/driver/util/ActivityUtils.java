package com.android.ubclaunchpad.driver.util;

import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;




import java.util.List;

/**
 * This provides methods to help Activities load their View (Fragment).
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
    //this function gets the lat/long from an address that users input
    //https://developer.android.com/reference/android/location/Geocoder.html

//    public static GeoPoint getLocationFromAddress(String strAddress){
//
//        Geocoder coder = new Geocoder(this);
//        List<Address> address;
//        GeoPoint p1 = null;
//
//        try {
//            address = coder.getFromLocationName(strAddress,5);
//            if (address==null) {
//                return null;
//            }
//            Address location=address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
//                    (int) (location.getLongitude() * 1E6));
//
//            return p1;
//        }
//    }

