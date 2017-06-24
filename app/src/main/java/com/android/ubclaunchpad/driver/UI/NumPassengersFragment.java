package com.android.ubclaunchpad.driver.UI;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.user.UserManager;

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
        numPassengerPick.setMinValue(0);
        numPassengerPick.setWrapSelectorWheel(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomizedDialogStyle);

        builder.setView(rootView);

        Button negButton = (Button) rootView.findViewById(R.id.neg_button);
        negButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Button posButton = (Button) rootView.findViewById(R.id.pos_button);
        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numPassengers = numPassengerPick.getValue();
                try {
                    UserManager.getInstance().getUser().setSeatNum(numPassengers);
                } catch (Exception e) {
                    Log.e(TAG, "Could not retrieve user" + e.getMessage());
                }

                if (FirebaseUtils.getFirebaseUser() != null) {
                    String uid = FirebaseUtils.getFirebaseUser().getUid();
                    Log.d(TAG, "got uid: " + uid);
                    FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.isDriverEndpoint).setValue(true);
                    FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(uid).child(StringUtils.numPassengersEndpoint).setValue(numPassengers);
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
                dismiss();
            }
        });


        AlertDialog alert = builder.create();
        alert.show();

        return alert;
    }

}
