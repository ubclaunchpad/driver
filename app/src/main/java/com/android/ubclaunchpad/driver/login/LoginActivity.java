package com.android.ubclaunchpad.driver.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.util.ActivityUtils;
import com.android.ubclaunchpad.driver.util.Injection;

/**
 * This activity is the overall controller that creates and connects views and presenters.
 * Performs the binding of the presenter and the view. Could eventually be extended to
 * host multiple presenters, and having them communicate using an Event Bus.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (loginFragment == null) {
            // create the fragment
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), loginFragment, R.id.content_frame);
        }

        // create the presenter
        mLoginPresenter = new LoginPresenter(loginFragment);
        // use a dependency injection in order to provide any data that depends on a
        // Context to the presenter as we want to decouple it as much as
        // possible from the Android framework.
        mLoginPresenter.setGoogleApiClient(Injection.provideGoogleApiClient(this, mLoginPresenter));
    }
}
