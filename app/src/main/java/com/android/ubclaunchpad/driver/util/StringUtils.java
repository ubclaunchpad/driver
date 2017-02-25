package com.android.ubclaunchpad.driver.util;

import android.support.annotation.Nullable;

import java.util.regex.Pattern;

/**
 * Utility for handling String matchings.
 */
public class StringUtils {

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

    public static final String FirebaseLatlngEndpoint = "latLng";
    public static final String FirebaseLatEndpoint = "latitude";
    public static final String FirebaseLonEndpoint ="longitude";
    public static final String isDriverEndpoint = "isDriver";
    public static final String numPassengersEndpoint = "seatNum";
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
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
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
