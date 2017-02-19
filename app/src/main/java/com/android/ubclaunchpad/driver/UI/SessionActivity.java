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
import com.android.ubclaunchpad.driver.util.SessionCreateDialog;
import com.android.ubclaunchpad.driver.util.SessionObj;
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


        Log.v("tag", "!!!!!!!!!!!!!!!!starting session latlngs!!!!!!!!!!!!!");

        /**
         * get a list of all session latlngs
         */
        mDatabase.child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<LatLng> allSessionlatLngs = getAllSessionLatLngs(dataSnapshot);
                        UserUtils userUtils = new UserUtils();
                        List<LatLng> nearbySessionLatLngs = userUtils.findNearbyLatLngs(allSessionlatLngs, getApplicationContext());
                        List<String> testSNames = getSessionNames(allSessionlatLngs);
                    //    for(String name : testSNames ) {
                    //        Log.v("tag", "NAME IS " + name);
                     //   }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", databaseError.toException());
                    }
                });



    }

    List<LatLng> getAllSessionLatLngs( DataSnapshot dataSnapshot) {
        final List<LatLng> latLngList = new ArrayList<>();
        for(DataSnapshot session : dataSnapshot.getChildren()) {
            DataSnapshot sessionLatLng = session.child("latLng");
            if( sessionLatLng.exists()) {
                Double lat = sessionLatLng.child("latitude").getValue(Double.class);
                Double lng = sessionLatLng.child("longitude").getValue(Double.class);
                LatLng latLng = new LatLng(lat, lng);
                latLngList.add(latLng);
            }
        }
        return latLngList;
    }

    List<String> getLatSessionNames(List<LatLng> nearbySessionLatLngs) {
        final List<String> nearbySessionNames = new ArrayList<String>();
        final List<String> nearbyLatSessionNames = new ArrayList<String>();
        final List<String> nearbyLngSessionNames = new ArrayList<String>();
        Log.v("tag","ENTERING GET SESSION NAME");

        for( LatLng latLng : nearbySessionLatLngs) {
            mDatabase.child("Users").orderByChild("latLng/latitude").equalTo(latLng.latitude)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot latLngSnapShot : dataSnapshot.getChildren()) {
                                DatabaseReference latLngRef = latLngSnapShot.getRef();
                                String sessionName = latLngRef.getKey();
                                if(!nearbyLatSessionNames.contains(sessionName)) {
                                    nearbyLatSessionNames.add(sessionName);
                                }
                             /*   Log.v("tag","-----------------");
                                for(String name: nearbyLatSessionNames) {
                                    Log.v("tag", name);
                                } */
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Cancelled", databaseError.toException());
                        }
                    });

            mDatabase.child("Users").orderByChild("latLng/longitude").equalTo(latLng.longitude)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot latLngSnapShot : dataSnapshot.getChildren()) {
                                DatabaseReference latLngRef = latLngSnapShot.getRef();
                                String sessionName = latLngRef.getKey();
                                if(!nearbyLngSessionNames.contains(sessionName)) {
                                    nearbyLngSessionNames.add(sessionName);
                                }
                           /*     Log.v("tag","***************");
                                for(String name: nearbyLngSessionNames) {
                                    Log.v("tag", name);
                                } */
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Cancelled", databaseError.toException());
                        }
                    });

        }


        return nearbyLatSessionNames;

    }

    List<String> getSessionNames(List<LatLng> nearbySessionLatLngs){
        Log.v("tag","sssssssssssssssssss");
        List<String> names = getLatSessionNames(nearbySessionLatLngs);
        for(String name : names) {

            Log.v("tag", name);
        }
        return new ArrayList<String>();
    }

}
