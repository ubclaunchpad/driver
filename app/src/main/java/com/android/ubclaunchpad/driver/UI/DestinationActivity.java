package com.android.ubclaunchpad.driver.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity asks the user where is the destination and goes to the passenger/driver
 * screen when "ok" is clicked on
 */
public class DestinationActivity extends BaseMenuActivity {
    private final static String TAG = DestinationActivity.class.getSimpleName();
    private static boolean permission;

    @BindView(R.id.okButton)
    Button okButton;

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

        myLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        User user;

        try {
            user = UserManager.getInstance().getUser();

            if (user == null) {
                //Something went wrong, go back to login
                FirebaseUtils.getFirebaseAuth().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve user" + e.getMessage());
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity(v);
            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());

                okButton.setEnabled(true);

                // get the singleton User again because user from the outer class may not be initialized
                User innerUser;

                try {
                    innerUser = UserManager.getInstance().getUser();

                    if (innerUser != null) {
                        innerUser.setDestinationLatLngStr(place.getLatLng());
                    } else {
                        //Something went wrong, go back to login
                        FirebaseUtils.getFirebaseAuth().signOut();
                        startActivity(new Intent(DestinationActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Could not retrieve user" + e.getMessage());
                }

                if (FirebaseUtils.getFirebaseUser() != null) {
                    String uid = FirebaseUtils.getFirebaseUser().getUid();
                    Log.d(TAG, "got uid: " + uid);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint)
                            .child(uid)
                            .child(StringUtils.FirebaseDestinationLatLngEndpoint)
                            .setValue(StringUtils.latLngToString(place.getLatLng()));
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //remove all activity in stack
        startActivity(intent);
    }

    public void useCurrentLocation(View view) {
        try {
            locn = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (locn == null) {
                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }

            User user = UserManager.getInstance().getUser();
            user.setCurrentLatLngStr(StringUtils.latLngToString(new LatLng(locn.getLatitude(), locn.getLongitude())));


            //TODO locn has lat/lan, need to save this to firebase and UserManager?
        } catch (SecurityException e) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            locn = location;

            myLocationManager.removeUpdates(this);
        }
        catch (SecurityException e){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
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
                }

                else {

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




