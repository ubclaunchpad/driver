package com.android.ubclaunchpad.driver.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;

public class ReauthenticationFragment extends DialogFragment {

    public ReauthenticationFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_reauthentication, null);

        final EditText mEmail = (EditText) mView.findViewById(R.id.emailEditText);
        final EditText mPassword = (EditText) mView.findViewById(R.id.passwordEditText);
        Button mLogin = (Button) mView.findViewById(R.id.btnLogin);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    // TODO try authentication with email and password

                    if(true) { // successful -> dismiss dialog and start EditProfile activity
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        startActivity(intent);
                    } else { // unsuccessful -> error message
                        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return dialog;
    }
}
