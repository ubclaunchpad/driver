package com.android.ubclaunchpad.driver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Java for Sign In screen. Signs in user and locally assigns user's info
 */


public class SignInActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private User userSignedIn;

    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mUsername = (EditText) findViewById(R.id.etUsername);
        mPassword = (EditText) findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in. Their user info is grabbed from Firebase and assigned locally
                    // for ease of use

                    final String userId = user.getUid();
                    mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    userSignedIn = dataSnapshot.getValue(User.class);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                }
                            });

                    Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent nextIntent = new Intent(SignInActivity.this, MainActivity.class);
                    SignInActivity.this.startActivity(nextIntent);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }


            }
        };

    }

    // To be used by other activities/classes when getting current user. Could
    // also be grabbed by other ways if decided
    public User getUser() {
        return userSignedIn;
    }

    // Click for "Enter" button
    public void signInClick(View view) {
        signIn(mUsername.getText().toString(), mPassword.getText().toString());
    }

    // Click for "Don't have an account?"
    public void registerClick(View view) {
        Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
        SignInActivity.this.startActivity(registerIntent);
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

        mAuth.signInWithEmailAndPassword(email, password);

         if (mAuth.getCurrentUser() == null) {

         Toast.makeText(getApplicationContext(), "Invalid email/password", Toast.LENGTH_SHORT).show();

         }

    }


    private boolean validateForm() {
        boolean valid = true;


        String email = mUsername.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mUsername.setError("Required.");
            valid = false;
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        }

        return valid;
    }


}
