package com.android.ubclaunchpad.driver.UI.register;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.ubclaunchpad.driver.BasePresenter;
import com.android.ubclaunchpad.driver.BaseView;
import com.android.ubclaunchpad.driver.MainApplication;
import com.android.ubclaunchpad.driver.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by Marina on 10.09.16.
 */
public interface RegisterContract {
    interface Presenter extends BasePresenter{
        void onCreate(Bundle savedInstanceState);

        void onStart();

        void onStop();

        void createAccount(final String name, final String email, final String postalCode, final String streetAddress, final String password);

        void signUp(String name, String email, String password1, String password2, String postalCode, String address);

    }
    interface View extends BaseView<Presenter>{
        User createUser();

        MainApplication getMainApplication();

        SharedPreferences getSharPref();

        void showRegistrationError(Task<AuthResult> task);

        void showSingUpError();

        void showPassword();

        AlertDialog setDialog();

        public boolean noEmptyBoxes(String name, String email, String passwordFirst, String passwordSecond);

        void startMainActivity();

    }
}

