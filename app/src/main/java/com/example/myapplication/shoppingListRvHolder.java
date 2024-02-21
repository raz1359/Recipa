package com.example.myapplication;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class shoppingListRvHolder extends RecyclerView.ViewHolder {

    CheckBox isChecked;
    TextView name,amount;
    public shoppingListRvHolder( @NonNull View itemView ) {
        super(itemView);

        isChecked = itemView.findViewById(R.id.checked);
        name = itemView.findViewById(R.id.ingredientNameShoppingList);
        amount = itemView.findViewById(R.id.amountTv);
    }
}
