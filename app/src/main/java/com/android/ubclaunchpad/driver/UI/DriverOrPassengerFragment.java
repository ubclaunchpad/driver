package com.android.ubclaunchpad.driver.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;

/**
 * Created by sherryuan on 2016-11-05.
 */

public class DriverOrPassengerFragment extends android.support.v4.app.DialogFragment {
    private int REQUEST_ENABLE_BT = 1;
    private Button mPassengerButton;
    private Button mDriverButton;

    private final static String TAG = DriverOrPassengerFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_driver_passenger, null
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(rootView);

        mPassengerButton = (Button) getView().findViewById(R.id.i_am_a_passenger_button);
        mPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is a debug statement, delete this when load screen view is implemented
                Toast.makeText(v.getContext(), "I AM A PASSENGER", Toast.LENGTH_SHORT).show();

                // TODO: at this point, take user to load screen, so they can wait to be matched
            }
        });

        mDriverButton = (Button) getView().findViewById(R.id.i_am_a_driver_button);
        mDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumPassengersFragment numPassengersFragment = new NumPassengersFragment();
                numPassengersFragment.show(getFragmentManager(), numPassengersFragment.getTag());
            }
        });

        return builder.create();
    }
}
