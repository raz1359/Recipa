package me.raz.recipa.data;

import android.graphics.Bitmap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private String title; // Title of the item
    private String date; // Date associated with the item
    private String subtitle; // Subtitle or additional information about the item
    private Bitmap bitmap; // Bitmap image associated with the item

    // Constructor for creating an Item
    public Item(String title, String date, String subtitle, Bitmap bitmap) {

        this.title = title;
        this.date = date;
        this.subtitle = subtitle;
        this.bitmap = bitmap;
    }

}
