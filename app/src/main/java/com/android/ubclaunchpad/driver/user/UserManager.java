package com.android.ubclaunchpad.driver.user;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.ubclaunchpad.driver.util.PreferenceHelper;
import com.android.ubclaunchpad.driver.util.StringUtils;

/**
 * Created by kelvinchan on 2016-10-27.
 */

public class UserManager {
    private static final String TAG = UserManager.class.getSimpleName();

    private User user;
    private static UserManager userManager;

    /**
     * Singleton instance of User manager
     *
     * @return Instance of user manager
     */
    public static UserManager getInstance() {
        if (userManager == null)
            userManager = new UserManager();
        return userManager;
    }

    /**
     * Private constructor for singleton ensure only one instance is created
     */
    private UserManager() {
    }

    /**
     * Returns the user handled by the manager instance
     *
     * @return User
     * @throws Exception if user is not set yet
     */
    public User getUser() throws Exception {
        if (user == null) {
            //TODO get user?
            throw new Exception("User not set");
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Saves a user to the shared prefs of the device. The user is serialized to allow for shared pref save
     *
     * @param currentUser
     */
    public static void saveUserToPref(User currentUser) {
        String userString = currentUser.serializeUser();
        try {
            PreferenceHelper.getPreferenceHelperInstance().put(StringUtils.UserKey, userString);
        } catch (Exception e) {
            Log.e(TAG, "Save user error:" + e.getMessage());
        }
    }

    /**
     * Looks for the shared pref saved user and deserializes it back into User.class
     *
     * @return User
     */
    @Nullable
    public User retrieveUserFromPref() {
        try {
            PreferenceHelper sharedPref = PreferenceHelper.getPreferenceHelperInstance();
            String userString = sharedPref.getString(StringUtils.UserKey, "");
            if (!userString.isEmpty()) {
                return User.createUser(userString);
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve user: " + e.getMessage());
        }

        return null;
    }
}
