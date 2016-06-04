package com.android.ubclaunchpad.driver.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.android.ubclaunchpad.driver.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Enables injection of mock implementations at compile time.
 * This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies
 * and run a test hermetically. This also allows us
 * to maintain our MVP architecture as we do want to deal
 * with objects that rely on Contexts in the presenter.
 */
public class Injection {

    public static GoogleApiClient provideGoogleApiClient(@NonNull Context context, GoogleApiClient.OnConnectionFailedListener connectionFailedListener) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}
