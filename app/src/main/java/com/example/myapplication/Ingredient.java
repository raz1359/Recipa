package com.example.myapplication;

public class Ingredient extends Object {

    private String id;

    public int getAmount() {
        return amount;
    }

    public void setAmount( int amount ) {
        this.amount = amount;
    }

    private int amount;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked( boolean checked ) {
        isChecked = checked;
    }

    boolean isChecked;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    private String name;

    public Ingredient(String id, String name) {

        this.id = id;
        this.name = name;
    }

    public Ingredient(boolean isChecked , int amount) {

        this.isChecked = isChecked;
        this.amount = amount;
    }

    public Ingredient(String name, boolean isChecked, int amount) {

        this.name = name;
        this.isChecked = isChecked;
        this.amount = amount;
    }
}
