package com.android.ubclaunchpad.driver;

import android.app.Application;

import com.android.ubclaunchpad.driver.util.PreferenceHelper;
import com.facebook.FacebookSdk;

/**
 * Application class that is alive throughout the entire
 * time of the app's lifecycle. Essentially can be
 * used as a Singleton, can be used to globally
 * initialize instances with application context.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        PreferenceHelper.initInstance(this);
    }


}
