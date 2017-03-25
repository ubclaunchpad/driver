package com.android.ubclaunchpad.driver.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.MapsActivity;
import com.android.ubclaunchpad.driver.models.SessionModel;
import com.android.ubclaunchpad.driver.session.SessionAdapter;
import com.android.ubclaunchpad.driver.session.SessionCreateDialog;
import com.android.ubclaunchpad.driver.util.UserUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.ubclaunchpad.driver.util.StringUtils.stringToLatLng;

public class SessionActivity extends AppCompatActivity {

    private static final String TAG = SessionActivity.class.toString();
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

        displayNearbySessions();

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
                        for(LatLng latLng : allSessionlatLngs){
                            Log.v(TAG, "all session latlng is " + latLng.latitude + " " + latLng.longitude);
                        }
                        UserUtils userUtils = new UserUtils();
                        List<LatLng> nearbySessionLatLngs = userUtils.findNearbyLatLngs(allSessionlatLngs, getApplicationContext());

                        for(LatLng latLng : nearbySessionLatLngs){
                            Log.v(TAG, "nearby session latlng is " + latLng.latitude + " " + latLng.longitude);
                        }

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
            SessionModel sessionModel = sessionSnapshot.getValue(SessionModel.class);
            String latLngString = sessionModel.getLocation();

            Log.v(TAG, "latlng string is "+latLngString);
            latLngList.add(stringToLatLng(latLngString));
            allSessions.add(sessionModel);

        }
        return latLngList;
    }



    /**
     * Get the sessions whose LatLng is close to the user's current latLng
     * @param nearbySessionLatLngs all session LatLngs close to the user's current LatLng
     */
    private List<SessionModel> getNearbySessions(final List<LatLng> nearbySessionLatLngs) {
        List<SessionModel> nearbySessions = new ArrayList<>();
        for( SessionModel sessionModel : allSessions ) {
            String latLngString = sessionModel.getLocation();
            LatLng latLngObj = stringToLatLng(latLngString);
            if( nearbySessionLatLngs.contains(latLngObj))
                nearbySessions.add(sessionModel);
        }
        return nearbySessions;
    }
}
