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

import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Java for the Register View, allows user to create an account.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText mName;
    EditText mEmail;
    EditText mPostalCode;
    EditText mStreetAddress;
    EditText mPassword1;
    EditText mPassword2;
    Button mRegister;

    String passwordFirst;
    String passwordSecond;
    String email;
    String password;
    String name;
    String postalCode;
    String streetAddress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;



    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        assignValues();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    writeNewUser(user.getUid());

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


    /**
     * Creates new user object and writes it to firebase
     * @param userId
     */
    private void writeNewUser(String userId) {
        User user = new User(name, email, streetAddress, postalCode);

        mDatabase.child("Users").child(userId).setValue(user);
    }


    /**
     *  Assigns necessary class values, such as Firebase instance, buttons, and EditTexts
     */
    public void assignValues() {
        // Edit texts and buttons
        mName = (EditText) findViewById(R.id.etName);
        mEmail = (EditText) findViewById(R.id.etEmail);
        mPostalCode = (EditText) findViewById(R.id.etPostalCode);
        mStreetAddress = (EditText) findViewById(R.id.etStreetAddress);
        mPassword1 = (EditText) findViewById(R.id.etPassword);
        mPassword2 = (EditText) findViewById(R.id.etPasswordConfirm);
        mRegister = (Button) findViewById(R.id.bSignUp);

        // String values of user properties
        email = mEmail.getText().toString();
        password = mPassword1.getText().toString();
        name = mName.getText().toString();
        postalCode = mPostalCode.getText().toString();
        streetAddress = mStreetAddress.getText().toString();

        // Firebase values
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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


        createAccount(email, password);

            // on to next activity (for now just leads back to SignIn)
            Intent nextIntent = new Intent(RegisterActivity.this, MainActivity.class);
            RegisterActivity.this.startActivity(nextIntent);
    }


    /**
     * Creates account after checking that all edit text's are properly filled.
     * Notifies user if account was or was not created successfully.
     * @param email
     * @param password
     */
    private void createAccount(final String email, final String password) {
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
                        } else {
                            mAuth.signInWithEmailAndPassword(email, password);
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

        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        }


        passwordFirst = mPassword1.getText().toString();
        if (TextUtils.isEmpty(passwordFirst)) {
            mPassword1.setError("Required.");
            valid = false;
        }

        passwordSecond = mPassword2.getText().toString();
        if (TextUtils.isEmpty(passwordSecond)) {
            mPassword2.setError("Required.");
            valid = false;
        }

        return valid;
    }


    /**
     * Returns true if both password edit texts match, false otherwise.
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
                            mPassword1.setText("");
                            mPassword2.setText("");
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
