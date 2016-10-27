package com.android.ubclaunchpad.driver.util;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.ubclaunchpad.driver.models.User;

/**
 * Created by kelvinchan on 2016-10-27.
 */

public class UserManager {
    private static final String TAG = UserManager.class.getSimpleName();

    private User user;
    private static UserManager userManager;

    public static UserManager getInstance() {
        if(userManager == null)
            userManager = new UserManager();
        return userManager;
    }

    private UserManager(){}

    public User getUser() throws Exception{
        if(user == null){
            //TODO get user
            throw new Exception("User not set");
        }
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void saveUserToPref(User currentUser){
        String userString = currentUser.serializeUser();
        try {
            PreferenceHelper.getPreferenceHelperInstance().put(StringUtils.UserKey, userString);
        }
        catch (Exception e){
            Log.e(TAG, "Save user error:" + e.getMessage());
        }
    }

    @Nullable
    public User retrieveUserFromPref(){
        try {
            PreferenceHelper sharedPref = PreferenceHelper.getPreferenceHelperInstance();
            String userString = sharedPref.getString(StringUtils.UserKey, "");
            if(!userString.isEmpty()){
                return User.createUser(userString);
            }
        }
        catch (Exception e){
            Log.e(TAG, "Could not retrieve user: " + e.getMessage());
        }

        return null;
    }
}
