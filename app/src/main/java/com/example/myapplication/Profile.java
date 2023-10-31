package com.example.myapplication;

public class Profile {

    String fullName, nickName, imageUri;

    public Profile(String fullName, String nickName, String imageUri) {
        this.fullName = fullName;
        this.nickName = nickName;
        this.imageUri = imageUri;
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


