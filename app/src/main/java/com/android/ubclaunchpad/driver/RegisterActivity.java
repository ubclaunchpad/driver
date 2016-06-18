package com.android.ubclaunchpad.driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Java for the Register View, allows user to create an account.
 */


public class RegisterActivity extends AppCompatActivity {

    EditText etName;
    EditText etEmail;
    EditText etPostalCode;
    EditText etStreetAddress;
    EditText password1;
    EditText password2;
    Button bRegister;
    String passwordFirst;
    String passwordSecond;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference users;

    // Create a storage reference from our app
    StorageReference storageRef;

    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPostalCode = (EditText) findViewById(R.id.etPostalCode);
        etStreetAddress = (EditText) findViewById(R.id.etStreetAddress);
        password1 = (EditText) findViewById(R.id.etPassword);
        password2 = (EditText) findViewById(R.id.etPasswordConfirm);

        bRegister = (Button) findViewById(R.id.bSignUp);

        mAuth = FirebaseAuth.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://ubc-driver.appspot.com");
        users = storageRef.child("users");


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(RegisterActivity.this, "Account created.",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }


            }
        };

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


    /**
     * When sign up button is clicked, account is created (if valid) and moves on
     * to the SignInActivity for the user to sign in for the first time.
     * @param view
     */
    public void signUpClick(View view) {

        if (!validateBoxes()) {
            return;
        }

            String email = etEmail.getText().toString();
            String password = password1.getText().toString();
            String name = etName.getText().toString();

        createAccount(email, password);

            // on to next activity (for now just leads back to SignIn)
            Intent nextIntent = new Intent(RegisterActivity.this, SignInActivity.class);
            RegisterActivity.this.startActivity(nextIntent);
    }


    /**
     * Creates account after checking that all edit text's are properly filled.
     * Notifies user if account was or was not created successfully.
     * @param email
     * @param password
     */
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateBoxes()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }



    /**
     * Returns true if all boxes are filled in, false otherwise. If any boxes are empty,
     * alerts user that that edit text is required to be filled.
     * @return
     */
    private boolean noEmptyBoxes() {
        boolean valid = true;

        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            etName.setError("Required.");
            valid = false;
        }

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        }

        String streetAddress = etStreetAddress.getText().toString();
        if (TextUtils.isEmpty(streetAddress)) {
            etStreetAddress.setError("Required.");
            valid = false;
        }

        String postalCode = etPostalCode.getText().toString();
        if (TextUtils.isEmpty(postalCode)) {
            etPostalCode.setError("Required.");
            valid = false;
        }


        passwordFirst = password1.getText().toString();
        if (TextUtils.isEmpty(passwordFirst)) {
            password1.setError("Required.");
            valid = false;
        }

        passwordSecond = password2.getText().toString();
        if (TextUtils.isEmpty(passwordSecond)) {
            password2.setError("Required.");
            valid = false;
        }

        return valid;
    }


    /**
     * Returns true if both password edit text's match, false otherwise.
     * @param passwordFirst
     * @param passwordSecond
     * @return
     */
    private boolean passwordMatch(String passwordFirst, String passwordSecond) {
        boolean valid = true;

        if (!passwordFirst.equals(passwordSecond)) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Uh oh!");
            alertDialog.setMessage("Passwords do not match");
            alertDialog.setButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            password1.setText("");
                            password2.setText("");
                        }
                    });
            alertDialog.show();


            valid = false;
        }

        return valid;

    }


    /**
     * Returns true if all boxes are filled in, and both password fields match.
     * False otherwise.
     * @return
     */
    private boolean validateBoxes() {
        boolean valid = true;
        if (noEmptyBoxes()) {
            if (!passwordMatch(passwordFirst, passwordSecond))
                valid = false;
        } else {
            valid = false;
        }

        return valid;
    }

}
