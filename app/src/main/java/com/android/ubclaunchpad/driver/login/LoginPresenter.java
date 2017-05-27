package com.android.ubclaunchpad.driver.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.UI.DestinationActivity;
import com.android.ubclaunchpad.driver.models.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.PreferenceHelper;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

/**
 * The [P]resenter in the MVP architectural pattern. This class
 * should contain almost all of the business logic. It relies on
 * the abstracted View to do all the Android specific UI work.
 */
public class LoginPresenter implements LoginContract.Presenter, FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private static final int REQUEST_GOOGLE_SIGN_IN = 9001;

    private final LoginContract.View mLoginView;

    private FirebaseAuth.AuthStateListener authStateListener;

    private GoogleApiClient mGoogleApiClient;

    private CallbackManager mCallbackManager;

    private FragmentActivity mContext;

    /**
     * Pass a reference of the view to the Presenter. We use this reference
     * to modify the view's state.
     *
     * @param exampleView the view (fragment implementing the View interface.)
     */
    public LoginPresenter(@NonNull LoginContract.View exampleView) {
        mLoginView = exampleView;
        mLoginView.setPresenter(this);
    }

    // method not defined in contract, as this method is used for dependency injection.
    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    /**
     * For clarity, this method currently is being invoked from the Fragment's onResume().
     */
    @Override
    public void start() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, this);
    }

    @Override
    public void onCreate(FragmentActivity fragContext){
        this.mContext = fragContext;

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseUtils.getFirebaseUser() != null){
                    FirebaseUtils.getDatabase().child(StringUtils.FirebaseUserEndpoint).child(FirebaseUtils.getFirebaseUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if(user != null){
                                //Set user to the manager
                                UserManager userManager = UserManager.getInstance();
                                userManager.setUser(user);

                                try {
                                    PreferenceHelper.getPreferenceHelperInstance().put(StringUtils.FirebaseUidKey, FirebaseUtils.getFirebaseUser().getUid());
                                }
                                catch (Exception e){
                                    Log.e(TAG, "Could not save firebase key:" + e.getMessage());
                                }

//                                mContext.startActivity(new Intent(mContext, DestinationActivity.class));
//                                mContext.finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(mContext, "Login error. Please try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
    }

    @Override
    public void onStart() {
        FirebaseUtils.getFirebaseAuth().addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        if (authStateListener != null) {
            FirebaseUtils.getFirebaseAuth().removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_GOOGLE_SIGN_IN == requestCode && Activity.RESULT_OK == resultCode) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i(TAG, "here?");
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.e(TAG, result.getStatus().getStatusMessage());
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Method shouldn't be in the LoginPresenter and is only here for brevity.
     * Move this method to a different presenter moving forward.
     *
     * @param email
     * @param password
     * @param confirmPassword
     */
    @Override
    public void createUserWithEmail(String email, String password, String confirmPassword) {

        if (!validateInputs(email, password, confirmPassword)) {
            return;
        }

        mLoginView.showProgressDialog();
        FirebaseUtils.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in succeeds the auth state listener
                        // will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "Authentication failed.");
                        }

                        mLoginView.hideProgressDialog();
                    }
                });
    }

    private boolean validateInputs(String email, String password, @Nullable String confirmPassword) {

        boolean valid = true;

        if (StringUtils.isEmpty(email)) { // we use StringUtils as opposed to TextUtils for easy testing.
            mLoginView.showEmptyFieldError();
            valid = false;
        } else {
            if (!StringUtils.isValidEmail(email)) {
                mLoginView.showInvalidEmailError();
                valid = false;
            }
        }

        if (StringUtils.isEmpty(password)) {
            mLoginView.showEmptyFieldError();
            valid = false;
        }

        if (confirmPassword != null) {
            if (StringUtils.isEmpty(confirmPassword)) {
                mLoginView.showEmptyFieldError();
                valid = false;
            }

            if (!StringUtils.equals(password, confirmPassword)) {
                mLoginView.showPasswordsNotEqualError();
                valid = false;
            }
        }

        return valid;
    }

    @Override
    public void signInWithEmail(String email, String password) {

        if (!validateInputs(email, password, null)) {
            return;
        }

        mLoginView.showProgressDialog();
        FirebaseUtils.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(mContext, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        } else {
                            mContext.startActivity(new Intent(mContext, DestinationActivity.class));
                            mContext.finish();
                        }

                        mLoginView.hideProgressDialog();
                    }
                });
    }

    @Override
    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mLoginView.showGoogleAuthView(signInIntent, REQUEST_GOOGLE_SIGN_IN);
    }

    @Override
    public void signInWithFacebook() {
        mLoginView.showFacebookAuthView(Arrays.asList("email", "public_profile"));
    }


    /**
     * ! UNTESTED !
     * Method shouldn't be in the LoginPresenter and is only here for brevity.
     * Move this method to the a different presenter moving forward and ensure
     * each sign out method (Google and Facebook) works as intended.
     */
    @Override
    public void signOut() {
        FirebaseUtils.getFirebaseAuth().signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {

                        }
                    }
                });

        // Facebook sign out.
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (FirebaseUtils.getFirebaseUser() != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + FirebaseUtils.getFirebaseUser().getUid());
            mLoginView.showMainActivity();

        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    /**
     * Google API callback
     **/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /* No-op */
    }

    /**
     * Facebook API callbacks
     **/
    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.d(TAG, "facebook:onSuccess:" + loginResult);
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "facebook:onCancel");
    }

    @Override
    public void onError(FacebookException error) {
        Log.d(TAG, "facebook:onError", error);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        mLoginView.showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseUtils.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                        }

                        mLoginView.hideProgressDialog();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        mLoginView.showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        FirebaseUtils.getFirebaseAuth()
                .signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                        }

                        mLoginView.hideProgressDialog();
                    }
                });
    }
}
