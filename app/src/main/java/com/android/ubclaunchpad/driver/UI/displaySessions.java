package com.android.ubclaunchpad.driver.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;

import java.util.ArrayList;
import java.util.List;

/*
* Created by Navjashan on 09/11/2016
 */

//This class get the session name and is required to generate a list of all the user who have
//joined this session

//Requires: An arrayList of strings of the user in a particular session
public class displaySessions extends AppCompatActivity {

    //This is the array list of all the users in the given sesssion
    ArrayList<String> showSessions = new ArrayList<String>();
    ArrayList<String> usersInSession = new ArrayList<String>();

    String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sessions);

        Intent i = getIntent();
        sessionName = i.getStringExtra("sessionName");
        showSessions  = getUsersinthisSession(sessionName);

        ListView sessions = (ListView) findViewById(R.id.sessions);
        TextView textView = (TextView) findViewById(R.id.SessionInfo);

        textView.setText(sessionName);
        ListAdapter sessionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, showSessions);
        sessions.setAdapter(sessionAdapter);
    }

    //Requires some handler class that will recieve the session name and will return a list of string
    //of the user in that session
    //A hashmap of <String, ArrayList<String>> is required
    private ArrayList<String> getUsersinthisSession(String sessionId){
        usersInSession = null;
        //Call to some method of that class to recieve the user info
        return usersInSession;
    }

    //When the user clicks GO, the next activity to which it will move
    public void sessionInfoDone(View view){
        Intent intent = new Intent(this, SessionList.class);
        startActivity(intent);
    }
}
