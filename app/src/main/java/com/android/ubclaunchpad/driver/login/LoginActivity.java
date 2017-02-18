package com.android.ubclaunchpad.driver.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.ActivityUtils;
import com.android.ubclaunchpad.driver.util.GPSchecker;
import com.android.ubclaunchpad.driver.util.Injection;

import butterknife.ButterKnife;

/**
 * This activity is the overall controller that creates and connects views and presenters.
 * Performs the binding of the presenter and the view. Could eventually be extended to
 * host multiple presenters, and having them communicate using an Event Bus.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginPresenter mLoginPresenter;
    private LocationManager locationManagerContext;
    private GPSchecker mGPSchecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        ButterKnife.bind(this);

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (loginFragment == null) {
            // create the fragment
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), loginFragment, R.id.content_frame);

            locationManagerContext = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            if(mGPSchecker == null) {
                mGPSchecker = new GPSchecker(locationManagerContext);
                           }

            if(!mGPSchecker.isLocationEnabled()){
                            showAlert();
            }
        }

        // create the presenter
        mLoginPresenter = new LoginPresenter(loginFragment);
        // use a dependency injection in order to provide any data that depends on a
        // Context to the presenter as we want to decouple it as much as
        // possible from the Android framework.
        mLoginPresenter.setGoogleApiClient(Injection.provideGoogleApiClient(this, mLoginPresenter));
                   }


                public void showAlert() {
             final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Enable Location")
                                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                                        "use this app")
                                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                               @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(myIntent);
                                    }
                            })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                               @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    }
                            });
              dialog.show();
    }
}
