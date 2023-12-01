package com.example.myapplication;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HighRaitingRecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "raz";
    TextView titleView;
    ImageView imageView1;
    View itemView2;

    public HighRaitingRecipesViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView1 = itemView.findViewById(R.id.image);
        imageView1.setOnClickListener(this);
        titleView = itemView.findViewById(R.id.title);
        itemView.setOnClickListener(this);
        itemView2 = itemView;



    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: " + view.getId());
        Log.d(TAG, "HighRaitingRecipesViewHolder: " + itemView2.getId());



    }
}
