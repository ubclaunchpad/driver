package com.android.ubclaunchpad.driver.session;

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
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SessionInfoActivity extends AppCompatActivity {

    private static final String TAG = "SessionInfoActivity";
    final String passengerDistance = "\nP\n\t\t\t\t";
    final String driverDistance = "\nD\n\t\t\t\t";
    final ArrayList<String> itemsArray = new ArrayList<String>();
   // private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        final ListView listView = (ListView) findViewById(R.id.sessionItemsList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsArray);
        listView.setAdapter(adapter);
        final String sessionName = getIntent().getStringExtra(StringUtils.SESSION_NAME);
        TextView textViewSessionName = (TextView) findViewById(R.id.viewSessionName);
        textViewSessionName.setText(sessionName);

        final DatabaseReference session = FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseSessionEndpoint)
                .child(sessionName);
        final String UID = FirebaseUtils.getFirebaseUser().getUid();
        final User currentUser;
        try {
            currentUser = UserManager.getInstance().getUser();
            //add current user's UID to the current session's driver or passenger list
            session.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SessionModel updatedSession = dataSnapshot.getValue(SessionModel.class);
                    if (currentUser.isDriver &&
                            !updatedSession.getDrivers().contains(UID)) {
                        updatedSession.addDriver(UID);
                        session.setValue(updatedSession);
                        Log.v(TAG, "new driver added");
                    }
                    if (!currentUser.isDriver &&
                            !updatedSession.getPassengers().contains(UID)) {
                        updatedSession.addPassenger(UID);
                        session.setValue(updatedSession);
                        Log.v(TAG, "new passenger added");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        //listen to changes in Firebase to update the driver passenger info list
        //the displayed "driver" or "passenger" label is not dependent on the isDriver
        //field but the list the user is in
        session.child(StringUtils.FirebaseSessionDriverEndpoint)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        addUserToAdapter(dataSnapshot, adapter, true);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        removeUserFromAdapter(dataSnapshot, adapter, true);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
        session.child(StringUtils.FirebaseSessionPassengerEndpoint)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        addUserToAdapter(dataSnapshot, adapter, false);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        removeUserFromAdapter(dataSnapshot, adapter, false);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void addUserToAdapter(DataSnapshot dataSnapshot, final ArrayAdapter<String> adapter, final boolean inDriverList){
        FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseUserEndpoint)
                .child(dataSnapshot.getValue().toString()) //get the added user's UID
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if( user != null ) {
                            String distance = inDriverList? driverDistance : passengerDistance;
                            String username = distance + user.getName();
                            itemsArray.add(username);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
        Log.d(TAG, "Child " + dataSnapshot.getValue() + " is added to " + (inDriverList? "drivers":"passengers"));
    }

    private void removeUserFromAdapter(DataSnapshot dataSnapshot, final ArrayAdapter<String> adapter, final boolean inDriverList){
        final String removedUID = dataSnapshot.getValue().toString();
        FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint)
                .child(removedUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if( user != null ) {
                            String distance = inDriverList? driverDistance : passengerDistance;
                            itemsArray.remove(distance + user.getName());
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, removedUID + " is removed from" + (inDriverList? "drivers":"passengers"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
}