package com.android.ubclaunchpad.driver.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.SessionModel;
import com.android.ubclaunchpad.driver.util.SessionCreateDialog;
import com.android.ubclaunchpad.driver.util.SessionObj;
import com.android.ubclaunchpad.driver.util.UserUtils;
import com.google.android.gms.fitness.data.Session;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private SessionModel mSession;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @BindView(R.id.create_session) Button CreateSession;
    @BindView(R.id.list_existing_sessions) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String sessionName;
    private SessionCreateDialog scd;


    private List<SessionModel> allSessions = new ArrayList<>();
    private List<SessionModel> sessions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SessionAdapter(sessions);
        mRecyclerView.setAdapter(mAdapter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();

        scd = new SessionCreateDialog(this);

        CreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scd.show();
            }
        });

        /**
         * this button is only used to test the map
         */
        Button showMapButton = (Button) findViewById(R.id.mapButton);
        final Intent mapIntent = new Intent(this, MapsActivity.class);
        //this LatLng is only used to test whether MapsActivity receives
        //the correct latlng, delete it with the real session latlng
        LatLng sessionLatLng = new LatLng(49.2827, -123.1207);

        mapIntent.putExtra("session latlng", sessionLatLng);
        showMapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(mapIntent);
            }
        });

        testParseString();
       // displayNearbySessions();
    }

    /**
     * Get a list of names of nearby sessions
     * Displaying the list will also be handled here
     */
    private void displayNearbySessions(){
        mDatabase.child("Session group")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<LatLng> allSessionlatLngs = getAllSessionLatLngs(dataSnapshot);
                        UserUtils userUtils = new UserUtils();
                        List<LatLng> nearbySessionLatLngs = userUtils.findNearbyLatLngs(allSessionlatLngs, getApplicationContext());
                        List<SessionModel> nearbySessions = getNearbySessions(nearbySessionLatLngs);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", databaseError.toException());
                    }
                });
    }

    /**
     * This function assumes SessionModel contains a name field and a name getter method
     * It gets saves all session LatLngs and all session models into two lists
     * @param dataSnapshot the DataSnapshot of Session group
     * @return a list of LatLngs of all sessions
     */
    private List<LatLng> getAllSessionLatLngs( DataSnapshot dataSnapshot) {
        final List<LatLng> latLngList = new ArrayList<>();
        for(DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
            //TODO - test this
            SessionModel sessionModel = sessionSnapshot.getValue(SessionModel.class);
          //  String latLngString = sessionModel.getLocation();
          //  latLngList.add(parseLatLngString(latLngString));
            allSessions.add(sessionModel);
        }
        return latLngList;
    }

    public void testParseString() {
        LatLng obj1 = new LatLng(0.44567, -3.99);
        LatLng obj2 = new LatLng(-0.00003, 90.345);
        List<LatLng> list = new ArrayList<>();
        list.add(obj1);
        list.add(obj2);
        Log.v("session activity", "list contains obj: " + list.contains(new LatLng(-0.00003,90.345)));
    }
    public static LatLng parseLatLngString(String latLngString){
        String[] latLng = latLngString.split(",");
        double lat = Double.parseDouble(latLng[0]);
        double lng = Double.parseDouble(latLng[1]);
        return new LatLng(lat, lng);
    }

    /**
     * Get the sessions whose LatLng is close to the user's current latLng
     * @param nearbySessionLatLngs all session LatLngs close to the user's current LatLng
     */
    private List<SessionModel> getNearbySessions(final List<LatLng> nearbySessionLatLngs) {
        List<SessionModel> nearbySessions = new ArrayList<>();
        for( SessionModel sessionModel : allSessions ) {
          //  String latLngString = sessionModel.getLocation();
          //  LatLng latLngObj = parseLatLngString(latLngString);
          //  if( nearbySessionLatLngs.contains(latLngObj))
          //      nearbySessions.add(sessionModel);
        }
        return nearbySessions;
    }

}
