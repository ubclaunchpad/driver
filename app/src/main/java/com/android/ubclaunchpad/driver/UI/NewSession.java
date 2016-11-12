package com.android.ubclaunchpad.driver.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.ubclaunchpad.driver.R;

/*
* Created by Navjashan on 09/11/2016
 */

//Post: Should also be able to add all the information that the user has entered to the session list
//Post: User who selects the session should also be added to that session

public class NewSession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
    }

    public void displaySession(View view){

        Intent display = new Intent(this, displaySessions.class);
        EditText editText = (EditText) findViewById(R.id.enterSession);
        String message = editText.getText().toString();

        if(message.length()!=0) {
            display.putExtra("displaySession", message);
            startActivity(display);
        }
    }
}
