package com.android.ubclaunchpad.driver.UI;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.SessionObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Marina on 10/22/16.
 * TODO this activity is for DEMO ONLY. UI please REMOVE
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
                dSessionName = txtDescription.getText().toString();
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
        mDatabase.child("Session group").child(dSessionName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists()){
                           Toast.makeText(getContext(),"session exists, change session name",Toast.LENGTH_LONG).show();
                       }
                       else {
                           createSession();
                           Toast.makeText(getContext(),"session created",Toast.LENGTH_LONG).show();
                           c.finish();
                       }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", databaseError.toException());
                    }
                });

    }
    private void createSession () {
        String uniqueID = UUID.randomUUID().toString();

        // hashmap to store the list of users
        Map<String, String> userSessionHashMap = new HashMap<>();
        userSessionHashMap.put(mUser.getUid(), mUser.getUid());

        scdSession = new SessionObj(dSessionName, uniqueID, "Lat Long", mUser.getUid());

        mDatabase.child("Session group").child(scdSession.getSessionName()).setValue(userSessionHashMap);
    }
}

