package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter <shoppingListRvHolder> {

    Context context;
    List<Ingredient> list;

    String TAG = "raz";

    //Constructor for the adapter
    public ShoppingListAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public shoppingListRvHolder onCreateViewHolder( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredien_item, parent, false);
        return new shoppingListRvHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull shoppingListRvHolder holder , int position ) {
        Ingredient ingredient = list.get(position);

        holder.name.setText(ingredient.getName());
        holder.amount.setText("X" + ingredient.getAmount());
        holder.isChecked.setEnabled(ingredient.isChecked());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
