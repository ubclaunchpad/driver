package com.android.ubclaunchpad.driver.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.login.LoginContract;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
* Created by Navjashan on 09/11/2016
 */

/*TODO
Get a session list which gets updated in real time
Maybe the best way to get it is from firebase itself
 */

//Requires: An ArrayList to display the list of sessions on the firebase
//Post: User who selects the session should also be added to that session
public class SessionList extends AppCompatActivity {

    //The type of array list could be changed
    ArrayList<String> sessionList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        //Gets the session List from the previous activity
        Intent i = getIntent();
        sessionList  = i.getStringArrayListExtra("sessionList");

        //Puts the list in a list view
        ListAdapter sessionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sessionList);
        ListView sessions = (ListView) findViewById(R.id.sessions);

        sessions.setAdapter(sessionAdapter);

        //Allows the user to select the session and sends the session name's string to the next activity
        sessions.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                 @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                     String selectedSession = String.valueOf(parent.getItemAtPosition(position));

                     //Second class where data to be sent
                     Intent sessionSelected = new Intent(SessionList.this, displaySessions.class);
                     sessionSelected.putExtra("getSession", selectedSession);
                     startActivity(sessionSelected);
                 }

                }
        );
    }

    //Allows the user to create a new session
    public void createNewSession(View view){
        Intent newSession = new Intent(this , NewSession.class);
        startActivity(newSession);
    }
}
