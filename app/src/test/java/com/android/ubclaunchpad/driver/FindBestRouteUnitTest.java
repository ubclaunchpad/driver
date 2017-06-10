package com.android.ubclaunchpad.driver;

import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FindBestRouteAlgorithm;
import com.android.ubclaunchpad.driver.util.UserUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.android.ubclaunchpad.driver.util.UserUtils.buildDriver;
import static com.android.ubclaunchpad.driver.util.UserUtils.buildPassenger;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by grigorii on 24/04/17.
 */

public class FindBestRouteUnitTest {

    public static List<User> contructFirstTestScenario() {
        ArrayList<User> users = new ArrayList<>();
        User driver1 = buildDriver("driver1", "49.279172, -123.117412", 3);
        users.add(driver1);
        User driver2 = buildDriver("driver2", "49.256545,-123.246030", 3);
        users.add(driver2);
        User driver3 = buildDriver("driver3", "49.210140,-122.986306", 3);
        users.add(driver3);
        User passenger1 = buildPassenger("passenger1", "49.202280, -123.013378");
        users.add(passenger1);
        User passenger2 = buildPassenger("passenger2", "49.261279,-123.223467");
        users.add(passenger2);
        User passenger3 = buildPassenger("passenger3", "49.272868,-123.010140");
        users.add(passenger3);
        User passenger4 = buildPassenger("passenger4", "49.206621,-123.113195");
        users.add(passenger4);
        User passenger5 = buildPassenger("passenger5", "49.226209,-123.197249");
        users.add(passenger5);
        User passenger6 = buildPassenger("passenger6", "49.236955,-123.003821");
        users.add(passenger6);
        User passenger7 = buildPassenger("passenger7", "49.255424,-123.083357");
        users.add(passenger7);
        User passenger8 = buildPassenger("passenger8", "49.275704, -123.129479");
        users.add(passenger8);
        return users;
    }

    public static List<User> constructSecondTestCase() {
        List<User> users = new ArrayList<>();
        User driver1 = buildDriver("driver1", "49.261203, -123.243026", 3);
        User driver2 = buildDriver("driver2", "49.197418, -123.111362", 3);
        User driver3 = buildDriver("driver3", "49.283394, -122.982468", 3);
        User passenger1 = buildPassenger("passenger1", "49.264669, -123.255404");
        User passenger2 = buildPassenger("passenger2", "49.205305, -123.023442");
        User passenger3 = buildPassenger("passenger3", "49.241653, -123.111566");
        User passenger4 = buildPassenger("passenger4", "49.257302, -123.153261");
        User passenger5 = buildPassenger("passenger5", "49.246742, -123.001179");
        users.add(driver1);
        users.add(driver2);
        users.add(driver3);
        users.add(passenger1);
        users.add(passenger2);
        users.add(passenger3);
        users.add(passenger4);
        users.add(passenger5);
        return users;
    }

    public static List<User> constructThirdTestCase() {

        List<User> users = new ArrayList<>();
        User driver1 = buildDriver("driver1", "49.256920, -123.244176", 4);
        User driver2 = buildDriver("driver2", "49.252701, -123.064430", 3);
        User driver3 = buildDriver("driver3l", "49.289151, -123.134583", 1);
        User driver4 = buildDriver("driver4", "49.287517, -122.988775", 2);

        User passenger1 = buildPassenger("passenger1", "49.219689, -123.165631");
        User passenger2 = buildPassenger("passenger2", "49.252483, -123.161686");
        User passenger3 = buildPassenger("passenger3", "49.215156, -123.047359");
        User passenger4 = buildPassenger("passenger4", "49.284518, -123.041547");
        User passenger5 = buildPassenger("passenger5", "49.238098, -123.106244");
        User passenger6 = buildPassenger("passenger6", "49.269551, -123.095222");
        User passenger7 = buildPassenger("passenger7", "49.268578, -123.242033");

        users.add(driver1);
        users.add(driver2);
        users.add(driver3);
        users.add(driver4);
        users.add(passenger1);
        users.add(passenger2);
        users.add(passenger3);
        users.add(passenger4);
        users.add(passenger5);
        users.add(passenger6);
        users.add(passenger7);

        return users;
    }

    @Test
    public void testFirstScenario() {
        System.out.println("Scenario 1");
        LatLng start = new LatLng(49.240802, -123.111876);
        List<User> users = contructFirstTestScenario();
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
        List<User> users = constructSecondTestCase();
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
        List<User> users = constructThirdTestCase();

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
