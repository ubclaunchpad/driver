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

    // requests take the form of https://maps.googleapis.com/maps/api/directions/outputFormat?parameters
    // outputFormat can be json or xml, we'll use json
    private static String DIRECTIONS_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private static String ORIGIN = "origin=";
    private static String DESTINATION = "destination=";
    private static String WAYPOINTS = "waypoints=";
    private static String API_KEY = "key=AIzaSyBviR5jNBd9ktpZMb25bmb8ZlK1MOJMEyY";
    private static String AMBERSAND = "&";

    private NetworkUtils networkUtils = new NetworkUtils();

    private Callback directionsCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

        }
    };

    public void getDirections(String originLatLng, String destinationLatLng, List<String> waypoints) {
        networkUtils.GetRequestAsync(
                encodeURL(originLatLng, destinationLatLng, waypoints),
                directionsCallback
        );
    }

    public String encodeURL(String originLatLng, String destinationLatLng, List<String> waypoints) {
        String encodedUrl = DIRECTIONS_BASE_URL +
                ORIGIN + originLatLng +
                AMBERSAND + DESTINATION + destinationLatLng;

        if (!waypoints.isEmpty()) {
            String wayPointsString = "";
            for (String next : waypoints) {
                wayPointsString = wayPointsString.concat(next + "|");
            }
            // remove last pipe character
            wayPointsString = wayPointsString.substring(0, wayPointsString.length() - 1);
            encodedUrl = encodedUrl.concat(AMBERSAND + WAYPOINTS + wayPointsString);
        }
        return encodedUrl + AMBERSAND + API_KEY;
    }
}
