package com.android.ubclaunchpad.driver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button iAmADriverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iAmADriverButton = (Button) findViewById(R.id.i_am_a_driver_button);
        iAmADriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "I am a driver lol", Toast.LENGTH_SHORT).show();
                EditText howManyPassengers = new EditText(v.getContext());
                howManyPassengers.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                ((EditText) findViewById(R.id.how_many_passengers)).setVisibility(View.VISIBLE);
            }
        });
    }
}
