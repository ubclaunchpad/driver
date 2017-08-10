package com.android.ubclaunchpad.driver.session;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.user.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverPassengersActivity extends AppCompatActivity {

    private static final String TAG = DriverPassengersActivity.class.getSimpleName();
    @BindView(R.id.start_button)
    Button startButton;
    @BindView(R.id.driver_name)
    TextView driverName;
    @BindView(R.id.driver_status)
    TextView driverStatus;
    @BindView(R.id.passengers_container)
    RecyclerView driversPassengers;
    private DatabaseReference session;
    private ChildEventListener listener;
    private DriverPassengersAdapter adapter;
    private List<User> passengers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_passengers);

        String sessionName = (String) getIntent().getBundleExtra(StringUtils.DriverPassengersFirebaseData).get(StringUtils.FirebaseSessionName);
        String driverUid = (String) getIntent().getBundleExtra(StringUtils.DriverPassengersFirebaseData).get(StringUtils.DriverPassengersDriverUid);
        ButterKnife.bind(this);
        passengers = new ArrayList<>();
        adapter = new DriverPassengersAdapter(this, passengers);
        driversPassengers = (RecyclerView) findViewById(R.id.passengers_container);
        driversPassengers.setAdapter(adapter);
        driversPassengers.setLayoutManager(new LinearLayoutManager(this));
        session = FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseSessionEndpoint)
                .child(sessionName);

        if (driverUid.equals(FirebaseUtils.getFirebaseUser().getUid())) {
            Toast.makeText(getBaseContext(), "You are the chosen one!", Toast.LENGTH_LONG).show();
            startButton.setVisibility(View.VISIBLE);
        }
        // Setting driver's name
        FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint)
                .child(driverUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User driver = dataSnapshot.getValue(User.class);
                driverName.setText(driver.name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addPassengerToAdapter(dataSnapshot, adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removePassengerFromAdapter(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        session.child(StringUtils.FirebaseSessionDriverPassengers).child(driverUid).addChildEventListener(listener);
    }


    private void addPassengerToAdapter(DataSnapshot dataSnapshot, final DriverPassengersAdapter adapter) {

        FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseUserEndpoint)
                .child(dataSnapshot.getValue().toString()) //get the added user's UID
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Toast.makeText(getBaseContext(), user.email, Toast.LENGTH_LONG).show();
                        if (user != null) {
                            passengers.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Log.d(TAG, "Passenger " + dataSnapshot.getValue() + " is added to this driver");
    }

    private void removePassengerFromAdapter(DataSnapshot dataSnapshot, final DriverPassengersAdapter adapter) {
        final String removedUID = dataSnapshot.getValue().toString();
        FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint)
                .child(removedUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            passengers.remove(user);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, user + " is removed from current driver");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        session.removeEventListener(listener);
    }
}
