package com.android.ubclaunchpad.driver.UI;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Dialog Fragment for when users choose to be Drivers,
 * here they can choose how many passengers they can take
 * <p>
 * author: Mav Cuyugan
 */
public class NumPassengersFragment extends DialogFragment {

    private NumberPicker numPassengerPick;
    private final static String TAG = NumPassengersFragment.class.getSimpleName();


    public NumPassengersFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_num_passengers, null
        );

        numPassengerPick = (NumberPicker) rootView.findViewById(R.id.num_passenger_picker);
        numPassengerPick.setMaxValue(10);
        numPassengerPick.setMinValue(1);
        numPassengerPick.setWrapSelectorWheel(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomizedDialogStyle);

        builder.setView(rootView);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int numPassengers = numPassengerPick.getValue();
                try {
                    UserManager.getInstance().getUser().setSeatNum(numPassengers);
                } catch (Exception e) {
                    Log.e(TAG, "Could not retrieve user" + e.getMessage());
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    Log.d(TAG, "got uid: " + uid);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.isDriverEndpoint).setValue(true);
                    mDatabase.child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.numPassengersEndpoint).setValue(numPassengers);
                }

                // debug toad
                // TODO: delete this when we're able to save this into user object
                Toast.makeText(getContext(),
                        "choosing " + Integer.toString(numPassengers) +
                                (numPassengers > 1 ? " passengers" : " passenger"),
                        Toast.LENGTH_SHORT)
                        .show();

                // TODO: at this point, value chosen by user should be saved in the User object
                // that is accessible everywhere in the app.
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        Button NegativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        NegativeButton.setBackgroundColor(Color.parseColor("#5c505c"));
        NegativeButton.setTextColor(Color.WHITE);

        Button PositiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        PositiveButton.setBackgroundColor(Color.parseColor("#5c505c"));
        PositiveButton.setTextColor(Color.WHITE);

        return alert;
    }

}
