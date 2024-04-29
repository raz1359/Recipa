package me.raz.recipa.adapters;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import lombok.Setter;
import me.raz.recipa.R;

@Getter
@Setter
public class HighRaitingRecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "raz";
    private TextView titleView; // TextView to display the title of the recipe
    private ImageView imageView;   // ImageView to display the image of the recipe
    private View itemView; // View representing the entire item view

    // Constructor for the ViewHolder
    public HighRaitingRecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize views from the item layout
        imageView = itemView.findViewById(R.id.image);
        imageView.setOnClickListener(this); // Set click listener for the image
        titleView = itemView.findViewById(R.id.title);
        itemView.setOnClickListener(this); // Set click listener for the entire item view
        this.itemView = itemView; // Store the entire item view
    }

    // onClick method to handle click events on the views
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view.getId());
        Log.d(TAG, "HighRaitingRecipesViewHolder: " + itemView.getId());



    }

}
