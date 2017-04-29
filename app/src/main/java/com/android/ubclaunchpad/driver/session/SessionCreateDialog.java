package com.android.ubclaunchpad.driver.session;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.SessionModel;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Marina on 10/22/16.
 */
public class SessionCreateDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity mActivity;
    public Button okButton, cancelButton;
    public String mSessionName;
    private EditText mTextDescription;
    public SessionModel mSessionModel;
    private DatabaseReference mDatabase;

    public SessionCreateDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_create_dialog);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTextDescription = (EditText) findViewById(R.id.session_name);

        okButton = (Button) findViewById(R.id.btn_yes);
        cancelButton = (Button) findViewById(R.id.btn_no);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                mSessionName = mTextDescription.getText().toString();
                createUniqueSession();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    private void createUniqueSession() {
        if (mDatabase != null) {
            mDatabase.child("Session group").child(mSessionName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getContext(), "session exists, change session name", Toast.LENGTH_LONG).show();
                            } else {
                                createSession();
                                Toast.makeText(getContext(), "session created", Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Cancelled", databaseError.toException());
                        }
                    });
        }
    }

    private void createSession() {
        LatLng latLng;
        try {
            latLng = StringUtils.stringToLatLng(UserManager.getInstance().getUser().getCurrentLatLngStr());
        } catch (Exception e) {
            latLng = new LatLng(0, 0);
        }
        mSessionModel = SessionModel.createNewSession(mSessionName, latLng);

        if (mDatabase != null) {
            mDatabase.child("Session group").child(mSessionName).setValue(mSessionModel);
        }
    }
}

