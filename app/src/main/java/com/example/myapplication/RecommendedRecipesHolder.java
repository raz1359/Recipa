package com.example.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendedRecipesHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "raz";
    TextView titleView;
    ImageView imageView1;
    ImageView likeBtn;

    public RecommendedRecipesHolder(@NonNull View itemView) {
        super(itemView);

        imageView1 = itemView.findViewById(R.id.image);
        titleView = itemView.findViewById(R.id.title);
        likeBtn = itemView.findViewById(R.id.ibHeart);



    }
}
