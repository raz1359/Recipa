package com.example.myapplication;

public class Profile {

    // Fields for user profile
    String fullName, nickName, imageUri, favourites;

    // Constructor to initialize profile fields
    public Profile(String fullName, String nickName, String imageUri, String favourites) {
        this.fullName = fullName;
        this.nickName = nickName;
        this.imageUri = imageUri;
        this.favourites = favourites;
    }
    // Empty constructor for Firebase (required for data mapping)
    public Profile(){
        // Firebase empty constructor
    }

    // add Getters and Setters
    public String getFavourites() {
        return favourites;
    }

    public void setFavourites(String favourites) {
        this.favourites = favourites;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}


