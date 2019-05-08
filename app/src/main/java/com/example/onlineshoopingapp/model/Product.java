package com.example.onlineshoopingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Simple class in order to emulate a product
 * from an online store
 */
public class Product implements Parcelable {
    private String name;
    private double price;
    private String[] firebaseImages;
    private int productsInStock;
    private int rating;
    private String QRValue;

    /**
     * Constructor used to create a new Product
     * THE ATTRIBUTE QRValue is optional
     * @param name
     * @param price
     * @param firebaseImages
     * @param productsInStock
     * @param rating
     */
    public Product(String name, double price, String[] firebaseImages, int productsInStock, int rating, @Nullable String QRValue) {
        this.name = name;
        this.price = price;
        this.firebaseImages = firebaseImages;
        this.productsInStock = productsInStock;
        this.rating = rating;
        this.QRValue = QRValue;
    }



    public Product(Parcel inputData) {
        name = inputData.readString();
        price = inputData.readDouble();
        firebaseImages = inputData.createStringArray();
        productsInStock = inputData.readInt();
        rating = inputData.readInt();
        QRValue = inputData.readString();
    }

    //method not useful but need overriding
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeStringArray(firebaseImages);
        parcel.writeInt(productsInStock);
        parcel.writeInt(rating);
        parcel.writeString(QRValue);
    }

    //the Parcelable interface requires a field name CREATOR
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel parcel) {
            return new Product(parcel);
        }

        @Override
        public Product[] newArray(int i) {
            return new Product[i];
        }
    };

    public String getQRValue() {
        return QRValue;
    }

    public void setQRValue(String QRValue) {
        this.QRValue = QRValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String[] getFirebaseImages() {
        return firebaseImages;
    }

    public void setFirebaseImages(String[] firebaseImages) {
        this.firebaseImages = firebaseImages;
    }

    public int getProductsInStock() {
        return productsInStock;
    }

    public void setProductsInStock(int productsInStock) {
        this.productsInStock = productsInStock;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
