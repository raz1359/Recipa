package me.raz.recipa.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ingredient {

    private String id;
    private String name;
    private int amount;
    private boolean isChecked;

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
