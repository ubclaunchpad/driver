package com.android.ubclaunchpad.driver.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
                new AlertDialogFragment().show(getFragmentManager(),"signOutAlertDialog");
                return true;

            case R.id.action_edit_profile:
                Log.v(TAG, "editing profile");
                Toast.makeText(getApplicationContext(),"editing profile",Toast.LENGTH_LONG).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut(){
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d(TAG, "Signing Out");
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
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
}
