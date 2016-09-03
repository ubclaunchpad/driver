package com.android.ubclaunchpad.driver.UI;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_ENABLE_BT = 1;
    private boolean mBluetoothProblems = true; //TODO maybe needs to be accessed in application layer
    private BluetoothSPP bt;

    private Button mPassengerButton;
    private Button mDriverButton;

    private final static String TAG = MainActivity.class.getSimpleName();

    User user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new BluetoothSPP(this);

        bluetoothCheck();

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

        bt.setupService();
        bt.startService(BluetoothState.DEVICE_ANDROID);

        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);



        mAuth = FirebaseAuth.getInstance();
        MainApplication app = ((MainApplication)getApplicationContext());
        user = app.getUser();

//        if(user == null){
//            //Something went wrong, go back to login
//            mAuth.signOut();
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            mBluetoothProblems = !(resultCode == RESULT_OK);
        } else if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                data.getExtras();
                bt.connect(data);
            }
        }
    }

    /**
     * Checks if phone has bluetooth and if it is enabled
     * If not enabled, prompt user to turn on
     */
    private void bluetoothCheck(){
        if(!bt.isBluetoothAvailable()) {
            mBluetoothProblems = true;
            Toast.makeText(this, "WARNING: This device does not support bluetooth," +
                    "Some features may not be available.", Toast.LENGTH_LONG).show();
        } else if(!bt.isBluetoothEnabled()) {
            // prompt user to turn on bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mBluetoothProblems = false; //if it passes the two checks, bluetooth is good to go
        }
    }
}
