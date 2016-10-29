package com.android.ubclaunchpad.driver.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A view-less activity in charge of dispatching to the correct
 * activity in order to prevent users from having to log in repeatedly
 * when reopening the app. Note that we do not always have to use the
 * MVP pattern in simple use cases.
 */
public class DispatchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    // ToDo: uncomment 'user' value assignment and entire 'if' statement
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startActivity(new Intent(this, PassengerCardView.class));
    }
}
