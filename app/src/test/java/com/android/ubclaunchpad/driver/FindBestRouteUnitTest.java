package com.android.ubclaunchpad.driver;

import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FindBestRouteAlgorithm;
import com.android.ubclaunchpad.driver.util.UserUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by grigorii on 24/04/17.
 */

public class FindBestRouteUnitTest {
    @Test
    public void testFirstScenario() {
        LatLng start = new LatLng(49.240802, -123.111876);
        List<User> users = UserUtils.contructFirstTestScenario();
        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(start);
        List<User> driversWithPassengers = algorithm.findBestRoute(users);
        User driver1 = getDriverWithName(driversWithPassengers, "driver1");
        User driver2 = getDriverWithName(driversWithPassengers, "driver2");
        User driver3 = getDriverWithName(driversWithPassengers, "driver3");
        System.out.println("Scenario 1\n");
        for (User driver: driversWithPassengers) {
            System.out.println(driver.getName());
            for (User passenger: driver.getPassengers()) {
                System.out.println(passenger.getName());
            }
            System.out.println("\n\n");
        }
    }
    @Test
    public void testSecondScenario() {
        System.out.println("Scenario 2\n");
        LatLng startPt = new LatLng(49.280184, -123.122728);
        List<User> users = UserUtils.constructSecondTestCase();
        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(startPt);
        List<User> driversWithPassengers = algorithm.findBestRoute(users);
        for (User driver: driversWithPassengers) {
            System.out.println(driver.getName());
            for (User passenger: driver.getPassengers()) {
                System.out.println(passenger.getName());
            }
            System.out.println("\n\n");
        }
    }
    public User getDriverWithName(List<User> drivers, String name) {
        for (User driver: drivers) {
            if (driver.getName().equals(name)) {
                return driver;
            }
        }
        Assert.fail("The driver with the given name disappeared");
        return null;
    }
}
