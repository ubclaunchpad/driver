package com.android.ubclaunchpad.driver;

import com.android.ubclaunchpad.driver.login.LoginContract;
import com.android.ubclaunchpad.driver.login.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link LoginPresenter}
 * Observe how much easier it is to run unit tests on our business logic.
 */
public class LoginPresenterUnitTest {

    private LoginPresenter mLoginPresenter;

    @Mock
    private LoginContract.View mLoginView;

    @Before
    public void setupLoginPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mLoginPresenter = new LoginPresenter(mLoginView);
    }

    @Test
    public void signInWithEmailWithEmptyFields() { // use verbose method names for tests.
        mLoginPresenter.signInWithEmail("", "");
        verify(mLoginView, atMost(2)).showEmptyFieldError(); // verify that a method was invoked.
    }

    @Test
    public void signInWithEmailWithEmptyPassword() {
        mLoginPresenter.signInWithEmail("test@email.com", "");
        verify(mLoginView).showEmptyFieldError();
    }

    @Test
    public void signInWithEmailWithEmptyEmail() {
        mLoginPresenter.signInWithEmail("", "password");
        verify(mLoginView).showEmptyFieldError();
    }

    @Test
    public void signInWithEmailWithInvalidEmailAndEmptyPassword() {
        mLoginPresenter.signInWithEmail("test", "");
        verify(mLoginView).showInvalidEmailError();
        verify(mLoginView).showEmptyFieldError();
    }

    @Test
    public void createUserWithEmailWithEmptyFields() {
        mLoginPresenter.createUserWithEmail("", "", "");
        verify(mLoginView, atMost(3)).showEmptyFieldError();
    }

    @Test
    public void createUserWithEmailWithEmptyEmail() {
        mLoginPresenter.createUserWithEmail("", "password", "password");
        verify(mLoginView).showEmptyFieldError();
    }

    @Test
    public void createUserWithEmailWithInvalidEmail() {
        mLoginPresenter.createUserWithEmail("tester", "password", "password");
        verify(mLoginView).showInvalidEmailError();
    }

    @Test
    public void createUserWithEmailWithEmptyConfirmPassword() {
        mLoginPresenter.createUserWithEmail("tester@email.com", "password", "");
        verify(mLoginView).showEmptyFieldError();
    }

    @Test
    public void createUserWithEmailWithUnequalPasswords() {
        mLoginPresenter.createUserWithEmail("tester@email.com", "password", "password1");
        verify(mLoginView).showPasswordsNotEqualError();
    }

}