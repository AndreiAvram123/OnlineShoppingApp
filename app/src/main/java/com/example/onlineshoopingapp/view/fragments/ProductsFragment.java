package com.example.onlineshoopingapp.view.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.onlineshoopingapp.R;
import com.example.onlineshoopingapp.model.Product;
import com.example.onlineshoopingapp.model.ProductsAdapter;
import com.example.onlineshoopingapp.view.CustomDivider;

import java.util.ArrayList;

/**
 * Use this fragment whenever you want to display a list of products
 * WHEN CREATING AN INSTANCE OF THIS FRAGMENT YOU CAN SPECIFY WEATHER OR
 * NOT YOU WANT TO HAVE A CLOSE BUTTON
 */
public class ProductsFragment extends Fragment
{
    private ArrayList<Product> products;
    private RecyclerView recyclerView;
    private static final String PRODUCTS_ARRAY_KEY= "PRODUCTS_ARRAY_KEY";
    private static final String KEY_BUTTON_ENABLED = "KEY_BUTTON";

    public static ProductsFragment newInstance(ArrayList<Product>products,boolean enableCloseButton){
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putParcelableArrayList(PRODUCTS_ARRAY_KEY,products);
        argumentsBundle.putBoolean(KEY_BUTTON_ENABLED,enableCloseButton);
        productsFragment.setArguments(argumentsBundle);
        return productsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.products_fragment_layout,container,false);
      products = getArguments().getParcelableArrayList(PRODUCTS_ARRAY_KEY);
      recyclerView = layout.findViewById(R.id.recycler_view_products);

      //check if we should display the close button or not
       boolean enableCloseButton = getArguments().getBoolean(KEY_BUTTON_ENABLED);
       if(enableCloseButton){
           ImageView closeIcon = layout.findViewById(R.id.close_icon_products_fragment);
           closeIcon.setOnClickListener(view ->getFragmentManager().popBackStack());
       }

      populateRecyclerView(container.getContext());
      return layout;
    }

    private void populateRecyclerView(Context context) {
      ProductsAdapter productsAdapter = new ProductsAdapter(products, getActivity());
      recyclerView.setAdapter(productsAdapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
      recyclerView.addItemDecoration(new CustomDivider(20));
    }



}
