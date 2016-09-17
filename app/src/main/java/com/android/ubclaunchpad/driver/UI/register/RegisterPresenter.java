package com.android.ubclaunchpad.driver.UI.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.MainActivity;
import com.android.ubclaunchpad.driver.UI.login.LoginContract;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

/**
 * Created by Marina on 10.09.16.
 */
public class RegisterPresenter implements RegisterContract.Presenter {


    private User user;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    private final RegisterContract.View mRegisterView;

    public RegisterPresenter(@NonNull RegisterContract.View exampleView) {
        mRegisterView = exampleView;
        mRegisterView.setPresenter(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
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

                    User user = mRegisterView.createUser();

                    //Set user to application layer
                    MainApplication app = mRegisterView.getMainApplication();
                    app.setUser(user);

                    //Save user to firebase
                    mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).setValue(user);

                    //save user id to shared pref for future auto-login
                    SharedPreferences sharedPref = mRegisterView.getSharPref();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(StringUtils.FirebaseUidKey, uid);
                    editor.apply();


                    //Move to main activity
                    mRegisterView.startMainActivity();
                }
            }
        };

    }

    @Override
    public void onStart(){
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        if(mAuthListener!= null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /**
     * When sign up button is clicked, account is created (if valid) and moves on
     * to the SignInActivity for the user to sign in for the first time.
     */
    public void signUp(String name, String email, String password1, String password2, String postalCode, String address) {

        if (!validateBoxes(name, email, password1, password2)) {
            return;
        }

        createAccount(name, email, postalCode, address, password1);
    }




    /**
     * Creates account after checking that all edit text's are properly filled.
     * Signs them in and stores their info in the firebase.
     * Notifies user if account was or was not created successfully.
     * @param email
     * @param password
     */
    public void createAccount(final String name, final String email, final String postalCode, final String streetAddress, final String password) {
        Log.d(StringUtils.RegisterActivity, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(StringUtils.RegisterActivity, "RegisterUser:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(StringUtils.RegisterActivity, "ERROR: Could not create Firebase account");
//                            Toast.makeText(RegisterActivity.this, "Authentication failed" + task.getException().getLocalizedMessage(),
//                                    Toast.LENGTH_SHORT).show();
                            mRegisterView.showRegistrationError(task);

                        } else {
                            //if user created successfully, auto login the user to firebase
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(StringUtils.RegisterActivity, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Log.w(StringUtils.RegisterActivity, "signInWithEmail", task.getException());
                                        mRegisterView.showSingUpError();
                                    }
                                }
                            });
                        }
                    }

                });
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

            AlertDialog alertDialog = mRegisterView.setDialog();

            alertDialog.setButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            mRegisterView.showPassword();
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
        if (mRegisterView.noEmptyBoxes(name, email, passwordFirst, passwordSecond)) {
            if (!passwordMatch(passwordFirst, passwordSecond))
                valid = false;
        } else {
            valid = false;
        }

        return valid;
    }

    @Override
    public void start() {

    }
}
