package com.android.ubclaunchpad.driver;

import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FindBestRouteAlgorithm;
import com.android.ubclaunchpad.driver.util.UserUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by grigorii on 24/04/17.
 */

public class FindBestRouteUnitTest {

    @Test
    public void testFirstScenario() {
        System.out.println("Scenario 1");
        LatLng start = new LatLng(49.240802, -123.111876);
        List<User> users = UserUtils.contructFirstTestScenario();
        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(start);
        List<User> driversWithPassengers = algorithm.findBestRoute(users);
        User driver1 = getDriverWithName(driversWithPassengers, "driver1");
        User driver2 = getDriverWithName(driversWithPassengers, "driver2");
        User driver3 = getDriverWithName(driversWithPassengers, "driver3");
        System.out.println("");
        System.out.println("Driv1");
        for (User passenger: driver1.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println("Driv2");
        for (User passenger: driver2.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println("Driv3");
        for (User passenger: driver3.getPassengers()) {
            System.out.println(passenger.getName());
        }

        System.out.println();
        System.out.println();
        System.out.println();
        /*
        assertTrue(driver3.getPassengers().contains(new User("passenger1", null)));
        assertTrue(driver2.getPassengers().contains(new User("passenger2", null)));
        assertTrue(driver3.getPassengers().contains(new User("passenger3", null)));
        assertTrue(driver2.getPassengers().contains(new User("passenger4", null)));
        assertTrue(driver2.getPassengers().contains(new User("passenger5", null)));
        assertTrue(driver3.getPassengers().contains(new User("passenger6", null)));
        assertTrue(driver1.getPassengers().contains(new User("passenger7", null)));
        assertTrue(driver1.getPassengers().contains(new User("passenger8", null)));
        */
    }

    @Test
    public void testSecondScenario() {
        System.out.println("Scenario 2\n");
        LatLng startPt = new LatLng(49.280184, -123.122728);
        List<User> users = UserUtils.constructSecondTestCase();
        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(startPt);
        List<User> driversWithPassengers = algorithm.findBestRoute(users);

        User driver1 = getDriverWithName(driversWithPassengers, "driver1");
        User driver2 = getDriverWithName(driversWithPassengers, "driver2");
        User driver3 = getDriverWithName(driversWithPassengers, "driver3");

        System.out.println("driv1");
        for (User passenger: driver1.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println("Driv2");
        for (User passenger: driver2.getPassengers()) {
            System.out.println(passenger.getName());
        }

        System.out.println("Driv3");
        for (User passenger: driver3.getPassengers()) {
            System.out.println(passenger.getName());
        }

        System.out.println();

        /*
        assertTrue(driver1.getPassengers().contains(new User("passenger1", null)));
        assertTrue(driver1.getPassengers().contains(new User("passenger4", null)));
        assertTrue(driver2.getPassengers().contains(new User("passenger3", null)));
        assertTrue(driver3.getPassengers().contains(new User("passenger5", null)));
        assertTrue(driver3.getPassengers().contains(new User("passenger2", null)));
        assertFalse(driver1.getPassengers().contains(new User("passenger2", null)));
        assertFalse(driver2.getPassengers().contains(new User("passenger1", null)));
        */

    }

    @Test
    public void testThirdScenario() {

        System.out.println("Scenario 3");
        LatLng startPt = new LatLng(49.246742, -123.001179);
        List<User> users = UserUtils.constructThirdTestCase();

        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(startPt);
        List<User> driversWithPassengers = algorithm.findBestRoute(users);

        User driver1 = getDriverWithName(driversWithPassengers, "driver1");
        User driver2 = getDriverWithName(driversWithPassengers, "driver2");
        User driver3 = getDriverWithName(driversWithPassengers, "driver3l");
        User driver4 = getDriverWithName(driversWithPassengers, "driver4");

        System.out.println("driv1");
        for (User passenger: driver1.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println("Driv2");
        for (User passenger: driver2.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println("Driv3");
        for (User passenger: driver3.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println("Driv4");
        for (User passenger: driver4.getPassengers()) {
            System.out.println(passenger.getName());
        }
        System.out.println();

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
