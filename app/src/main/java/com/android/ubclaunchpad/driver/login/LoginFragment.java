package com.android.ubclaunchpad.driver.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ubclaunchpad.driver.MainActivity;
import com.android.ubclaunchpad.driver.R;


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
 * <p/>
 * Created by Chris Li on 6/1/2016.
 */
public class LoginFragment extends Fragment implements LoginContract.View {

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

        View view = inflater.inflate(R.layout.fragment_example, container, false);

        // Setup UI

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
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showInvalidEmailError() {

    }

    @Override
    public void showEmptyFieldError() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showGoogleAuthView(Intent signInIntent, int requestCode) {
        startActivityForResult(signInIntent, requestCode);
    }

    @Override
    public void showMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
