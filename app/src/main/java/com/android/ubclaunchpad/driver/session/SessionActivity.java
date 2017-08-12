package com.android.ubclaunchpad.driver.session;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.BaseMenuActivity;
import com.android.ubclaunchpad.driver.UI.MapsActivity;
import com.android.ubclaunchpad.driver.session.models.SessionModel;
import com.android.ubclaunchpad.driver.user.UserUtils;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.ubclaunchpad.driver.util.StringUtils.stringToLatLng;

public class SessionActivity extends BaseMenuActivity {
    private static final String TAG = SessionActivity.class.toString();
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1010;
    @BindView(R.id.create_session)
    FloatingActionButton CreateSession;
    @BindView(R.id.list_existing_sessions)
    RecyclerView mRecyclerView;
    @BindView(R.id.map_button)
    Button showMapButton;
    @BindView(R.id.launch_google_maps_button)
    Button launchGoogleMapsButton;
    private SessionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String sessionName;
    private SessionCreateDialog sessionCreateDialog;
    private DividerItemDecoration mDividerItemDecoration;

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

        mAdapter = new SessionAdapter(this, sessions);
        mRecyclerView.setAdapter(mAdapter);

        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), RecyclerView.HORIZONTAL);
        mDividerItemDecoration.setDrawable(getDrawable(R.drawable.session_activity_divider));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        sessionCreateDialog = new SessionCreateDialog(this);

        CreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionCreateDialog.show();
            }
        });

        // button for showing embedded Google Map, for testing purposes
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                //this LatLng is only used to test whether MapsActivity receives
                //the correct latlng, delete it with the real session latlng
                LatLng sessionLatLng = new LatLng(49.2827, -123.1207);
                mapIntent.putExtra("session latlng", sessionLatLng);
                startActivity(mapIntent);
            }
        });

        // button for launching Google Maps and showing a hardcoded route, for testing purposes
        launchGoogleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=CF Richmond Centre3&destination=Myst Asian Fusion Restaurant&waypoints=Oakridge Centre|Metrotown");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    // Will refresh the list of sessions every time Activity enters foreground
    @Override
    protected void onResume() {
        super.onResume();
        //request for accessing fine location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "access fine location not permitted");
            ActivityCompat.requestPermissions(SessionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            // reset the sessions to an empty list and find the nearby sessions again
            allSessions = new ArrayList<>();
            sessions = new ArrayList<>();
            displayNearbySessions();
        }
    }

    /**
     * Get a list of names of nearby sessions
     * Displaying the list will also be handled here
     */
    private void displayNearbySessions() {
        FirebaseUtils.getDatabase().child(StringUtils.FirebaseSessionEndpoint)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<LatLng> allSessionlatLngs = getAllSessionLatLngs(dataSnapshot);
                        UserUtils userUtils = new UserUtils();
                        List<LatLng> nearbySessionLatLngs = userUtils.findNearbyLatLngs(allSessionlatLngs);
                        List<SessionModel> nearbySessions = getNearbySessions(nearbySessionLatLngs);
                        mAdapter.updateSessions(nearbySessions);
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
     *
     * @param dataSnapshot the DataSnapshot of Session group
     * @return a list of LatLngs of all sessions
     */
    private List<LatLng> getAllSessionLatLngs(DataSnapshot dataSnapshot) {
        final List<LatLng> latLngList = new ArrayList<>();
        for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
            SessionModel sessionModel = sessionSnapshot.getValue(SessionModel.class);
            String latLngString = sessionModel.getLocation();
            Log.v(TAG, "latlng string is " + latLngString);
            latLngList.add(stringToLatLng(latLngString));
            allSessions.add(sessionModel);
        }
        return latLngList;
    }


    /**
     * Get the sessions whose LatLng is close to the user's current latLng
     *
     * @param nearbySessionLatLngs all session LatLngs close to the user's current LatLng
     */
    private List<SessionModel> getNearbySessions(final List<LatLng> nearbySessionLatLngs) {
        List<SessionModel> nearbySessions = new ArrayList<>();
        for (SessionModel sessionModel : allSessions) {
            String latLngString = sessionModel.getLocation();
            LatLng latLngObj = stringToLatLng(latLngString);
            if (nearbySessionLatLngs.contains(latLngObj))
                nearbySessions.add(sessionModel);
        }
        return nearbySessions;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    displayNearbySessions();
                } else {
                    Log.v(TAG, "permission denied");
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }
}
