package com.android.ubclaunchpad.driver.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kelvinchan on 2016-10-27.
 */

public class PreferenceHelper {
    private static SharedPreferences sharedPreferences;
    private static PreferenceHelper preferenceHelperInstance;

    /**
     * Initalize the preference helper when the app starts from the Main application
     * This ensure the singleton is created once and can be used throughout the app.
     *
     * @param context - required to create shared preferences
     * @return - an instance of the Preference Helper
     */
    public static PreferenceHelper initInstance(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (preferenceHelperInstance == null) {
            preferenceHelperInstance = new PreferenceHelper();
        }
        return preferenceHelperInstance;
    }

    /**
     * Returns the instance of the Preference Helper created from the application startup
     *
     * @return Instance of the Preference Helper
     * @throws Exception - If the instance was not created proper
     */
    public static PreferenceHelper getPreferenceHelperInstance() throws Exception {
        if (sharedPreferences == null) {
            throw new Exception("Preference Helper needs to be initialized first");
        }
        return preferenceHelperInstance;
    }

    /**
     * Singleton constructor. Should be private to prevent additional instance being created
     */
    private PreferenceHelper() {
    }

    /**
     * Put methods are to write to shared pref
     */
    public void put(String key, String val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public void put(String key, int val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public void put(String key, boolean val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public void put(String key, float val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, val);
        editor.apply();
    }

    public void put(String key, long val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, val);
        editor.apply();
    }

    /**
     * Get methods to retrieve from shared pref
     */

    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    public boolean getBool(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    public float getFloat(String key, float def) {
        return sharedPreferences.getFloat(key, def);
    }

    public long getLong(String key, long def) {
        return sharedPreferences.getLong(key, def);
    }
}
