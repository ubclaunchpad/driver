package com.android.ubclaunchpad.driver.util;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Pattern;

/**
 * Utility for handling String matchings.
 */
public class StringUtils {

    /**
     * Names for intent extras
     */
    public static final String SESSION_NAME = "SessionName";

    /**
     * Log Tags
     */
    public static final String DispatchActivity = "Dispatch";
    public static final String RegisterActivity = "Register";
    public static final String SignInActivity = "SignIn";

    /**
     * Firebase endpoints (children)
     */
    public static final String FirebaseUserEndpoint = "Users";
    public static final String FirebaseSessionEndpoint = "Session group";
    public static final String FirebaseDestinationLatLngEndpoint = "destinationLatLng";
    public static final String FirebaseCurrentLatLng = "currentLatLng";
    public static final String isDriverEndpoint = "isDriver";
    public static final String numPassengersEndpoint = "seatNum";

    public static final String FirebaseSessionDriverEndpoint = "drivers";
    public static final String FirebaseSessionPassengerEndpoint = "passengers";
    /**
     * Shared pref keys
     * TODO turn to enum
     */
    public static final String FirebaseUidKey = "FirebaseUidKey";
    public static final String UserKey = "UserKey";

    /**
     * Email validation pattern.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * Checks if input is a valid email address.
     *
     * @param email The email to validate.
     * @return {@code true} if the input is a valid email. {@code false} otherwise.
     */
    public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }


    /**
     * Returns true if the string is null or 0-length.
     * Note: Copied from the Android framework's TextUtils to facilitate testing.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String latLngToString(LatLng latLng) {
        Double lat = latLng.latitude;
        Double lng = latLng.longitude;
        return lat.toString() + "," + lng.toString();
    }

    public static LatLng stringToLatLng(String latLng) {
        // String has the format "latitude,longitude"
        String[] latLngArray = latLng.split(",");
        return new LatLng(
                Double.parseDouble(latLngArray[0]),
                Double.parseDouble(latLngArray[1]));
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.
     * Note: Copied from the Android framework's TextUtils to facilitate testing.
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }
}
