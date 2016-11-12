package com.android.ubclaunchpad.driver.UI;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.login.LoginActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.BluetoothCore;
import com.android.ubclaunchpad.driver.util.WiFiDirectBroadcastReceiver;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_ENABLE_BT = 1;
    private boolean mBluetoothProblems = true; //TODO maybe needs to be accessed in application layer

    private Button mPassengerButton;
    private Button mDriverButton;
    private Button mSessionButton;

    private final static String TAG = MainActivity.class.getSimpleName();

    User user;
    FirebaseAuth mAuth;

    WifiP2pManager mManager;
    WifiP2pManager.PeerListListener myPeerListListener;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothCheck();
        mAuth = FirebaseAuth.getInstance();
        MainApplication app = ((MainApplication)getApplicationContext());
        user = app.getUser();
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

        mSessionButton = (Button) findViewById(R.id.button3);
        final Intent SessionIntent = new Intent(this, SessionActivity.class);                           // session intent
        mSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SessionIntent);
            }
        });

//        mAuth = FirebaseAuth.getInstance();
//        MainApplication app = ((MainApplication)getApplicationContext());
//        user = app.getUser();

        if(user == null){
            //Something went wrong, go back to login
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            mBluetoothProblems = !(resultCode == RESULT_OK);
        }
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void StartWifi(View view){
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method,

            }

            @Override
            public void onFailure(int reason) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
    //            Toast.makeText(MainActivity.this, "Connect failed. Retry.",
    //                    Toast.LENGTH_SHORT).show();

            }
        });

    }



    /**
     * Checks if phone has bluetooth and if it is enabled
     * If not enabled, prompt user to turn on
     */
    private void bluetoothCheck(){
        if(!BluetoothCore.deviceHasBluetooth()){
            // No bluetooth supported
            mBluetoothProblems = true;
            Toast.makeText(this, "WARNING: This device does not support bluetooth," +
                    "Some features may not be available.", Toast.LENGTH_LONG).show();
        }
        else if(!BluetoothCore.isBluetoothEnabled()){
            // prompt user to turn on bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
            mBluetoothProblems = false; //if it passes the two checks, bluetooth is good to go
    }
}
