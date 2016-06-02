package com.android.ubclaunchpad.driver.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * User model. Used to serialize and deserialize data to/from
 * POJOs (Plain Old Java Objects).
 * <p/>
 * Created by Chris Li on 6/1/2016.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
