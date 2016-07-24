package com.android.ubclaunchpad.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.ubclaunchpad.driver.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    User user;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        MainApplication app = ((MainApplication)getApplicationContext());
        user = app.getUser();

        if(user == null){
            //Something went wrong, go back to login
            mAuth.signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
