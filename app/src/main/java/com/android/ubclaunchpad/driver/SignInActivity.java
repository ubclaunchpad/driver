package com.android.ubclaunchpad.driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Firebase.setAndroidContext(this);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView signInLink = (TextView) findViewById(R.id.bSignIn);

        final Firebase firebaseApp = new Firebase("driver-7987b.firebaseio.com");

        signInLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                firebaseApp.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        
                        // will change this to an intent leading to next page
                        AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
                        alertDialog.setTitle("Success!");
                        alertDialog.setMessage("You're logged in");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                       //goes away
                                    }
                                });
                        alertDialog.show();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
                        alertDialog.setTitle("Wrong credentials!");
                        alertDialog.setMessage("You're not logged in");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //goes away
                                    }
                                });
                        alertDialog.show();
                    }
                });

            }
        });


    }
}
