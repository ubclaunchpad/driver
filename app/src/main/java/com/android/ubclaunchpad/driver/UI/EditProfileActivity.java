package com.android.ubclaunchpad.driver.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.user.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends BaseMenuActivity {
    @BindView(R.id.name_update_button) Button nameUpdateButton;
    @BindView(R.id.password_update_button) Button passwordUpdateButton;
    @BindView(R.id.email_update_button) Button emailUpdateButton;

    @BindView(R.id.profile_name) EditText nameEditText;
    @BindView(R.id.profile_email) EditText emailEditText;
    @BindView(R.id.profile_password) EditText passwordEditText;

    // this method hides "edit profile" option from the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_edit_profile);
        item.setVisible(false);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(this);

        // set EditText to show user's name
        String name = "";
        try {
            name = UserManager.getInstance().getUser().getName();
        }
        catch(Exception e){ // exception thrown by getUser if user is null
            e.printStackTrace();
        }
        nameEditText.setText(name);

        // set EditText to show user's email
        FirebaseUser user = FirebaseUtils.getFirebaseUser();
        if(user != null) emailEditText.setText(user.getEmail());

        // set onClickListeners to helpers that update the database
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
        passwordUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    // email updated in 3 places: UserManager, FirebaseAuth, and Firebase database
    private void updateEmail(){
        String email = emailEditText.getText().toString();
        FirebaseUser user = FirebaseUtils.getFirebaseUser();
        String uid = user.getUid();

        // update auth
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
            FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.FirebaseEmailEndpoint).setValue(email);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // name updated in 2 places: user manager, and firebase database
    private void updateName(){
        String name = nameEditText.getText().toString();
        FirebaseUser user = FirebaseUtils.getFirebaseUser();
        String uid = user.getUid();

        try {
            // update name in user manager
            UserManager.getInstance().getUser().setName(name);

            // update name in database
            FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.FirebaseNameEndpoint).setValue(name);
            Toast.makeText(EditProfileActivity.this, "Successfully updated name.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(EditProfileActivity.this, "Your name failed to update.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // password updated only in Firebase authentication
    private void updatePassword(){
        String newPassword = passwordEditText.getText().toString();
        FirebaseUser user = FirebaseUtils.getFirebaseAuth().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "User password updated.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(EditProfileActivity.this, "User password failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}