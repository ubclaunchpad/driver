package com.android.ubclaunchpad.driver.network;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sherryuan on 2017-07-03.
 */
public class GoogleDirectionsApi {

    // base URL for launching an Intent to the Google Maps app
    // https://developers.google.com/maps/documentation/urls/guide
    private static String DIRECTIONS_BASE_URL = "https://www.google.com/maps/dir/?api=1&";
    private static String ORIGIN = "origin=";
    private static String DESTINATION = "destination=";
    private static String WAYPOINTS = "waypoints=";
    private static String AMPERSAND = "&";

    public String encodeURL(String originLatLng, String destinationLatLng, List<String> waypoints) {
        String encodedUrl = DIRECTIONS_BASE_URL +
                ORIGIN + originLatLng +
                AMPERSAND + DESTINATION + destinationLatLng;

        if (!waypoints.isEmpty()) {
            String wayPointsString = "";
            for (String next : waypoints) {
                wayPointsString = wayPointsString.concat(next + "|");
            }
            // remove last pipe character
            wayPointsString = wayPointsString.substring(0, wayPointsString.length() - 1);
            encodedUrl = encodedUrl.concat(AMPERSAND + WAYPOINTS + wayPointsString);
        }
        return encodedUrl;
    }
}
