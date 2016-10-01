package com.android.ubclaunchpad.driver;

import android.app.Application;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.ubclaunchpad.driver.models.User;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Application class that is alive throughout the entire
 * time of the app's lifecycle. Essentially can be
 * used as a Singleton, can be used to globally
 * initialize instances with application context.
 */
public class MainApplication extends Application {

    private FirebaseAuth mAuth;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

    }

    public User getUser(){
        return user;
    }

    public void setUser(User u){
        user = u;
    }


}
