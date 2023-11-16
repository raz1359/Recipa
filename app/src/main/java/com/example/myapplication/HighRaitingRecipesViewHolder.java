package com.example.myapplication;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HighRaitingRecipesViewHolder extends RecyclerView.ViewHolder {

    TextView titleView;
    ImageView imageView1;

    public HighRaitingRecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView1 = itemView.findViewById(R.id.image);
        titleView = itemView.findViewById(R.id.title);

    }
}
