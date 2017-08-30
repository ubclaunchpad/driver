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
    // local storage of what the user's current location LatLng is
    LatLng mCurrLoc = null;
    // local storage of what the user's destination location LatLng is
    LatLng mDestLoc = null;


    // keeps track of whether or not current location listener should be used
    boolean shouldUseLocationListener = false;

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

            if (user == null) {
                // Something went wrong, go back to login
                FirebaseUtils.getFirebaseAuth().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve user" + e.getMessage());
        }

        mCurrentAutoCompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.current_autocomplete_fragment);

        mDestinationAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // get the user and set the location information
                    User user = UserManager.getInstance().getUser();
                    // makes sure that the user exists in Firebase and user manager
                    if (user != null && FirebaseUtils.getFirebaseUser() != null) {
                        // move onto the next screen if both the inputs are filled in when button is clicked
                        if (mCurrLoc != null && mDestLoc != null) {
                            // update user information on user manager
                            user.setCurrentLatLngStr(StringUtils.latLngToString(mCurrLoc));
                            user.setDestinationLatLngStr(StringUtils.latLngToString(mDestLoc));

                            // get Firebase user and save the location information
                            String uid = FirebaseUtils.getFirebaseUser().getUid();
                            Log.d(TAG, "got uid: " + uid);
                            // save to current location endpoint
                            mDatabase.child(StringUtils.FirebaseUserEndpoint)
                                    .child(uid)
                                    .child(StringUtils.FirebaseCurrentLatLngStr)
                                    .setValue(StringUtils.latLngToString(mCurrLoc));
                            // save to destination endpoint
                            mDatabase.child(StringUtils.FirebaseUserEndpoint)
                                    .child(uid)
                                    .child(StringUtils.FirebaseDestinationLatLngEndpoint)
                                    .setValue(StringUtils.latLngToString(mDestLoc));
                            goToMainActivity(v);
                        }

                        // if not, give alert to remind user to include the location information
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
                    }else {
                        // something went wrong with grabbing the user, go back to login
                        FirebaseUtils.getFirebaseAuth().signOut();
                        startActivity(new Intent(DestinationActivity.this, LoginActivity.class));
                        finish();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // listener for selecting current location
        mCurrentAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());
                okButton.setEnabled(true);
                // since a current location place has been selected, current location listener
                // would not need to update mLocation on location changes, so we set
                // shouldUseLocationListener to false
                shouldUseLocationListener = false;
                // save the selected place as the current location locally
                mCurrLoc = place.getLatLng();
            }
                @Override
                public void onError (Status status){
                    Log.d(TAG, "An error occurred: " + status);
                }
        });

        // listener for deselecting current location
        mCurrentAutoCompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // since current location has been deselected, we set shouldUseLocationListener to
                // true so we can update mLocation on location changes
                mCurrentAutoCompleteFragment.setText("");
                mCurrentAutoCompleteFragment.setHint(getText(R.string.autocomplete_search));
                mCurrLoc = null;
            }
        });

        // listener for selecting a current location
        mDestinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());

                okButton.setEnabled(true);
                // save the selected place as destination location locally
                mDestLoc = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG, "An error occurred: " + status);
            }
        });

        // listener for deselecting destination
        mDestinationAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDestinationAutocompleteFragment.setText("");
                mDestinationAutocompleteFragment.setHint(getText(R.string.autocomplete_search));
                mDestLoc = null;
            }
        });
    }


    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void useCurrentLocation(View view) {

        // this means the user clicked the use current location button,
        // and wants the location listener to be used, so we'll set shouldUseLocationListener to true
        shouldUseLocationListener = true;
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            // get last cached location
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //If not found, try to request a gps update and set it
            if (mLocation != null) {
                // save the current location as a LatLng locally
                mCurrLoc = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                Log.d(TAG, "Current LatLng is " + "");
                mCurrentAutoCompleteFragment.setText("");
                mCurrentAutoCompleteFragment.setHint(getText(R.string.autocomplete_your_location));
            }

        } catch (SecurityException e) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (shouldUseLocationListener) {
            mLocation = location;
            mCurrLoc = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
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