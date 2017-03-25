package com.android.ubclaunchpad.driver;

import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FindBestRouteAlgorithm;
import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by grigorii on 25/03/17.
 */

public class FindBestRouteUnitTest {


    @Test
    public void testFirstScenario() {
        LatLng start = new LatLng(49.240802, -123.111876);
        Set<User> users = new HashSet<>();

        User driver1 = new User();
        driver1.setName("driver1");
        driver1.setIsDriver(true);
        driver1.setDestination(new LatLng(49.279172, -123.117412));
        driver1.setSeatNum(3);
        driver1.setPassengers(new ArrayList<User>());
        users.add(driver1);

        User driver2 = new User();
        driver2.setName("driver2");
        driver2.setIsDriver(true);
        driver2.setDestination(new LatLng(49.256545, -123.246030));
        driver2.setSeatNum(3);
        driver2.setPassengers(new ArrayList<User>());
        users.add(driver2);

        User driver3 = new User();
        driver3.setName("driver3");
        driver3.setIsDriver(true);
        driver3.setDestination(new LatLng(49.210140, -122.986306));
        driver3.setSeatNum(3);
        driver3.setPassengers(new ArrayList<User>());
        users.add(driver3);


        User passenger1 = new User();
        passenger1.setName("passenger1");
        passenger1.setIsDriver(false);
        passenger1.setDestination(new LatLng(49.00557, -123.000023));
        users.add(passenger1);

        User passenger2 = new User();
        passenger2.setName("passenger2");
        passenger2.setIsDriver(false);
        passenger2.setDestination(new LatLng(49.261279, -123.223467));
        users.add(passenger2);

        User passenger3 = new User();
        passenger3.setName("passenger3");
        passenger3.setIsDriver(false);
        passenger3.setDestination(new LatLng(49.272868, -123.010140));
        users.add(passenger3);

        User passenger4 = new User();
        passenger4.setName("passenger4");
        passenger4.setIsDriver(false);
        passenger4.setDestination(new LatLng(49.206621, -123.113195));
        users.add(passenger4);

        User passenger5 = new User();
        passenger5.setName("passenger5");
        passenger5.setIsDriver(false);
        passenger5.setDestination(new LatLng(49.226209, -123.197249));
        users.add(passenger5);

        User passenger6 = new User();
        passenger6.setName("passenger6");
        passenger6.setIsDriver(false);
        passenger6.setDestination(new LatLng(43.236955, -123.003821));
        users.add(passenger6);

        User passenger7 = new User();
        passenger7.setName("passenger7");
        passenger7.setIsDriver(false);
        passenger7.setDestination(new LatLng(49.255424, -123.083357));
        users.add(passenger7);

        User passenger8 = new User();
        passenger8.setName("passenger8");
        passenger8.setIsDriver(false);
        passenger8.setDestination(new LatLng(49.227240, -123.197014));
        users.add(passenger8);

        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(start);
        List<User> driversWithPassengers = algorithm.findBestRoute(users);

        driver1 = getDriverWithName(driversWithPassengers, driver1.getUserName());
        driver2 = getDriverWithName(driversWithPassengers, driver2.getUserName());
        driver3 = getDriverWithName(driversWithPassengers, driver3.getUserName());
        System.out.println("Scenario 1\n");
        for (User driver: driversWithPassengers) {
            System.out.println(driver.getUserName());
            for (User passenger: driver.getPassengers()) {
                System.out.println(passenger.getUserName());
            }
            System.out.println("\n\n");
        }
    }

    @Test
    public void testSecondScenario() {
        System.out.println("Scenario 21\n");
        LatLng startPt = new LatLng(49.280184, -123.122728);


        User driver1 = buildDriver("driver1", new LatLng(49.261203, -123.243026));
        User driver2 = buildDriver("driver2", new LatLng(49.197418, -123.111362));
        User driver3 = buildDriver("driver3", new LatLng(49.283394, -122.982468));
        User driver4

        User passenger1 = buildPassenger("passenger1", new LatLng(49.264669, -123.255404));
        User passenger2 = buildPassenger("passenger2", new LatLng(49.205305, -123.023442));
        User passenger3 = buildPassenger("passenger3", new LatLng(49.241653, -123.111566));
        User passenger4 = buildPassenger("passenger4", new LatLng(49.257302, -123.153261));
        User passenger5 = buildPassenger("passenger5", new LatLng(49.264669, -123.255404));



        User passen
    }


    public User buildDriver(String name, LatLng destination) {
        User driver = new User();
        driver.setName(name);
        driver.setIsDriver(true);
        driver.setDestination(destination);
        driver.setSeatNum(3);
        driver.setPassengers(new ArrayList<User>());
        return driver;
    }

    public User buildPassenger(String name, LatLng destination) {
        User passenger = new User();
        passenger.setName(name);
        passenger.setIsDriver(false);
        passenger.setDestination(destination);
        return passenger;
    }

    public User getDriverWithName(List<User> drivers, String name) {

        for (User driver: drivers) {
            if (driver.getUserName().equals(name)) {
                return driver;
            }
        }
        Assert.fail("The driver with the given name disappeared");
        return null;
    }

}
