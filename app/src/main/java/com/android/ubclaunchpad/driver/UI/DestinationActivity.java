package com.android.ubclaunchpad.driver.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This activity asks the user where is the destination and goes to the passenger/driver
 * screen when "ok" is clicked on
 */
public class DestinationActivity extends AppCompatActivity {
    private final static String TAG = DestinationActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        User user;

        try {
             user = UserManager.getInstance().getUser();

            if (user == null) {
                //Something went wrong, go back to login
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
        catch (Exception e){
            Log.e(TAG, "Could not retrieve user" + e.getMessage());

        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());

                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    Log.d(TAG, "got uid: " + uid);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint)
                            .child(uid)
                            .child(StringUtils.FirebaseLatlngEndpoint)
                            .child(StringUtils.FirebaseLatEndpoint)
                            .setValue(latitude);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint)
                            .child(uid)
                            .child(StringUtils.FirebaseLatlngEndpoint)
                            .child(StringUtils.FirebaseLonEndpoint)
                            .setValue(longitude);
                }

            }

            @Override
            public void onError(Status status) {
                Log.d(TAG, "An error occurred: " + status);
            }
        });

    }

    public void goToMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //remove all activity in stack
        startActivity(intent);
    }
}