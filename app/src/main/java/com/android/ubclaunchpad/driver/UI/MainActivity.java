package com.android.ubclaunchpad.driver.UI;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.BluetoothCore;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_ENABLE_BT = 1;
    private boolean mBluetoothProblems = true; //TODO maybe needs to be accessed in application layer

    private Button iAmAPassengerButton;
    private Button iAmADriverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothCheck();

        iAmAPassengerButton = (Button) findViewById(R.id.i_am_a_passenger_button);
        iAmAPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "I AM A PASSENGER", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.how_many_passengers)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.submit_passenger_number)).setVisibility(View.GONE);
            }
        });

        iAmADriverButton = (Button) findViewById(R.id.i_am_a_driver_button);
        iAmADriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.how_many_passengers)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.submit_passenger_number)).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            mBluetoothProblems = !(resultCode == RESULT_OK);
        }
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
