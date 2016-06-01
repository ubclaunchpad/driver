package com.android.ubclaunchpad.driver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView signInLink;
    private ProgressDialog pd;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        signInLink = (TextView) findViewById(R.id.bSignIn);

        mAuth = FirebaseAuth.getInstance();

        // Handles authentication changes on UI end
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out

                }
            }
        };


        // On-clicks
        findViewById(R.id.bSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(etUsername.getText().toString(), etPassword.getText().toString());

            }
        });

        findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
                SignInActivity.this.startActivity(registerIntent);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    // Signs in, currently doesn't lead to next activity until we make it
    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }


        // Sign in with email/password
        mAuth.signInWithEmailAndPassword(email, password);

        // On Success, leads to next activity
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();

            /**
             * Leads to next activity, when we make it (Driver/Passenger select?)
             */
            // Intent nextIntent = new Intent(SignInActivity.this, DriverPassengerActivity.class);
            // SignInActivity.this.startActivity(nextIntent);

        } else {
            Toast.makeText(getApplicationContext(), "Invalid email/password", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean validateForm() {
        boolean valid = true;


        String email = etUsername.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etUsername.setError("Required.");
            valid = false;
        } else {
            etUsername.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }


}
