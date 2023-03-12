package com.example.myapplication;

import android.graphics.Bitmap;

public class Item {

    private String title;
    private String date;
    private String subtitle;
    private Bitmap bitmap;

    // constructor
    public Item(String title, String date, String subtitle, Bitmap bitmap) {

        this.title = title;
        this.date = date;
        this.subtitle = subtitle;
        this.bitmap = bitmap;
    }

    // getters and setters
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
