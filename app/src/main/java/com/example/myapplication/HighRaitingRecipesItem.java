package com.example.myapplication;

import android.graphics.Bitmap;

public class HighRaitingRecipesItem {

    String name;
    Bitmap image;

    public HighRaitingRecipesItem(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
