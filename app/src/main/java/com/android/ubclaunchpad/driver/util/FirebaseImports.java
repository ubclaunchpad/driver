package com.android.ubclaunchpad.driver.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Navjashan on 06/05/2017.
 */

public class FirebaseImports {

    public static DatabaseReference getDatabase(){ return FirebaseDatabase.getInstance().getReference(); }

    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getFirebaseUser(){
        return getFirebaseAuth().getCurrentUser();
    }
}
