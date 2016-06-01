package com.android.ubclaunchpad.driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    EditText etName;
    EditText etEmail;
    EditText etPostalCode;
    EditText password1;
    EditText password2;
    Button bRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPostalCode = (EditText) findViewById(R.id.etPostalCode);
        password1 = (EditText) findViewById(R.id.etPassword);
        password2 = (EditText) findViewById(R.id.etPasswordConfirm);

        mAuth = FirebaseAuth.getInstance();


        bRegister = (Button) findViewById(R.id.bSignUp);

        bRegister.setOnClickListener(new View.OnClickListener() {

            /**
             *  Currently links back to SignIn on register success, will link it to new and proper activity when made.
             *  Not sure how to store user's postal code in the firebase.
              */

            @Override
            public void onClick(View v) {
                if (!validateBoxes()) {
                    return;
                }

                if (v.getId() == R.id.bSignUp) {
                    String email = etEmail.getText().toString();
                    String password = password1.getText().toString();
                    String name = etName.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password);
                    mAuth.signInWithEmailAndPassword(email, password);

                    // set user's name
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    user.updateProfile(userProfileChangeRequest);

                    // on to next activity (for now just leads back to SignIn)
                    Intent nextIntent = new Intent(RegisterActivity.this, SignInActivity.class);
                    RegisterActivity.this.startActivity(nextIntent);

                }
            }
        });
    }



    private boolean validateBoxes() {
        boolean valid = true;

        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            etName.setError("Required.");
            valid = false;
        } else {
            etName.setError(null);
        }

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String postalCode = etPostalCode.getText().toString();
        if (TextUtils.isEmpty(postalCode)) {
            etPostalCode.setError("Required.");
            valid = false;
        } else {
            etPostalCode.setError(null);
        }


        String passwordFirst = password1.getText().toString();
        if (TextUtils.isEmpty(passwordFirst)) {
            password1.setError("Required.");
            valid = false;
        } else {
            password1.setError(null);
        }

        String passwordSecond = password2.getText().toString();
        if (TextUtils.isEmpty(passwordSecond)) {
            password2.setError("Required.");
            valid = false;
        } else {
            password2.setError(null);
        }


        if (!passwordFirst.equals(passwordSecond)) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Uh oh!");
            alertDialog.setMessage("Passwords do not match");
            alertDialog.setButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            alertDialog.show();
            valid = false;
        }

        return valid;
    }
}
