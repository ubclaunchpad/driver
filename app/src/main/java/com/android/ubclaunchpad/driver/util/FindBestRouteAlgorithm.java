package com.android.ubclaunchpad.driver.util;

/**
 * Created by grigorii on 24/04/17.
 */


import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The algorithm finds a number of passengers for each driver determined by
 * the driver's car capacity. This is done by assigning a "score" for each passenger relative
 * to each driver, where the score is determined by how much longer the driver's trip becomes if we assign
 * a passenger tod this driver.
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
                Double influence = influenceToCurrentRoute(driverWithCoords.getKey(), passengerWithCoord.getKey());

                Double distance = distanceBetweenLineAndPoint(driverWithCoords.getValue(), passengerWithCoord.getValue())*influence;
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
        /*
         * Finding slope of a line from starting point to driver's destination and
         * slope of a line perpendicular to the above one.
        */
        /*
        Double slope = drDestY/drDestX;
        Double perpLineSlope = -1/slope;


        Double startLineIntercept = perpLineSlope * passDestX;
        Double driverLineIntercept = perpLineSlope * passDestX + drDestY;

        // Check if we are in upper half-space
        if (drDestY >= 0) {
            // Check if destination of passenger is in the region where calculating the
            // perpendicular distance is feasible
            if (startLineIntercept <= passDestY && driverLineIntercept >= passDestY) {
                distanceToLine = Math.abs(-slope * passDestX + passDestY) / Math.sqrt(slope * slope + 1);
            }
        // Lower half-space
        } else {
            // Check if destination of passenger is in the region where calculating the
            // perpendicular distance is feasible
            if (startLineIntercept >= passDestY && driverLineIntercept <= passDestY) {
                distanceToLine = Math.abs(-slope * passDestX + passDestY) / Math.sqrt(slope * slope + 1);
            }
        }

        Double distanceToFinish = eucledianDistance(drDestX, drDestY, passDestX, passDestY);

        Double finalDistance = Math.min(distanceToFinish, distanceToLine);
        */
        Double distanceToFinish = eucledianDistance(drDestX, drDestY, passDestX, passDestY);
        return distanceToFinish;
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
            // #AngryDebugging
            if (optimalDriver == null) {
                System.out.println("fuck");
            }
            optimalDriver.addPassenger(currentPassenger);
        }
        return driverPassengersMap;
    }

    /**
     * New heuristic. This method measures the influence of adding new destination point to drivers route.
     * First, it calculates the current overall distance that the driver has to travel based on already
     * added passengers. Then, it adds a new passenger to this route and computes the ratio between the
     * old distance and new distance, which acts as a "score" for each particular added passenger.
     * @param driver Current driver
     * @param passenger passenger to add to the driver's route
     * @return
     */
    private double influenceToCurrentRoute(User driver, User passenger) {

        List<LatLng> trip = constructDrivingSequence(driver);
        double distance = findRouteLength(trip);
        LatLng newLatLng = StringUtils.stringToLatLng(passenger.getDestinationLatLngStr());
        trip.add(findIndexOfClosestLatLng(trip, newLatLng), newLatLng);
        double newDistance = findRouteLength(trip);

        return newDistance / distance;
    }

    /**
     * Construct the driving route for the driver with assigned passengers.
     * Construction of the route is based on adding the closest passenger to each
     * consequent node.
     * @param driver with assigned passengers
     * @return the shortest route for current list of passengers
     */
    private List<LatLng> constructDrivingSequence(User driver) {

        LatLng currentTripPoint = startPt;
        Set<LatLng> passengerDestinations = new HashSet<>();
        List<LatLng> trip = new ArrayList<>();
        trip.add(startPt);

        // Store destinations of all passengers
        for (User user: driver.getPassengers()) {
            passengerDestinations.add(StringUtils.stringToLatLng(user.getDestinationLatLngStr()));
        }

        while (!passengerDestinations.isEmpty()) {

            LatLng closestPtToCurrentTripPt = new LatLng(0, 0);
            Double closestDistanceToCurrentTripPt = Double.MAX_VALUE;
            for (LatLng latLng : passengerDestinations) {

                Double currentDistance = eucledianDistance(currentTripPoint.longitude, currentTripPoint.latitude, latLng.longitude, latLng.latitude);
                if (currentDistance < closestDistanceToCurrentTripPt) {
                    closestDistanceToCurrentTripPt = currentDistance;
                    closestPtToCurrentTripPt = latLng;
                }
            }
            trip.add(closestPtToCurrentTripPt);
            passengerDestinations.remove(closestPtToCurrentTripPt);
        }

        trip.add(StringUtils.stringToLatLng(driver.getDestinationLatLngStr()));

        return trip;
    }

    /**
     * Sum of eucledian distances between consequent points
     * @param route
     * @return the resulting distance
     */
    private double findRouteLength(List<LatLng> route) {

        double distance = 0;

        for (int i = 1; i < route.size(); i++) {
            distance += eucledianDistance(
                    route.get(i).longitude,
                    route.get(i).latitude,
                    route.get(i-1).longitude,
                    route.get(i-1).latitude);
        }

        return distance;
    }

    /**
     * Given newPt, find the closest one to it from the currentTrip list
     * @param currentTrip
     * @param newPt
     * @return the closest point to newPt from currentTrip
     */
    private int findIndexOfClosestLatLng(List<LatLng> currentTrip, LatLng newPt) {

        Double distance = Double.MAX_VALUE;
        int smallestDistanceIndex = Integer.MAX_VALUE;

        for (int i = 0; i < currentTrip.size(); i++) {

            if (eucledianDistance(newPt.longitude, newPt.latitude, currentTrip.get(i).longitude, currentTrip.get(i).latitude) < distance) {
                smallestDistanceIndex = i;
            }
        }
        return smallestDistanceIndex;
    }

    /**
     * @param x_1 longitude of first pt
     * @param y_1 latitude of first pt
     * @param x_2 longitude of second pt
     * @param y_2 latitude of second pt
     * @return an Eucledian distance between points with given coordinates
     */
    private double eucledianDistance(double x_1, double y_1, double x_2, double y_2) {

        return Math.sqrt((x_1 - x_2)*(x_1 - x_2) + (y_1 - y_2)*(y_1 - y_2));
    }

}

