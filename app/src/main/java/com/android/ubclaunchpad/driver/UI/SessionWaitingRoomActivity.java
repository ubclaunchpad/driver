package com.android.ubclaunchpad.driver.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sherryuan on 2017-01-21.
 */

public class SessionWaitingRoomActivity extends AppCompatActivity {

    private String sessionName;
    private TextView sessionNameTextView;
    private ListView sessionUsersListView;
    private DatabaseReference mDatabase;
    private ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_waiting_room);

        sessionName = getIntent().getStringExtra("SESSION_NAME");
        sessionNameTextView = (TextView) findViewById(R.id.session_waiting_name);
        sessionNameTextView.setText(sessionName);
        sessionUsersListView = (ListView) findViewById(R.id.session_users_list);
        // TODO: see if I'm passing correct layout file
        adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.activity_session_waiting_room);
        sessionUsersListView.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener sessionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // this should give snapshot of entire database
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(sessionListener);
    }
}
