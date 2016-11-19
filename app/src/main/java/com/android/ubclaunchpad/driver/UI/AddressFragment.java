package com.android.ubclaunchpad.driver.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by sherryuan on 2016-11-05.
 */

public class AddressFragment extends android.support.v4.app.DialogFragment {

    private Button confirmButton;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_address, null
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(rootView);

        confirmButton = (Button) rootView.findViewById(R.id.main_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "confirm button clicked");
                DriverOrPassengerFragment driverOrPassengerFragment = new DriverOrPassengerFragment();
                driverOrPassengerFragment.show(getFragmentManager(), driverOrPassengerFragment.getTag());
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d(TAG, "Place: " + place.getName() + "\nLatLong: " + place.getLatLng());
                try {
                    try {
                        User user = UserManager.getInstance().getUser();
                        if(user != null){
                            user.setAddress(place.getAddress().toString());
                            user.setLatLngAsString(place.getLatLng());
                        }
                    }
                    catch (Exception e){
                        Log.e(TAG, "Could not retrieve user" + e.getMessage());
                    }

                } catch (NullPointerException e){
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });

        return builder.create();
    }
}
