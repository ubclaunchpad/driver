package com.android.ubclaunchpad.driver.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.ubclaunchpad.driver.R;
import com.google.firebase.auth.FirebaseAuth;

// Activity that has the status bar.
// Extend this activity if you want your Activity to have the logout menu at the top.
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
        checkInternetConnection();
    }

    // ActionBar menu item options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                new AlertDialogFragment().show(getFragmentManager(), "signOutAlertDialog");
                return true;

            case R.id.action_edit_profile:
                ReauthenticationFragment reauthenticationFragment = new ReauthenticationFragment();
                reauthenticationFragment.show(getFragmentManager(), "reauthenticationDialog");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method signs the user out and clears the backstack so that the user will no longer be
     * able to access activities before it by pressing the back button.
     */
    public void signOut() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //remove all activity in stack
        Log.d(TAG, "Signing Out");
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }

    public static class AlertDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Attention")
                    .setIcon(R.drawable.alert_icon)
                    .setMessage("Are you sure you want to sign out?");

            return builder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((BaseMenuActivity) getActivity()).signOut();
                        }
                    })
                    .setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dismiss();
                                }
                            })
                    .create();
        }
    }

    public void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())) {

        }
    }
}
