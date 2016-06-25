package com.android.ubclaunchpad.driver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button iAmAPassengerButton;
    private Button iAmADriverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iAmAPassengerButton = (Button) findViewById(R.id.i_am_a_passenger_button);
        iAmAPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "I AM A PASSENGER", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.how_many_passengers)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.submit_passenger_number)).setVisibility(View.GONE);
            }
        });

        iAmADriverButton = (Button) findViewById(R.id.i_am_a_driver_button);
        iAmADriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.how_many_passengers)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.submit_passenger_number)).setVisibility(View.VISIBLE);
            }
        });
    }
}
