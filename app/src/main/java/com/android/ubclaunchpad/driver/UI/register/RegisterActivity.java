package com.android.ubclaunchpad.driver.UI.register;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.MainActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Java for the Register View, allows user to create an account.
 */

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View{

    EditText mName;
    EditText mEmail;
    EditText mPostalCode;
    EditText mStreetAddress;
    EditText mPassword1;
    EditText mPassword2;
    Button mRegister;

    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        assignValues();

        mRegisterPresenter = new RegisterPresenter(this);
        mRegisterPresenter.onCreate(savedInstanceState);
        //Move to main activity
//        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //remove all activity in stack
//        startActivity(mainIntent);
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

        mRegisterPresenter.signUp(name, email, password1, password2, postalCode, address);

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
    }

    /**
     * Returns true if all boxes are filled in, false otherwise. If any boxes are empty,
     * alerts user that that edit text is required to be filled.
     * @return
     */
    public boolean noEmptyBoxes(String name, String email, String passwordFirst, String passwordSecond) {
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

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {

    }

    @Override
    public User createUser() {
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String postalCode = mPostalCode.getText().toString();
        String address = mStreetAddress.getText().toString();
        return new User(name, email, address, postalCode);
    }

    @Override
    public MainApplication getMainApplication() {
        return (MainApplication) this.getApplicationContext();
    }

    @Override
    public SharedPreferences getSharPref() {
        return getSharedPreferences(StringUtils.FirebaseUidKey, MODE_PRIVATE);
    }

    @Override
    public void showRegistrationError(Task<AuthResult> task) {
        Toast.makeText(RegisterActivity.this, "Authentication failed" + task.getException().getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSingUpError() {
        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPassword() {
        mPassword1.setText("");
        mPassword2.setText("");
    }

    @Override
    public AlertDialog setDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Uh oh!");
        alertDialog.setMessage("Passwords do not match");
        return alertDialog;
    }

    EditText getName(){
        return mName;
    }

    @Override
    public void startMainActivity(){
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //remove all activity in stack
        startActivity(mainIntent);
    }

}
