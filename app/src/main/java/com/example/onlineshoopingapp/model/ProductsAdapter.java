package com.example.onlineshoopingapp.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshoopingapp.R;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private ArrayList<Product> productsToDisplay;
    private Context context;
    private ProductsAdapterInterface productsAdapterInterface;

    public interface ProductsAdapterInterface{
        void expandProduct(Product product);
    }

    public ProductsAdapter(ArrayList<Product> productsToDisplay, Activity activity){
        this.productsToDisplay = productsToDisplay;
        productsAdapterInterface = (ProductsAdapterInterface) activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.item_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productName.setText(productsToDisplay.get(position).getName());
        holder.productPrice.setText("£"+productsToDisplay.get(position).getPrice());
        //get the image with glide from Firebase
        Glide.with(context)
                .load(productsToDisplay.get(position).getFirebaseImages()[0])
                .into(holder.productImage);

        //set a listener on the item
        holder.itemView.setOnClickListener(layout->
                productsAdapterInterface.expandProduct(productsToDisplay.get(position)));
    }

    @Override
    public int getItemCount() {
        return productsToDisplay.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      ImageView productImage;
      TextView  productPrice;
      TextView  productName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productPrice = itemView.findViewById(R.id.product_price);
            productName = itemView.findViewById(R.id.product_name);
        }



    }
}
