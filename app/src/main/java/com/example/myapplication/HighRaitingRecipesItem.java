package com.example.myapplication;

import android.graphics.Bitmap;

public class HighRaitingRecipesItem {

    String name,image,id;


    public HighRaitingRecipesItem() {
        // firebase
    }

    public HighRaitingRecipesItem(String name, String image, String id) {
        this.name = name;
        this.id = id;
        this.image = image;
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
}


