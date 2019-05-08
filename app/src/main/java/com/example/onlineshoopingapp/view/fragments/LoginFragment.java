package com.example.onlineshoopingapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlineshoopingapp.R;
import com.example.onlineshoopingapp.model.Utilities;


public class LoginFragment extends AuthenticationFragment {

    private TextView errorMessage;
    private EditText emailField;
    private EditText passwordField;
    private ProgressBar loadingBar;
    private Button signInButton;
    private TextView signUpText;
    private LoginFragmentCallbackInterface loginFragmentCallbackInterface;
    private TextView forgotPassword;
    public static final String FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        loginFragmentCallbackInterface = (LoginFragmentCallbackInterface) getActivity();
        initializeUI(layout);
        return layout;
    }


    /**
     * ACTION - LOGIN
     * This method gets the text from the emailField and the
     * passwordField and then calls the method areLoginDetailsValid()
     * if the method called return true then call loginFragmentCallbackInterface.loginUser()
     */

    @Override
    void attemptAction() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (areLoginDetailsValid(email, password)) {
            toggleLoadingBar();
            loginFragmentCallbackInterface.login(email, password);
        }
    }

    @Override
    void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    @Override
    void initializeUI(View layout) {
        initializeViews(layout);
        configureButtons();

    }


    @Override
    void configureButtons() {
        signUpText.setOnClickListener(view -> loginFragmentCallbackInterface.showSignUpFragment());
        signInButton.setOnClickListener(view -> attemptAction());
    }


    private void initializeViews(View layout) {
        emailField = layout.findViewById(R.id.email_edit_text_main);
        passwordField = layout.findViewById(R.id.password_edit_text_main);
        errorMessage = layout.findViewById(R.id.error_message_text_view);
        loadingBar = layout.findViewById(R.id.logging_progress_bar);
        signInButton = layout.findViewById(R.id.sign_in_button);
        signUpText = layout.findViewById(R.id.sign_up_text);
        forgotPassword = layout.findViewById(R.id.forgot_password_text_view);
        forgotPassword.setOnClickListener(view -> loginFragmentCallbackInterface.showForgotPasswordFragment());


        setTextViewColor(emailField,layout.findViewById(R.id.email_hint_login));
        setTextViewColor(passwordField,layout.findViewById(R.id.password_hint_login));


    }


    /**
     * Once the user has pressed the signInButton and
     * the credentials are valid it may take some time
     * until Firebase processes our login request(usually this does not happen)
     * We hide the signInButton and the signUpButton and show the loadingBar
     * until Firebase has processed the request
     * THIS METHOD IS ALSO CALLED FROM @activity StartScreenActivity
     */
    public void toggleLoadingBar() {

        if (signInButton.getVisibility() == View.VISIBLE) {
            signInButton.setVisibility(View.INVISIBLE);
            loadingBar.setVisibility(View.VISIBLE);
            //make sure the user does not press the text
            signUpText.setClickable(false);

        } else {
            signUpText.setClickable(true);
            signInButton.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * This method is used to check if the login
     * details are valid or not.We need to check the following:
     * <p>
     * If the email is valid using the method from the Utilities class
     * (the email should have the following format [a-zA-Z0-9]+@[a-z]+\.[a-z]+)
     * <p>
     * If the password field is not empty and the length of the password is AT LEST 6
     * characters( Firebase does not allow password that have less that 6 characters)
     *
     * @return
     */
    private boolean areLoginDetailsValid(String email, String password) {
        if (!Utilities.isEmailValid(email)) {
            displayErrorMessage("Please enter a valid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            displayErrorMessage("Please enter a password");
            return false;
        }
        return true;

    }

    @Override
    public void displayErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

    public interface LoginFragmentCallbackInterface {
        void login(String email, String password);
        void showSignUpFragment();
        void showForgotPasswordFragment();

    }


}
