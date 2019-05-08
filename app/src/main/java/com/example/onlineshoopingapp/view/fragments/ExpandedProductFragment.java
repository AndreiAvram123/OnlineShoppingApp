package com.example.onlineshoopingapp.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.onlineshoopingapp.R;
import com.example.onlineshoopingapp.model.Product;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class ExpandedProductFragment extends Fragment {

    private Product product;
    private CarouselView carouselView;
    private static final String OBJECT_KEY ="OBJECT_KEY";
    //use this listener to load the images and set the scale type

    ImageListener imageListener = (position, imageView) -> {
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //use glide to load the image
        Glide.with(getContext())
                .load(product.getFirebaseImages()[position])
                .into(imageView);

    };

    public static ExpandedProductFragment newInstance(@NonNull Product product){
        ExpandedProductFragment expandedProductFragment = new ExpandedProductFragment();
        //create a bundle and store the current product
        Bundle bundle = new Bundle();
        bundle.putParcelable(OBJECT_KEY,product);
        expandedProductFragment.setArguments(bundle);
        return expandedProductFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =inflater.inflate(R.layout.fragment_expanded_product, container, false);
        carouselView = layout.findViewById(R.id.carouselView);
        product = getArguments().getParcelable(OBJECT_KEY);

        ImageView backIcon = layout.findViewById(R.id.back_icon_expanded);

        backIcon.setOnClickListener(view -> getFragmentManager().popBackStack());

        createCarouselView();
        return layout;
    }

    /**
     * Method used to create a slide show
     *
     */
    private void createCarouselView() {
        carouselView.setPageCount(product.getFirebaseImages().length);
        carouselView.setImageListener(imageListener);
        carouselView.setSlideInterval(0);

    }



    }




