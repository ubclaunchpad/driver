package com.android.ubclaunchpad.driver.util;

import android.bluetooth.BluetoothAdapter;

import com.android.ubclaunchpad.driver.models.Exceptions.BluetoothException;

/**
 * Created by Kelvin on 2016-06-25.
 */
public class BluetoothCore {
    public BluetoothAdapter mBluetoothAdapter;

    public BluetoothCore() throws BluetoothException {
        if(isBluetoothEnabled()){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    //getters and setters
    public BluetoothAdapter getmBluetoothAdapter(){ return mBluetoothAdapter; }
    public void setmBluetoothAdapter(BluetoothAdapter adapter) { this.mBluetoothAdapter = adapter; }

    //static methods
    /**
     * Gets the main adapter for bluetooth
     * @return the devices bluetooth adaptor, NULL if device has no bluetooth capibilities
     */
    public static boolean deviceHasBluetooth(){
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    /**
     * Checks that bluetooth is supported by the device and if it is turned on
     * @return true if device has bluetooth turned on, false if not/not exist
     */
    public static boolean isBluetoothEnabled() {
        return deviceHasBluetooth() && BluetoothAdapter.getDefaultAdapter().isEnabled();
    }
}
