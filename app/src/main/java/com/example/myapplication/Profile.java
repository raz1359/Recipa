package com.example.myapplication;

public class Profile {
    
    public String Fullname;
    public String Nickname;
    public String ProfileImage;


    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Profile(String fullname, String nickname, String profileImage) {
        Fullname = fullname;
        Nickname = nickname;
        ProfileImage = profileImage;
    }


    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }
}


