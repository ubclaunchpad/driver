package com.android.ubclaunchpad.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A view-less activity in charge of dispatching to the correct
 * activity in order to prevent users from having to log in repeatedly
 * when reopening the app. Note that we do not always have to use the
 * MVP pattern in simple use cases.
 */
public class DispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //    if (user != null) {
            // User is signed in
            startActivity(new Intent(this, MainActivity.class));
    //    } else {
            // No user is signed in
    //        startActivity(new Intent(this, LoginActivity.class));
    //    }
    //    finish();
    }
}
