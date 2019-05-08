package com.example.onlineshoopingapp.view.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.onlineshoopingapp.R;

/**
 * Simple Fragment which display a no connection error
 * Implement the interface in order to provide
 * a way for the user to retryInternetConnection his internet connection
 */

public class NoInternetFragment extends Fragment {
    private Button tryAgainButton;
    private NoInternetFragmentInterface noInternetFragmentInterface;
    public static final String FRAGMENT_TAG = "NO_INTERNET_FRAGMENT_TAG";

    public static NoInternetFragment newInstance(){
        return new NoInternetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View layout =inflater.inflate(R.layout.fragment_no_internet, container, false);
       noInternetFragmentInterface = (NoInternetFragmentInterface) getActivity();
       tryAgainButton = layout.findViewById(R.id.try_again_button);
       tryAgainButton.setOnClickListener(button-> noInternetFragmentInterface.retryInternetConnection());
       return layout;
    }

    public interface NoInternetFragmentInterface{
        void retryInternetConnection();
    }
}
