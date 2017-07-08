package com.android.ubclaunchpad.driver.network;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sherryuan on 2017-07-03.
 */
public class GoogleDirectionsApiTest {

    @Test
    public void testEncodeUrl() {
        GoogleDirectionsApi googleDirectionsApi = new GoogleDirectionsApi();

        // test case where waypoints are empty
        String urlWithOutWaypoints = googleDirectionsApi.encodeURL("41.43206,-81.38992", "41.412,-81.245", new ArrayList<String>());
        assertEquals(urlWithOutWaypoints,
                "https://www.google.com/maps/dir/?api=1&origin=41.43206,-81.38992&destination=41.412,-81.245");

        // test case with multiple waypoints
        List<String> wayPoints = new ArrayList<>();
        wayPoints.add("-37.81223,144.96254");
        wayPoints.add("-34.92788,138.60008");
        String urlWithWaypoints = googleDirectionsApi.encodeURL("-33.8688,151.2093", "-31.9505,115.8605", wayPoints);
        assertEquals(urlWithWaypoints,
                "https://www.google.com/maps/dir/?api=1&origin=-33.8688,151.2093&destination=-31.9505,115.8605&waypoints=-37.81223,144.96254|-34.92788,138.60008"
        );
    }
}