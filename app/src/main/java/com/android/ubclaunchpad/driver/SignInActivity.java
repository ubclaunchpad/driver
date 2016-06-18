package com.android.ubclaunchpad.driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();

    }


    // Click for "Enter" button
    public void signInClick(View view) {
        signIn(etUsername.getText().toString(), etPassword.getText().toString());
    }

    // Click for "Don't have an account?"
    public void registerClick(View view) {
        Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
        SignInActivity.this.startActivity(registerIntent);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    // Signs in, currently doesn't lead to next activity until we make it
    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password);



         //Leads to next activity upon successful sign in

         if (mAuth.getCurrentUser() != null) {

         Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();

         Intent nextIntent = new Intent(SignInActivity.this, MainActivity.class);
         SignInActivity.this.startActivity(nextIntent);
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
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        }

        return valid;
    }


}
