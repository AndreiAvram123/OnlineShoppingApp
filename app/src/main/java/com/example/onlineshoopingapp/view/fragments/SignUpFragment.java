package com.example.onlineshoopingapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlineshoopingapp.R;
import com.example.onlineshoopingapp.model.Utilities;


/**
 * This fragment is used in order to provide a way of signing up the
 * user.
 * This fragment is build on top of the Authentication fragment
 *
 * !!!!!! IMPLEMENTING THIS FRAGMENT REQUIRES IMPLEMENTING THE FRAGMENT INTERFACE AS WELL!!!!
 * CALL THE METHOD toggleLoadingBar() once you have
 * successfully/unsuccessfully completed the sign up request
 */

public class SignUpFragment extends AuthenticationFragment {

    private EditText emailField;
    private EditText passwordField;
    private EditText reenteredPasswordField;
    private EditText nicknameField;
    private Button finishButton;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private ImageView backImage;
    private SignUpFragmentCallback signUpFragmentCallback;
    public static final String FRAGMENT_TAG = "SIGN_UP_FRAGMENT_TAG";


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUpFragmentCallback = (SignUpFragmentCallback) getActivity();


        initializeUI(layout);

        return layout;

    }


    @Override
    void configureButtons() {
        finishButton.setOnClickListener(view -> attemptAction());
        backImage.setOnClickListener(image -> getFragmentManager().popBackStack());
    }


    @Override
    public void displayErrorMessage(String message) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
    }

    /**
     * Once the user has pressed the finishButton and
     * the credentials are valid it may take some time
     * until Firebase processes our SignUp request(usually this does not happen)
     * We hide the finishButton  and show the loadingBar
     * until Firebase has processed the request
     * !!!!!!!!!
     * THIS METHOD SHOULD BE CALLED ONCE THE SIGN UP OPERATION HAS
     * BEEN COMPLETED !!!!
     */
    @Override
    public void toggleLoadingBar() {
        if (finishButton.getVisibility() == View.VISIBLE) {
            finishButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            finishButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        clearFields();
    }


    /**
     * Method used in order to initialize all the views inside the fragment layout
     */
    private void initializeViews(View layout) {

        emailField = layout.findViewById(R.id.email_field_sign_up);
        passwordField = layout.findViewById(R.id.password_field_sign_up);
        reenteredPasswordField = layout.findViewById(R.id.reentered_password_field);
        nicknameField = layout.findViewById(R.id.nickname_field);

        finishButton = layout.findViewById(R.id.finish_sign_up);
        progressBar = layout.findViewById(R.id.loading_progress_bar_sign_up);
        errorTextView = layout.findViewById(R.id.error_message_sign_up);
        backImage = layout.findViewById(R.id.back_image_sign_up);

        configureButtons();

        setTextViewColor(emailField, layout.findViewById(R.id.hint_enter_email));
        setTextViewColor(passwordField, layout.findViewById(R.id.hint_enter_password));
        setTextViewColor(reenteredPasswordField, layout.findViewById(R.id.hint_reenter_password));
        setTextViewColor(nicknameField, layout.findViewById(R.id.hint_enter_nickname));

    }





    private boolean areCredentialsValid(String email, String password, String reenteredPassword,
                                        String nickname) {

        if (!Utilities.isEmailValid(email)) {
            displayErrorMessage(getString(R.string.error_invalid_email));
            return false;
        }
        if (password.isEmpty()) {
            displayErrorMessage(getString(R.string.no_password));
            return false;
        }
        if (!password.equals(reenteredPassword)) {
            displayErrorMessage(getString(R.string.password_match));
            return false;
        }
        if (nickname.isEmpty()) {
            displayErrorMessage(getString(R.string.error_no_nickname));
        }
        return true;

    }


    /**
     * ACTION - SIGN UP
     */
    @Override
    void attemptAction() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String reenteredPassword = reenteredPasswordField.getText().toString().trim();
        String nickname = nicknameField.getText().toString().trim();

        if (areCredentialsValid(email, password, reenteredPassword, nickname)) {
            toggleLoadingBar();
            signUpFragmentCallback.signUp(email, password, nickname);

        }
    }



    @Override
    void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        reenteredPasswordField.setText("");
        nicknameField.setText("");
    }

    @Override
    void initializeUI(View layout) {
        initializeViews(layout);
        configureButtons();
    }




    public interface SignUpFragmentCallback {
        void signUp(String email, String password, String nickname);
    }

}
