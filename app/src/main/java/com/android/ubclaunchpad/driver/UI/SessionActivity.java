package com.android.ubclaunchpad.driver.UI;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.SessionCreateDialog;
import com.android.ubclaunchpad.driver.util.SessionObj;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SessionActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private SessionObj mSession;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button CreateSession;
    private String sessionName;
    private SessionCreateDialog scd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();

        CreateSession = (Button) findViewById(R.id.create_session);
        scd = new SessionCreateDialog(this);
        Intent intent1 = getIntent();
        Intent SessionIntent = new Intent(this, SessionCreateDialog.class);   // session intent

        CreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scd.show();
            }
        });

    }
}
