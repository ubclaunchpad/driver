package com.android.ubclaunchpad.driver.UI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.BaseMenuActivity;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends BaseMenuActivity {

    Button nameUpdateButton;
    Button passwordUpdateButton;
    Button emailUpdateButton;

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameUpdateButton = (Button) findViewById(R.id.name_update_button);
        passwordUpdateButton = (Button) findViewById(R.id.password_update_button);
        emailUpdateButton = (Button) findViewById(R.id.email_update_button);

        nameEditText = (EditText) findViewById(R.id.profile_name);
        emailEditText = (EditText) findViewById(R.id.profile_email);
        passwordEditText = (EditText) findViewById(R.id.profile_password);

        // TODO reauthenticate

        // set EditText to show the user's name
        String name = "";
        try {
            name = UserManager.getInstance().getUser().getName();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        nameEditText.setText(name);

        // set EditText to show user's email
        FirebaseUser user = FirebaseUtils.getFirebaseUser();
        emailEditText.setText(user.getEmail());


        // set onClickListeners to update the database
        emailUpdateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        nameUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
            }
        });
    }

    private void updateEmail(){
        String email = emailEditText.getText().toString();
        FirebaseUser user = FirebaseUtils.getFirebaseUser();
        String uid = user.getUid();

        // update email auth
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "User email address updated.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "User email address failed to update.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
            // update email in usermanager
            UserManager.getInstance().getUser().setEmail(email);

            // update email in database
            FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(uid).child("email").setValue(email);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateName(){
        String name = nameEditText.getText().toString();
        FirebaseUser user = FirebaseUtils.getFirebaseUser();
        String uid = user.getUid();

        try {
            // update name in user manager
            UserManager.getInstance().getUser().setName(name);

            // update name in database
            FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(uid).child("name").setValue(name);
        } catch (Exception e) {
            Toast.makeText(EditProfileActivity.this, "Your name failed to update.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(EditProfileActivity.this, "Successfully updated name.", Toast.LENGTH_SHORT).show();
    }

    // TODO add fields for nameEditText, emailEditText, passwordEditText to the UI

}
