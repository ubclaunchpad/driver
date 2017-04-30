package com.android.ubclaunchpad.driver.util;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.DispatchActivity;
import com.google.firebase.auth.FirebaseAuth;

public class BaseMenuActivity extends AppCompatActivity {
    private static final String TAG = BaseMenuActivity.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                Intent intent = new Intent(this, DispatchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d(TAG, "Signing Out");
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                //finish();
                return true;

            case R.id.action_edit_profile:
                Log.v(TAG, "editing profile");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
