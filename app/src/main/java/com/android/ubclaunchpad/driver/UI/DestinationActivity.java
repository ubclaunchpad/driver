package com.android.ubclaunchpad.driver.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.user.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.user.UserManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity asks the user where is the destination and goes to the passenger/driver
 * screen when "ok" is clicked on
 */
public class DestinationActivity extends BaseMenuActivity implements LocationListener {
    LocationManager mLocationManager;
    Location mLocation;
    PlaceAutocompleteFragment mCurrentAutoCompleteFragment;
    PlaceAutocompleteFragment mDestinationAutocompleteFragment;

    // keeps track of whether or not we saved the location after the user requested it
    boolean shouldSaveLocation = false;

    // shows whether or not we have the value for the current location
    boolean currLocationNull = true;
    // shows whether or not we have a value for the destination location
    boolean destinationNull = true;

    //an int used to check and request permission for the app to access the user's location
    private final static int PERMISSION_REQUEST_FINE = 105;
    private final static String TAG = DestinationActivity.class.getSimpleName();
    private static boolean permission;

    @BindView(R.id.okButton)
    Button okButton;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check for permission
        permission = (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!permission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        ButterKnife.bind(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user;

        try {
            user = UserManager.getInstance().getUser();
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not retrieve user" + e.getMessage());
            //Something went wrong, go back to login
            FirebaseUtils.getFirebaseAuth().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mCurrentAutoCompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.current_autocomplete_fragment);
        mCurrentAutoCompleteFragment.getView().setBackgroundColor(0xe5ffff);

        mDestinationAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);
        mDestinationAutocompleteFragment.getView().setBackgroundColor(0xe5ffff);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move onto the next screen if we have enough information
                if (!currLocationNull && !destinationNull) {
                    goToMainActivity(v);
                }
                // if not, give reminder for user to include the location information
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Empty Fields Detected");
                    builder.setMessage("Please make sure you have indicated where you are " +
                            "and where you want to go.");
                    builder.setCancelable(false);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }
        });

        mCurrentAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());

                okButton.setEnabled(true);

                User innerUser;

                try {
                    innerUser = UserManager.getInstance().getUser();
                    // Save location to user's currentLatLngStr
                    innerUser.setCurrentLatLngStr(place.getLatLng());

                } catch (NullPointerException e) {
                    Log.e(TAG, "Could not retrieve user" + e.getMessage());
                    FirebaseUtils.getFirebaseAuth().signOut();
                    startActivity(new Intent(DestinationActivity.this, LoginActivity.class));
                    finish();
                }

                // store location on Firebase
                if (FirebaseUtils.getFirebaseUser() != null) {
                    String uid = FirebaseUtils.getFirebaseUser().getUid();
                    Log.d(TAG, "got uid: " + uid);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint)
                            .child(uid)
                            .child(StringUtils.FirebaseCurrentLatLngStr)
                            .setValue(StringUtils.latLngToString(place.getLatLng()));
                    // a current location has been indicated by the user
                    currLocationNull = false;
                }
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG, "An error occurred: " + status);
            }
        });

        mDestinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());

                okButton.setEnabled(true);

                // get the singleton User again because user from the outer class may not be initialized
                User innerUser;

                try {
                    innerUser = UserManager.getInstance().getUser();
                    // Save location to user's destinationLatLngStr
                    innerUser.setDestinationLatLngStr(place.getLatLng());

                } catch (NullPointerException e) {
                    Log.e(TAG, "Could not retrieve user" + e.getMessage());
                    //Something went wrong, go back to login
                    FirebaseUtils.getFirebaseAuth().signOut();
                    startActivity(new Intent(DestinationActivity.this, LoginActivity.class));
                    finish();
                }

                if (FirebaseUtils.getFirebaseUser() != null) {
                    String uid = FirebaseUtils.getFirebaseUser().getUid();
                    Log.d(TAG, "got uid: " + uid);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint)
                            .child(uid)
                            .child(StringUtils.FirebaseDestinationLatLngEndpoint)
                            .setValue(StringUtils.latLngToString(place.getLatLng()));
                    // user has indicated destination activity
                    destinationNull = false;
                }
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG, "An error occurred: " + status);
            }
        });
    }


    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void useCurrentLocation(View view) {
        // this means the user clicked the use current location button,
        // and wants the location to be saved again, so we'll set shouldSaveLocation to true
        shouldSaveLocation = true;
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            // get last cached location
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //If not found, try to request a gps update and set it
            if (mLocation != null) {
                // current location has been selected
                currLocationNull = false;
                saveCurrentLocationToFirebase();
                mCurrentAutoCompleteFragment.setText(getString(R.string.autocomplete_your_location));
            }

        } catch (SecurityException e) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
        }
    }

    public void saveCurrentLocationToFirebase() {
        try {
            //Get user and save current lat lng
            User user = UserManager.getInstance().getUser();
            user.setCurrentLatLngStr(StringUtils.latLngToString(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));

            //Firebase user id and save location to firebase
            String uid = FirebaseUtils.getFirebaseUser().getUid();
            mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.FirebaseCurrentLatLngStr).setValue(user.currentLatLngStr);
            // we just saved the location, so even if the location changes
            // we shouldn't update it unless the user taps "Use Current Location" again
            shouldSaveLocation = false;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (shouldSaveLocation) {
            saveCurrentLocationToFirebase();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}