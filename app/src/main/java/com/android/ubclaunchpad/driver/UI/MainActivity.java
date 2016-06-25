package com.android.ubclaunchpad.driver.UI;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.Exceptions.BluetoothException;
import com.android.ubclaunchpad.driver.util.BluetoothCore;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_TAG = "MAIN ACTIVITY";
    private int REQUEST_ENABLE_BT = 1;
    private boolean mBluetoothProblems = true; //TODO maybe needs to be accessed in application layer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryEnableBluetooth();
        if (!mBluetoothProblems) {
            try {
                BluetoothCore bluetoothCore = new BluetoothCore();
            }
            catch (BluetoothException e){
                Log.d(MAIN_TAG, e.getLocalizedMessage());
            }
        }
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
    private void tryEnableBluetooth(){
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
