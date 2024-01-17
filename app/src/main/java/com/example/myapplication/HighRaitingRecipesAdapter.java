package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HighRaitingRecipesAdapter extends RecyclerView.Adapter<HighRaitingRecipesViewHolder> {

    // Context to be used in the adapter
    Context context;
    // List of high rating recipes to be displayed
    List<HighRaitingRecipesItem> list;
    // Tag for logging purposes
    private static final String TAG = "raz";


    // Constructor to initialize the context and list of high rating recipes
    public HighRaitingRecipesAdapter(Context context, List<HighRaitingRecipesItem> list) {
        this.context = context;
        this.list = list;
        Log.d(TAG, "HighRaitingRecipesAdapter: 33");
    }

    // Inflates the layout for each item view in the RecyclerView
    @NonNull
    @Override
    public HighRaitingRecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HighRaitingRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.high_raiting_recipes, parent, false));
    }

    // Binds the data to the views in each item view
    @Override
    public void onBindViewHolder(HighRaitingRecipesViewHolder holder, int position) {
        // Get the current high rating recipes item
        HighRaitingRecipesItem highRaitingRecipesItem = list.get(position);

        // Set the recipe name to the TextView
        holder.titleView.setText(highRaitingRecipesItem.getName());

        // Load and display the recipe image using Picasso
        Picasso.get().load(highRaitingRecipesItem.getImage()).resize(500,500).centerCrop().into(holder.imageView1);

        // Set click listeners for the item view and the recipe image to open the RecipePageActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), RecipePageActivity.class);

                Log.d(TAG, "onClick id: " + list.get(position).getId());
                intent.putExtra("id", list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }


        });

        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), RecipePageActivity.class);

                Log.d(TAG, "onClick id: " + list.get(position).getId());
                intent.putExtra("id", list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return list.size();
    }
}
