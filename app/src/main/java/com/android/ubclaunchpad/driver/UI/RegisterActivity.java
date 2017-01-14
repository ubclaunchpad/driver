package com.android.ubclaunchpad.driver.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
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

    private User user;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        assignValues();

        // Firebase values
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();

                    //Create the newly successfully registered user
                    String name = mName.getText().toString();
                    String email = mEmail.getText().toString();
//                    String streetAddress = mStreetAddress.getText().toString();
//                    String postalCode = mPostalCode.getText().toString();
                    User user = new User(name, email);

                    //Set user to UserManager
                    UserManager.getInstance().setUser(user);

                    //Save user to firebase
                    mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).setValue(user);

                    //save user id to shared pref for future auto-login
                    SharedPreferences sharedPref = getSharedPreferences(StringUtils.FirebaseUidKey, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(StringUtils.FirebaseUidKey, uid);
                    editor.apply();

                    //Move to destination activity
                    Intent mainIntent = new Intent(RegisterActivity.this, DestinationActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //remove all activity in stack
                    startActivity(mainIntent);
                }
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!= null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * When sign up button is clicked, account is created (if valid) and moves on
     * to the SignInActivity for the user to sign in for the first time.
     * @param view
     */
    public void signUpClick(View view) {
        String name, email, postalCode, address, password1, password2;
        name = mName.getText().toString();
        email = mEmail.getText().toString();
        postalCode = mPostalCode.getText().toString();
        address = mStreetAddress.getText().toString();
        password1 = mPassword1.getText().toString();
        password2 = mPassword2.getText().toString();

        if (!validateBoxes(name, email, password1, password2)) {
            return;
        }

        createAccount(name, email, postalCode, address, password1);
    }


    /**
     *  Assigns necessary class values, such as Firebase instance, buttons, and EditTexts
     */
    private void assignValues() {
        // Edit texts and buttons
        mName = (EditText) findViewById(R.id.etName);
        mEmail = (EditText) findViewById(R.id.etEmail);
        mPostalCode = (EditText) findViewById(R.id.etPostalCode);
        mStreetAddress = (EditText) findViewById(R.id.etStreetAddress);
        mPassword1 = (EditText) findViewById(R.id.etPassword);
        mPassword2 = (EditText) findViewById(R.id.etPasswordConfirm);
        mRegister = (Button) findViewById(R.id.bSignUp);
    }

    /**
     * Creates account after checking that all edit text's are properly filled.
     * Signs them in and stores their info in the firebase.
     * Notifies user if account was or was not created successfully.
     * @param email
     * @param password
     */
    private void createAccount(final String name, final String email, final String postalCode, final String streetAddress, final String password) {
        Log.d(StringUtils.RegisterActivity, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(StringUtils.RegisterActivity, "RegisterUser:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(StringUtils.RegisterActivity, "ERROR: Could not create Firebase account");
                            Toast.makeText(RegisterActivity.this, "Authentication failed" + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            //if user created successfully, auto login the user to firebase
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(StringUtils.RegisterActivity, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Log.w(StringUtils.RegisterActivity, "signInWithEmail", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }



    /**
     * Returns true if all boxes are filled in, false otherwise. If any boxes are empty,
     * alerts user that that edit text is required to be filled.
     * @return
     */
    private boolean noEmptyBoxes(String name, String email, String passwordFirst, String passwordSecond) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        }

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        }

        if (TextUtils.isEmpty(passwordFirst)) {
            mPassword1.setError("Required.");
            valid = false;
        }

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
    private boolean validateBoxes(String name, String email, String passwordFirst, String passwordSecond) {
        boolean valid = true;
        if (noEmptyBoxes(name, email, passwordFirst, passwordSecond)) {
            if (!passwordMatch(passwordFirst, passwordSecond))
                valid = false;
        } else {
            valid = false;
        }

        return valid;
    }

}
