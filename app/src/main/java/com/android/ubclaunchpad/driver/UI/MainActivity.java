package com.android.ubclaunchpad.driver.UI;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.session.SessionActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.i_am_a_passenger_button)
    Button mPassengerButton;
    @BindView(R.id.i_am_a_driver_button)
    Button mDriverButton;
    @BindView(R.id.button3)
    Button mSessionButton;
    @BindView(R.id.sign_out_button)
    Button mSignOutButton;

    private static Context context;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        context = getApplicationContext();


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());
                try {
                    try {
                        User user = UserManager.getInstance().getUser();
                        if (user != null) {
                            user.setCurrentLatLngStr(place.getLatLng());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Could not retrieve user" + e.getMessage());
                    }

                } catch (NullPointerException e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });

        mPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is a debug statement, delete this when load screen view is implemented
                Toast.makeText(v.getContext(), "I AM A PASSENGER", Toast.LENGTH_SHORT).show();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    Log.d(TAG, "got uid: " + uid);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.isDriverEndpoint).setValue(false);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.numPassengersEndpoint).setValue(0);
                }
                // TODO: at this point, take user to load screen, so they can wait to be matched
            }
        });

        mDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment numPassengersFragment = new NumPassengersFragment();
                numPassengersFragment.show(getFragmentManager(), "num_passengers");
//                numPassengersFragment.show(getSupportFragmentManager(), "num_passengers");
            }
        });

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "SIGNING OUT...", Toast.LENGTH_SHORT).show();
                // TODO: discuss what where we should store whether a user is logged in or not
            }
        });


/**
 * TODO this button is for demo purposes. When the UI team comes and changes the UI, please REMOVE
 */

        final Intent SessionIntent = new Intent(this, SessionActivity.class);                           // session intent
        mSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SessionIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public static Context getContext() {
        return MainActivity.context;
    }
}