package com.android.ubclaunchpad.driver.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;



import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.PreferenceHelper;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A view-less activity in charge of dispatching to the correct
 * activity in order to prevent users from having to log in repeatedly
 * when reopening the app. Note that we do not always have to use the
 * MVP pattern in simple use cases.
 */
public class DispatchActivity extends AppCompatActivity {
    private static final String TAG = DispatchActivity.class.getSimpleName();

    // ToDo: uncomment 'user' value assignment and entire 'if' statement
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final String savedUid;

        //If firebase user is cached on the device, get uid from that
        if(FirebaseUtils.getFirebaseUser() != null) {
            savedUid = FirebaseUtils.getFirebaseUser().getUid();
        }
        else{
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            savedUid = sharedPref.getString(StringUtils.FirebaseUidKey, "");
        }

        if(!savedUid.equals("")){
            FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(savedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        //Add user to Manager
                        UserManager.getInstance().setUser(user);

//                        //It is possible that shared pref is out of sync if firebase user cache is different.
//                        //Safer option to is to re-save
                        try {
                            PreferenceHelper.getPreferenceHelperInstance().put(StringUtils.FirebaseUidKey, savedUid);
                        }
                        catch (Exception e){
                            Log.e(TAG, "Could not save firebase key:" + e.getMessage());
                        }

                        startActivity(new Intent(DispatchActivity.this, DestinationActivity.class));
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
    }
}
