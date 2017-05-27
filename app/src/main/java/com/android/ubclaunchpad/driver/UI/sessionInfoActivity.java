package com.android.ubclaunchpad.driver.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.SessionModel;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class sessionInfoActivity extends AppCompatActivity {

    private static final String TAG = "sessionInfoActivity";
   // private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        final ArrayList<String> itemsArray = new ArrayList<String>();
        final ListView listView = (ListView) findViewById(R.id.sessionItemsList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsArray);
        listView.setAdapter(adapter);
        final String sessionName = getIntent().getStringExtra(getString(R.string.session_name));
        final String passengerDistance = "\nP\n\t\t\t\t";
        final String driverDistance = "\nD\n\t\t\t\t";
        TextView textViewSessionName = (TextView) findViewById(R.id.viewSessionName);
        textViewSessionName.setText(sessionName);

        final String UID = FirebaseUtils.getFirebaseUser().getUid();
        //find current user's id
        FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseUserEndpoint)
                .child(UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnapshot) {
                        final User currentUser = userSnapshot.getValue(User.class);

                        //add current user's UID to the current session's driver or passenger list
                        final DatabaseReference session = FirebaseUtils.getDatabase()
                                .child("Session group")
                                .child(sessionName); //find the current session
                        session.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SessionModel updatedSession = dataSnapshot.getValue(SessionModel.class);
                                        if (currentUser.isDriver &&
                                                !updatedSession.getDrivers().contains(UID)) {
                                            updatedSession.getDrivers().add(UID);
                                            session.setValue(updatedSession);
                                        }
                                        if (!currentUser.isDriver &&
                                                !updatedSession.getPassengers().contains(UID)) {
                                            updatedSession.getPassengers().add(UID);
                                            session.setValue(updatedSession);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                        ChildEventListener childEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                //get driver's name
                                FirebaseUtils.getDatabase()
                                        .child(StringUtils.FirebaseUserEndpoint)
                                        .child(dataSnapshot.getValue().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                String username;
                                                if (user.isDriver)
                                                    username = driverDistance + user.getName();
                                                else username = passengerDistance + user.getName();
                                                itemsArray.add(username);
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                Log.d(TAG, "Populating" + dataSnapshot.getValue());
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                String removedUID = dataSnapshot.getValue().toString();
                                FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint)
                                        .child(removedUID)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                String removableUser;
                                                if(user.isDriver)
                                                    removableUser = driverDistance + user.getName();
                                                else removableUser = passengerDistance + user.getName();
                                                adapter.remove(removableUser);
                                                adapter.notifyDataSetChanged();
                                                Log.d(TAG,"Data Removed");
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        session.child("drivers")
                                .addChildEventListener(childEventListener);
                        session.child("passengers")
                                .addChildEventListener(childEventListener);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

          //Testing
//        FirebaseUtils.getDatabase().child("Session group").child("UBC").child("drivers").push().child("title").setValue("before :D");
//        FirebaseUtils.getDatabase().child("Session group").child("UBC").child("passengers").push().child("title").setValue("So even if the name is really long. Still it will look better then before :D");
//        Log.d(TAG, "testing");
    }
}