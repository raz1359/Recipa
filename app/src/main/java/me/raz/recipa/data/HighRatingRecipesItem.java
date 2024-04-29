package me.raz.recipa.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HighRatingRecipesItem {

    // Fields representing the properties of a recipe item
    private String id;
    private String name;
    private String image;
    private boolean isFavourite;


    // Default constructor for Firebase
    public HighRatingRecipesItem() {
        // Firebase constructor
    }

    // Parameterized constructor to initialize the fields with values
    public HighRatingRecipesItem(String name, String image, String id, boolean isFavourite) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.isFavourite = isFavourite;
    }

    @Override
    public String toString() {
        return image + " " + name + " " + id + " " + isFavourite;
    }
    
}


