package com.android.ubclaunchpad.driver.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        String sessionName = getIntent().getStringExtra(getString(R.string.session_name));
        final String passengerDistance = "\nP\n\t\t\t\t";
        final String driverDistance = "\nD\n\t\t\t\t";
        TextView textViewSessionName = (TextView) findViewById(R.id.viewSessionName);
        textViewSessionName.setText(sessionName);

        //Adding Drivers to list view
        FirebaseUtils.getDatabase().child("Session group").child(sessionName).child("drivers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //get driver's name
                String driverInfo = (String) dataSnapshot.child("title").getValue(String.class);
                driverInfo =  driverDistance + driverInfo;
                if(driverInfo != null) {
                    itemsArray.add(driverInfo);
                    adapter.notifyDataSetChanged();
                }
                Log.d(TAG, "Populating");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final String removableDriver = driverDistance + (String) dataSnapshot.child("title").getValue();
                adapter.remove(removableDriver);
                adapter.notifyDataSetChanged();
                Log.d(TAG,"Data Removed");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Data Cancelled");
            }
        });

        //Adding Passengers to list view
        FirebaseUtils.getDatabase().child("Session group").child(sessionName).child("passengers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 String passengerInfo = (String) dataSnapshot.child("title").getValue(String.class);
                passengerInfo =  passengerDistance + passengerInfo ;
                if(passengerInfo != null) {
                    adapter.add(passengerInfo);
                }
                Log.d(TAG, "Populating");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Data Removed");
                final String removablePassenger = passengerDistance + (String) dataSnapshot.child("title").getValue();
                adapter.remove(removablePassenger);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"DataCancelled");
            }
        });


          //Testing
//        FirebaseUtils.getDatabase().child("Session group").child("UBC").child("drivers").push().child("title").setValue("before :D");
//        FirebaseUtils.getDatabase().child("Session group").child("UBC").child("passengers").push().child("title").setValue("So even if the name is really long. Still it will look better then before :D");
//        Log.d(TAG, "testing");
    }
}