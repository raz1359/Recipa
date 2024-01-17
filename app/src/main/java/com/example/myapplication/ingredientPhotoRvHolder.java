package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ingredientPhotoRvHolder extends RecyclerView.ViewHolder {

   TextView text;

    // Constructor for the ViewHolder
    public ingredientPhotoRvHolder(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.ingredientName);

    }

}
