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
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReauthenticationFragment extends DialogFragment {

    @BindView(R.id.emailEditText) EditText mEmail;
    @BindView(R.id.passwordEditText) EditText mPassword;
    @BindView(R.id.btnLogin) Button mLogin;

    public ReauthenticationFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_reauthentication, null);
        ButterKnife.bind(this, mView);
        //final EditText mEmail = (EditText) mView.findViewById(R.id.emailEditText);
        //final EditText mPassword = (EditText) mView.findViewById(R.id.passwordEditText);
        //Button mLogin = (Button) mView.findViewById(R.id.btnLogin);

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
                    FirebaseUser user = FirebaseUtils.getFirebaseUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, password);

                    user.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                // if authentication is successful, start EditProfile activity
                                @Override
                                public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                                        startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                // if authentication is unsuccessful, display error message
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return dialog;
    }
}
