package me.raz.recipa.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {

    // Fields for user profile
    private String fullName;
    private String nickName;
    private String imageUri;
    private String favourites;

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

}


