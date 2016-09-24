package com.android.ubclaunchpad.driver.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button mPassengerButton;
    private Button mDriverButton;

    private final static String TAG = MainActivity.class.getSimpleName();

    User user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });

        mPassengerButton = (Button) findViewById(R.id.i_am_a_passenger_button);
        mPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is a debug statement, delete this when load screen view is implemented
                Toast.makeText(v.getContext(), "I AM A PASSENGER", Toast.LENGTH_SHORT).show();

                // TODO: at this point, take user to load screen, so they can wait to be matched
            }
        });

        mDriverButton = (Button) findViewById(R.id.i_am_a_driver_button);
        mDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment numPassengersFragment = new NumPassengersFragment();
                numPassengersFragment.show(getSupportFragmentManager(), "num_passengers");
            }
        });

        mAuth = FirebaseAuth.getInstance();
        MainApplication app = ((MainApplication)getApplicationContext());
        user = app.getUser();

        if(user == null){
            //Something went wrong, go back to login
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
