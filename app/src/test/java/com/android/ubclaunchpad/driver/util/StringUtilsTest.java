package com.android.ubclaunchpad.driver.util;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sherryuan on 2017-03-15.
 */
public class StringUtilsTest {
    @Test
    public void latLngToStringTest() throws Exception {
        LatLng latLng1 = new LatLng(0, 0);
        Double latitude1 = latLng1.latitude;
        Double longitude1 = latLng1.longitude;
        String latLngString1 = latitude1.toString() + "," + longitude1.toString();
        assertEquals(StringUtils.latLngToString(latLng1), "0.0,0.0");
        assertEquals(StringUtils.latLngToString(latLng1), latLngString1);

        LatLng latLng2 = new LatLng(-23.3524, 53.341);
        Double latitude2 = latLng2.latitude;
        Double longitude2 = latLng2.longitude;
        String latLngString2 = latitude2.toString() + "," + longitude2.toString();
        assertEquals(StringUtils.latLngToString(latLng2), "-23.3524,53.341");
        assertEquals(StringUtils.latLngToString(latLng2), latLngString2);
    }
}