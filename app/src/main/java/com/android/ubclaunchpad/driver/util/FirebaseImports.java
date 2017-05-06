package com.android.ubclaunchpad.driver.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Navjashan on 06/05/2017.
 */

public class FirebaseImports {

    public static DatabaseReference getDatabase(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getFirebaseUser(){
        return getFirebaseAuth().getCurrentUser();
    }

    /*
    public static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    public static final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    */
}
