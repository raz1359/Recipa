package com.example.myapplication;

import android.graphics.Bitmap;

public class HighRaitingRecipesItem {

    // Fields representing the properties of a recipe item
    String name,image,id;
    boolean isFavourite;


    // Default constructor for Firebase
    public HighRaitingRecipesItem() {
        // Firebase constructor
    }

    // Parameterized constructor to initialize the fields with values
    public HighRaitingRecipesItem(String name, String image, String id, boolean isFavourite) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.isFavourite = isFavourite;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {

        return image+" "+name+" "+id + isFavourite;
    }
}


