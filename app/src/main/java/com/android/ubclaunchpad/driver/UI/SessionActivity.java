package com.android.ubclaunchpad.driver.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO - This ENTIRE activity is for DEMO ONLY. UI team will REMOVE this
 */
public class SessionActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private SessionObj mSession;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @BindView(R.id.create_session) Button CreateSession;
    private String sessionName;
    private SessionCreateDialog scd;

    private List<SessionModel> allSessions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        Intent intent = getIntent();
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();

        scd = new SessionCreateDialog(this);
        Intent intent1 = getIntent();
        Intent SessionIntent = new Intent(this, SessionCreateDialog.class);   // session intent

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

        getNearbySessionNames();
    }

    /**
     * Get a list of names of nearby sessions
     * Displaying the list will also be handled here
     */
    private void getNearbySessionNames(){
        //"Users" is only used for testing, needs to be changed to "Session group"
        //after Firebase session group structure is set up
        mDatabase.child("Session group")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<LatLng> allSessionlatLngs = getAllSessionLatLngs(dataSnapshot);
                        UserUtils userUtils = new UserUtils();
                        List<LatLng> nearbySessionLatLngs = userUtils.findNearbyLatLngs(allSessionlatLngs, getApplicationContext());
                        onLoadedAllSessionNames(getSessionNames(allSessionlatLngs));
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
            latLngList.add(sessionModel.getLocation());
            allSessions.add(sessionModel);
        }
        return latLngList;
    }

    /**
     * Get the names of the sessions whose LatLng is close to the user's current latLng
     * @param nearbySessionLatLngs all session LatLngs close to the user's current LatLng
     */
    private List<String> getSessionNames(final List<LatLng> nearbySessionLatLngs) {
        List<String> sessionNames = new ArrayList<>();
        for( SessionModel sessionModel : allSessions ) {
            if( nearbySessionLatLngs.contains(sessionModel.getLocation()))
                sessionNames.add(sessionModel.getName());
        }
        return sessionNames;
    }

    /**
     * TODO - Use the list of nearby session names here
     * @param nearbySessionNames
     */
    void onLoadedAllSessionNames(List<String> nearbySessionNames){

    }

}
