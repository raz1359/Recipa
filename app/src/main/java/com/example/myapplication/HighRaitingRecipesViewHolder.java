package com.example.myapplication;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HighRaitingRecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "raz";
    TextView titleView; // TextView to display the title of the recipe
    ImageView imageView1;   // ImageView to display the image of the recipe
    View itemView2; // View representing the entire item view

    // Constructor for the ViewHolder
    public HighRaitingRecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize views from the item layout
        imageView1 = itemView.findViewById(R.id.image);
        imageView1.setOnClickListener(this); // Set click listener for the image
        titleView = itemView.findViewById(R.id.title);
        itemView.setOnClickListener(this); // Set click listener for the entire item view
        itemView2 = itemView; // Store the entire item view
    }

    // onClick method to handle click events on the views
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view.getId());
        Log.d(TAG, "HighRaitingRecipesViewHolder: " + itemView2.getId());



    }
}
