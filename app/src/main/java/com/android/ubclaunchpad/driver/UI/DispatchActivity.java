package com.android.ubclaunchpad.driver.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DispatchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    // ToDo: uncomment 'user' value assignment and entire 'if' statement
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String savedUid;

        //If firebase user is cached on the device, get uid from that
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            savedUid = user.getUid();
        }
        else{
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            savedUid = sharedPref.getString(StringUtils.FirebaseUidKey, "");
        }

        if(!savedUid.equals("")){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(StringUtils.FirebaseUserEndpoint).child(savedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        //Add user to application level
                        MainApplication app = ((MainApplication)getApplicationContext());
                        app.setUser(user);

                        //It is possible that shared pref is out of sync if firebase user cache is different.
                        // Safer option to is to re-save
                        SharedPreferences sharedPref = getSharedPreferences(StringUtils.FirebaseUidKey, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(StringUtils.FirebaseUidKey, savedUid);
                        editor.apply();

                        startActivity(new Intent(DispatchActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(DispatchActivity.this, LoginActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(StringUtils.DispatchActivity, databaseError.getMessage());
                    startActivity(new Intent(DispatchActivity.this, LoginActivity.class));
                }
            });
        }
        //If no user is found
        else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
        startActivity(new Intent(this, TabSelect.class));
    }
}
