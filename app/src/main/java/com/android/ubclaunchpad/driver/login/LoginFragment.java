package com.android.ubclaunchpad.driver.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.MainActivity;
import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.RegisterActivity;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.facebook.login.LoginManager;

import java.util.Collection;


/**
 * This fragment represents the [V]iew in the MVP architectural pattern.
 * Note how the view contains almost no code pertaining to the business
 * logic. Its main job is to delegate user input and pass data to
 * the presenter in order for it to be processed. Since the fragment
 * implements the View interface, the presenter can control this view
 * while being completely abstracted from the View's internal
 * implementation, allowing us to easily swap out this Fragment if needed.
 * It also gives us good cohesion as almost all of our Android
 * specific dependencies (like the View and Context class) are only
 * used in one layer.
 */
public class LoginFragment extends Fragment implements LoginContract.View, View.OnClickListener {

    private LoginContract.Presenter mPresenter;

    public LoginFragment() {
        // Requires empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void setPresenter(@NonNull LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Setup UI
        view.findViewById(R.id.signInButton).setOnClickListener(this /* the LoginFragment */);
        view.findViewById(R.id.registerButton).setOnClickListener(this /* the LoginFragment */);

        mPresenter.onCreate(getActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // fragment is now ready and in view, do initial setup (if any)
        mPresenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // passing data from the Android framework to the presenter.
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showInvalidEmailError() {
        /* No-op */
    }

    @Override
    public void showEmptyFieldError() {
        Toast.makeText(getContext(), "All fields must be filled in to continue", Toast.LENGTH_LONG).show();
        //TODO make better error
    }

    @Override
    public void showPasswordsNotEqualError() {
        /* No-op */
    }

    @Override
    public void showProgressDialog() {
        /* No-op */
    }

    @Override
    public void hideProgressDialog() {
        /* No-op */
    }

    @Override
    public void showGoogleAuthView(Intent signInIntent, int requestCode) {
        startActivityForResult(signInIntent, requestCode);
    }

    @Override
    public void showFacebookAuthView(Collection<String> permissions) {
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    @Override
    public void showMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button:
                // extend this to handle all the click events easily by specifying another case
                // based on the clicked view's unique id, as opposed to using messy anonymous
                // classes or strongly coupling the XML view to the business logic by declaring
                // the android:onClick attribute.

                // e.g. mPresenter.signInWithFacebook()
                break;
            case R.id.signInButton:
                Log.d(StringUtils.SignInActivity, "Clicked");
                String email = ((EditText)getView().findViewById(R.id.emailSignInEditText)).getText().toString();
                String password = ((EditText)getView().findViewById(R.id.signInPasswordEditText)).getText().toString();

                mPresenter.signInWithEmail(email, password);
                break;
            case R.id.registerButton:
                Log.d(StringUtils.SignInActivity, "Clicked");
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
        }
    }
}
