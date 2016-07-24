package com.android.ubclaunchpad.driver.login;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.android.ubclaunchpad.driver.BasePresenter;
import com.android.ubclaunchpad.driver.BaseView;

import java.util.Collection;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoginContract {

    interface Presenter extends BasePresenter {

        // we can expose activity/fragment lifecycle events to the presenter
        void onCreate(FragmentActivity context);

        void onStart();

        void onStop();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void createUserWithEmail(String email, String password, String confirmPassword);

        void signInWithEmail(String email, String password);

        void signInWithGoogle();

        void signInWithFacebook();

        void signOut();
    }

    interface View extends BaseView<Presenter> {

        void showInvalidEmailError();

        void showEmptyFieldError();

        void showPasswordsNotEqualError();

        void showProgressDialog();

        void hideProgressDialog();

        void showGoogleAuthView(Intent signInIntent, int requestCode);

        void showFacebookAuthView(Collection<String> permissions);

        void showMainActivity();

    }
}
