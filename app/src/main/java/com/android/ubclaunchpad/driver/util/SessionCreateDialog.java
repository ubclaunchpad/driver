package com.android.ubclaunchpad.driver.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Marina on 10/22/16.
 */
public class SessionCreateDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public String dSessionName;
    private EditText txtDescription;
    public SessionObj scdSession;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    public SessionCreateDialog (Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_create_dialog);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();


        txtDescription = (EditText) findViewById(R.id.session_name);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Log.d("session_name",txtDescription.getText().toString());
                dSessionName = txtDescription.getText().toString();
                createSession();
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void createSession () {

        String uniqueID = UUID.randomUUID().toString();
        // hashmap to store the list of users
        Map<String, Object> userSessionHashMap = new HashMap<String, Object>();

        // get session list of user objects
        for (User sessionUser: session.getUser()) {
            userDataHashMap.put(sessionUser.getUser(), sessionUser);
        }

        mDatabase.child("Session").child(uniqueID).setValue(userSessionHashMap);

        scdSession = new SessionObj(dSessionName, uniqueID, mUser.getDisplayName());
        // Lat Lon should be changed to the real lat lon of the current session
        mDatabase.child("Geo point").child("Lat Long").setValue(scdSession.getSessionID());
    }
}

