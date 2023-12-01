package com.example.myapplication;

public class Profile {

    String fullName, nickName, imageUri, favourites;

    public Profile(String fullName, String nickName, String imageUri, String favourites) {
        this.fullName = fullName;
        this.nickName = nickName;
        this.imageUri = imageUri;
        this.favourites = favourites;
    }

    public String getFavourites() {
        return favourites;
    }

    public void setFavourites(String favourites) {
        this.favourites = favourites;
    }

    public Profile(){
        //Firebse empty constractor
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


