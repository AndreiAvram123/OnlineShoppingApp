package com.example.onlineshoopingapp.view.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.onlineshoopingapp.R;
import com.example.onlineshoopingapp.model.Utilities;
import com.example.onlineshoopingapp.view.CustomDialog;
import com.example.onlineshoopingapp.view.fragments.ForgotPasswordFragment;
import com.example.onlineshoopingapp.view.fragments.LoginFragment;
import com.example.onlineshoopingapp.view.fragments.NoInternetFragment;
import com.example.onlineshoopingapp.view.fragments.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthenticationActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentCallbackInterface,
        SignUpFragment.SignUpFragmentCallback,
       //TODO
        //simplify the custom dialog interface
        CustomDialog.CustomDialogInterface,
        NoInternetFragment.NoInternetFragmentInterface
{

    private FirebaseAuth firebaseAuth;
    private CustomDialog customDialog;
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;
    //keep a tag in order to identify which fragment is not displayed
    private String currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        //onStart() runs before onCreate()
        //if this method is called then the user is not signed in ...
        prepareFragments();
        displayLoginFragment();

        if (!Utilities.isNetworkAvailable(this)) {
            //displayMessageDialog(getString(R.string.no_internet_connection));
            displayNoInternetFragment();
        }

    }

    private void displayNoInternetFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_auth, NoInternetFragment.newInstance())
                .commit();
    }

    private void displayVerificationEmailDialog() {
        customDialog = new CustomDialog(this, getString(R.string.email_verification_sent), this);
        customDialog.setButton1Message("ALRIGHT");
        customDialog.enableNegativeButton();
        customDialog.setButton2Message(getString(R.string.resend_verification_email));
        customDialog.show();
    }
    /**
     * This method is used to initialize the LoginFragment
     * and the SignUpFragment along with the fragmentManager
     */
    private void prepareFragments() {
        loginFragment = LoginFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
    }
    /**
     * This method pushes a login request with email and password to Firebase
     * IF we were able to log in the user,we check if his email address is verified
     * or not.If the email is not verified we call the method displayVerificationEmailDialog().If the
     * user has his email verified we call startMainActivity()
     * <p>
     * IF we were unable to log in the use we display a simple toast
     * telling him that the  login details are invalid
     *
     * @param email
     * @param password
     */
    private void pushLoginRequest(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            startMainActivity();
                        } else {
                            displayVerificationEmailDialog();
                        }
                    } else {
                        loginFragment.displayErrorMessage(getString(R.string.error_invalid_login_details));
                    }
                    loginFragment.toggleLoadingBar();
                });
    }

    /**
     * This method pushed a signUp request to Firebase in order
     * to register a new user with email and password.Moreover
     * after we successfully added a new user to our database
     * we update his profile
     *
     * @param email
     * @param password
     * @param nickname
     */
    private void pushSignUpRequest(String email, String password, String nickname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        updateNickname(nickname);
                    } else {
                        signUpFragment.displayErrorMessage(getString(R.string.error_create_account));
                    }
                    signUpFragment.toggleLoadingBar();
                });
    }

    private void updateNickname(String nickname) {
        firebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .build());
    }

    /******************TODO
     * ************************NEED TO CHANGE ********************
     */
    @Override
    public void button1Pressed() {
        customDialog.hide();
    }

    @Override
    public void button2Pressed() {
        firebaseAuth.getCurrentUser().sendEmailVerification();
        customDialog.hide();
        Toast.makeText(this, "WE HAVE SENT YOU A NEW EMAIL.", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    /**
     * This method is used to display the LoginFragment to the user
     */
    private void displayLoginFragment() {
       getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_auth, loginFragment)
                .addToBackStack(null)
                .commit();
       currentFragment = LoginFragment.FRAGMENT_TAG;

    }


    /**
     * Check if there is an user signed in
     * IF not keep this activity open
     * ELSE call the method startMainActivity()
     */
    @Override
    protected void onStart() {
        super.onStart();

          firebaseAuth = FirebaseAuth.getInstance();
     if (firebaseAuth.getCurrentUser() != null) {
           //start main activity if there is a user signed in
          startMainActivity();
     }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void login(String email, String password) {
      pushLoginRequest(email,password);
    }

    @Override
    public void showSignUpFragment() {
        if(Utilities.isNetworkAvailable(this)) {
            displaySignUpFragment();
        }else{
            displayNoInternetFragment();
        }
    }

    @Override
    public void showForgotPasswordFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_auth, ForgotPasswordFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Method used to show the SignUpFragment
     */
    private void displaySignUpFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_auth, signUpFragment)
                .addToBackStack(null)
                .commit();
     currentFragment = SignUpFragment.FRAGMENT_TAG;

    }

    @Override
    public void signUp(String email, String password, String nickname) {
        if (Utilities.isNetworkAvailable(this)) {
            pushSignUpRequest(email, password, nickname);
        } else {
            signUpFragment.toggleLoadingBar();
            displayNoInternetFragment();
        }
    }

    @Override
    public void retryInternetConnection() {
        if(Utilities.isNetworkAvailable(this)){
            if(currentFragment.equals(SignUpFragment.FRAGMENT_TAG)){
               displaySignUpFragment();
            }else {
                displayLoginFragment();
            }
        }
    }
}
