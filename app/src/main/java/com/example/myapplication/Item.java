package com.example.myapplication;

import android.graphics.Bitmap;

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

    // Getters and Setters
    public String getTitle() {
        return title;}

    public void setTitle(String title) {
        this.title = title;}

    public String getDate() {
        return date;}

    public void setDate(String date) {
        this.date = date;}

    public String getSubtitle() {
        return subtitle;}

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;}

    public Bitmap getBitmap() {
        return bitmap;}

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;}
}
