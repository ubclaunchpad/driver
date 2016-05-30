package com.android.ubclaunchpad.driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPostalCode;
    EditText password1;
    EditText password2;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText password = (EditText) findViewById(R.id.etPassword);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        bRegister = (Button) findViewById(R.id.bSignUp);

        bRegister.setOnClickListener(new View.OnClickListener() {

            // currently doesn't link anywhere on success.
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bSignUp) {
                    password1 = (EditText) findViewById(R.id.etPassword);
                    password2 = (EditText) findViewById(R.id.etPasswordConfirm);

                    if (!password1.equals(password2)) {
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
                    }
                }
            }
        });
    }
}
