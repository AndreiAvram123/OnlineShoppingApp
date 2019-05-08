package com.example.onlineshoopingapp.view.fragments;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * This is a base class used to build fragments
 * such as a LOGIN fragment or a Sign up Fragment
 */
public abstract class AuthenticationFragment extends Fragment {


    abstract void attemptAction();

    abstract void clearFields();

    abstract void initializeUI(View layout);

    protected  void setTextViewColor(EditText editText, TextView textView) {
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus){
                textView.setTextColor(Color.parseColor("#05AFF2"));
            }else{
                textView.setTextColor(Color.parseColor("#000000"));
            }
        });
    }

    abstract void configureButtons();


    public abstract void displayErrorMessage(String message);

    public abstract void toggleLoadingBar();



}
