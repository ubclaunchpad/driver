package com.android.ubclaunchpad.driver.util;

/**
 * Created by grigorii on 24/04/17.
 */

/**
 * Created by grigorii on 09/03/17.
 */
import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The algorithm finds a number of passengers for each driver determined by
 * the driver's car capacity. This is done by assigning a "score" for each passenger relative
 * to each driver, where the score is determined by how much longer the driver's trip becomes if we assign
 * a passenger to this driver.
 */

public class FindBestRouteAlgorithm {

    private LatLng startPt;
    private List<User> passengers;
    private List<User> drivers;

    // Takes starting point for everybody as an argument
    public FindBestRouteAlgorithm(LatLng startPt) {
        this.startPt = startPt;
    }
    private void sortUsers(List<User> users) {
        drivers = new ArrayList<>();
        passengers = new ArrayList<>();
        for (User user: users) {
            if (user.isDriver()) drivers.add(user);
            else passengers.add(user);
        }
    }
    /**
     * Starting method for the route search. We set up a starting point as
     * (0,0) in Cartesian coordinate system
     * TODO: TOO MANY LOOPS!
     * @param users a list of drivers a passengers to optimize the journey for
     * @return a list of routes for each driver
     */
    public List<User> findBestRoute(List<User> users) {
        sortUsers(users);
        // Map<Passenger, Map<Driver, Distance>>
        Map<User, Map<User, Double>> distanceMaps = new HashMap<>();
        // Setting up startPt as (0,0)
        Map<User, LatLng> driverCoords = new HashMap<>();
        for (User driver: drivers) {
            driverCoords.put(driver, new LatLng(StringUtils.stringToLatLng(driver.getDestinationLatLngStr()).longitude - startPt.longitude,
                    StringUtils.stringToLatLng(driver.getDestinationLatLngStr()).latitude - startPt.latitude));
        }
        Map<User, LatLng> passengerCoords = new HashMap<>();
        for (User passenger: passengers) {
            passengerCoords.put(passenger, new LatLng(StringUtils.stringToLatLng(passenger.getDestinationLatLngStr()).longitude - startPt.longitude,
                    StringUtils.stringToLatLng(passenger.getDestinationLatLngStr()).latitude - startPt.latitude));
        }
        for (Map.Entry<User, LatLng> passengerWithCoord : passengerCoords.entrySet()) {
            //
            Map<User, Double> distances = new HashMap<>();
            for (Map.Entry<User, LatLng> driverWithCoords : driverCoords.entrySet()) {
                Double distance = distanceBetweenLineAndPoint(driverWithCoords.getValue(), passengerWithCoord.getValue());
                distances.put(driverWithCoords.getKey(), distance);
            }
            distanceMaps.put(passengerWithCoord.getKey(), distances);
        }
        assignPassengersToDrivers(distanceMaps);
        return drivers;
    }
    /**
     * Find the distance between a point and a line
     * @param drDestPt destination of a driver
     * @param passDestPt destination of a point
     * @return distance between passDest and a line between start and drDest, and a point passDest
     */
    private double distanceBetweenLineAndPoint(LatLng drDestPt, LatLng passDestPt) {
        Double drDestX = drDestPt.longitude;
        Double drDestY = drDestPt.latitude;
        Double passDestX = passDestPt.longitude;
        Double passDestY = passDestPt.latitude;
        Double distanceToLine = Double.MAX_VALUE;
        Double slope = drDestY/drDestX;
        Double perpLineSlope = -1/slope;
        Double startLineIntercept = perpLineSlope * passDestX;
        Double driverLineIntercept = perpLineSlope * passDestX + drDestY;
        if (drDestY >= 0) {
            if (startLineIntercept <= passDestY && driverLineIntercept >= passDestY) {
                distanceToLine = Math.abs(-slope * passDestX + passDestY) / Math.sqrt(slope * slope + 1);
            }
        } else {
            if (startLineIntercept >= passDestY && driverLineIntercept <= passDestY) {
                distanceToLine = Math.abs(-slope * passDestX + passDestY) / Math.sqrt(slope * slope + 1);
            }
        }
        Double distanceToFinish = Math.sqrt((drDestX - passDestX)*(drDestX - passDestX) + (drDestY - passDestY)*(drDestY - passDestY));
        return Math.min(distanceToLine, distanceToFinish);
    }
    /**
     * It literally does what its name says
     * @param distanceMaps a map containing passenger as a key and map containing
     *                     driver-distance to this driver for this passenger
     */
    private Map<String, ArrayList<String>> assignPassengersToDrivers(Map<User, Map<User, Double>> distanceMaps) {
        // Iterate over entries of distanceMaps
        Map<String, ArrayList<String>> driverPassengersMap = new HashMap<>();
        for (Map.Entry<User, Map<User, Double>> entry : distanceMaps.entrySet()) {
            User currentPassenger = entry.getKey();
            Map<User, Double> driverDistanceMatrix = entry.getValue();
            User optimalDriver = null;
            Double smallestDistance = Double.MAX_VALUE;
            // Find the closest driver for currentPassenger
            for (Map.Entry<User, Double> driverDistanceEntry : driverDistanceMatrix.entrySet()) {
                User currentDriver = driverDistanceEntry.getKey();
                Double currentDistance = driverDistanceEntry.getValue();
                if (currentDistance < smallestDistance && currentDriver.getNumFreeSeats() > 0) {
                    smallestDistance = currentDistance;
                    optimalDriver = currentDriver;
                }
            }
            optimalDriver.addPassenger(currentPassenger);
        }
        return driverPassengersMap;
    }
}
