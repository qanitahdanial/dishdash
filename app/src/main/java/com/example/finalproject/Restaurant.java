package com.example.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Restaurant implements Parcelable {
    private int id;
    private String name;
    private String location;
    private String cuisineType;
    private String price; // Price as String
    private String rating; // Rating as String to match the database storage
    private String menuLink;
    private int rank;

    // Constructor
    public Restaurant(int id, String name, String location, String cuisineType, String price, String rating, String menuLink) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cuisineType = cuisineType;
        this.price = price;
        this.rating = rating;
        this.menuLink = menuLink;
    }

    // Parcelable Constructor
    protected Restaurant(Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
        cuisineType = in.readString();
        price = in.readString();
        rating = in.readString();
        menuLink = in.readString();
        rank = in.readInt();
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(cuisineType);
        dest.writeString(price);
        dest.writeString(rating);
        dest.writeString(menuLink);
        dest.writeInt(rank);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public double getRatingAsDouble() {
        try {
            return Double.parseDouble(rating); // Converts rating to double if needed
        } catch (NumberFormatException e) {
            return 0.0; // Default to 0.0 if the rating is invalid
        }
    }

    public String getMenuLink() {
        return menuLink;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
