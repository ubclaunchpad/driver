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
    }

    public void signUpClick(View view) {

        if (!validateBoxes()) {
            return;
        }

            String email = etEmail.getText().toString();
            String password = password1.getText().toString();
            String name = etName.getText().toString();


            // on to next activity (for now just leads back to SignIn)
            Intent nextIntent = new Intent(RegisterActivity.this, SignInActivity.class);
            RegisterActivity.this.startActivity(nextIntent);
    }



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
