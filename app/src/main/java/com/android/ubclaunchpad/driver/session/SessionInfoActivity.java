package com.android.ubclaunchpad.driver.session;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.routingAlgorithms.FindBestRouteAlgorithm;
import com.android.ubclaunchpad.driver.session.models.SessionModel;
import com.android.ubclaunchpad.driver.user.User;
import com.android.ubclaunchpad.driver.user.UserManager;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionInfoActivity extends AppCompatActivity {

    private static final String TAG = "SessionInfoActivity";
    final String passengerDistance = "\nP\n\t\t\t\t";
    final String driverDistance = "\nD\n\t\t\t\t";
    final ArrayList<String> itemsArray = new ArrayList<>();
    final String UID = FirebaseUtils.getFirebaseUser().getUid();
    private ChildEventListener driversListener;
    private ChildEventListener passengersListener;
    private ChildEventListener driverPassengersListener;
    private DatabaseReference session;
    @BindView(R.id.viewSessionName)
    TextView textViewSessionName;

    // private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);
        ButterKnife.bind(this);
        final ListView listView = (ListView) findViewById(R.id.sessionItemsList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsArray);
        listView.setAdapter(adapter);
        final String sessionName = getIntent().getStringExtra(StringUtils.SESSION_NAME);
        textViewSessionName.setText(sessionName);
        final Button goButton = (Button) findViewById(R.id.go_Button);
        goButton.setVisibility(View.INVISIBLE);
        session = FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseSessionEndpoint)
                .child(sessionName);
        final User currentUser;
        try {
            currentUser = UserManager.getInstance().getUser();

            session.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String uid = dataSnapshot.child(StringUtils.FirebaseSessionHostUid).getValue(String.class);
                    if (uid.equals(FirebaseUtils.getFirebaseUser().getUid())) {
                        Toast.makeText(getBaseContext(), "You are the chosen one!", Toast.LENGTH_LONG).show();
                        goButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //add current user's UID to the current session's driver or passenger list
            session.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SessionModel updatedSession = dataSnapshot.getValue(SessionModel.class);
                    if (updatedSession == null) {
                        finish();
                        Toast.makeText(getBaseContext(), "Session no longer exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
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
            // This stinks like good old french cheese
        } catch (Exception e) {
            e.printStackTrace();
        }


        //listen to changes in Firebase to update the driver passenger info list
        //the displayed "driver" or "passenger" label is not dependent on the isDriver
        //field but the list the user is in
        initializeListeners(adapter, sessionName);
        session.child(StringUtils.FirebaseSessionDriverEndpoint)
                .addChildEventListener(driversListener);
        session.child(StringUtils.FirebaseSessionPassengerEndpoint)
                .addChildEventListener(passengersListener);
        session.addChildEventListener(driverPassengersListener);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting the goddamn algorithm
                setDriverPassengers();
            }
        });
    }

    private void addUserToAdapter(DataSnapshot dataSnapshot, final ArrayAdapter<String> adapter, final boolean inDriverList) {
        FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseUserEndpoint)
                .child(dataSnapshot.getValue().toString()) //get the added user's UID
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            String distance = inDriverList ? driverDistance : passengerDistance;
                            String username = distance + user.getName();
                            itemsArray.add(username);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Log.d(TAG, "Child " + dataSnapshot.getValue() + " is added to " + (inDriverList ? "drivers" : "passengers"));
    }

    private void removeUserFromAdapter(DataSnapshot dataSnapshot, final ArrayAdapter<String> adapter, final boolean inDriverList) {
        final String removedUID = dataSnapshot.getValue().toString();
        FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint)
                .child(removedUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            String distance = inDriverList ? driverDistance : passengerDistance;
                            itemsArray.remove(distance + user.getName());
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, removedUID + " is removed from" + (inDriverList ? "drivers" : "passengers"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void initializeListeners(final ArrayAdapter<String> adapter, final String sessionName) {
        driversListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addUserToAdapter(dataSnapshot, adapter, true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeUserFromAdapter(dataSnapshot, adapter, true);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        passengersListener = new ChildEventListener() {
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
        };



        driverPassengersListener = new ChildEventListener() {

            @Override
            public void onChildAdded(final DataSnapshot sessionSnapshot, String s) {

                DatabaseReference usersReference = FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint);
                usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot usersSnapshot) {

                        // If algorithm finished
                        if (sessionSnapshot.getKey().equals(StringUtils.FirebaseSessionDriverPassengers)) {

                            User currentUser = usersSnapshot.child(UID).getValue(User.class);
                            DataSnapshot driverPassengersSnapshot = sessionSnapshot.child(StringUtils.FirebaseSessionDriverPassengers);
                            Bundle data = new Bundle();
                            Message message = new Message();
                            message.setData(data);
                            data.putString(StringUtils.FirebaseSessionName, sessionName);
                            if (currentUser.getIsDriver()) {

                                // No need to search for anything, just start DriverPassengersActivity
                                data.putString(StringUtils.DriverPassengersDriverUid, UID);
                                runOnUiThread(startActivityTask(data));
                            } else {
                                // We need to find to which driver this passenger belongs to
                                for (DataSnapshot driver : driverPassengersSnapshot.getChildren()) {
                                    for (DataSnapshot passenger : driver.getChildren()) {
                                        if (passenger.getValue().equals(UID)) {

                                            data.putString(StringUtils.DriverPassengersDriverUid, UID);
                                            runOnUiThread(startActivityTask(data));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private Runnable startActivityTask(final Bundle data) {
        return new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SessionInfoActivity.this, DriverPassengersActivity.class);
                intent.putExtra(StringUtils.DriverPassengersFirebaseData, data);
                startActivity(intent);
            }
        };
    }

    /**
     * Retrieve all drivers and passengers from the current session
     */
    private void setDriverPassengers() {

        final List<User> users = new ArrayList<>();

        DatabaseReference usersReference = FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint);
        // Getting reference to the users in Firebase
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshotUsers) {
                // Getting reference to the drivers in current session
                session.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        DataSnapshot dataSnapshotDrivers = dataSnapshot.child(StringUtils.FirebaseSessionDriverEndpoint);


                        for (DataSnapshot driver : dataSnapshotDrivers.getChildren()) {
                            String driverUid = driver.getValue(String.class);

                            User uDriver = dataSnapshotUsers.child(driverUid).getValue(User.class);
                            uDriver.setIsDriver(true);
                            uDriver.setUserUid(driverUid);
                            users.add(uDriver);
                        }
                        DataSnapshot dataSnapshotPassengers = dataSnapshot.child(StringUtils.FirebaseSessionPassengerEndpoint);
                        for (DataSnapshot passenger : dataSnapshotPassengers.getChildren()) {
                            String passengerUid = passenger.getValue(String.class);
                            User uPassenger = dataSnapshotUsers.child(passengerUid).getValue(User.class);
                            uPassenger.setUserUid(passengerUid);
                            users.add(uPassenger);

                        }

                        String locStr = dataSnapshot.child(StringUtils.FirebaseSessionLocation).getValue(String.class);
                        LatLng location = StringUtils.stringToLatLng(locStr);


                        FindBestRouteAlgorithm algorithm = new FindBestRouteAlgorithm(location);
                        Map<String, Object> driverPassengers = algorithm.findBestRoute(users);

                        session.child(StringUtils.FirebaseSessionDriverPassengers).updateChildren(driverPassengers);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        session.removeEventListener(driversListener);
        session.removeEventListener(passengersListener);
        super.onDestroy();
    }
}