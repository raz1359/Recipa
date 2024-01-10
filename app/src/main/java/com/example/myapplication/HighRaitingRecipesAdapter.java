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

    Context context;
    List<HighRaitingRecipesItem> list;
    private static final String TAG = "raz";


    public HighRaitingRecipesAdapter(Context context, List<HighRaitingRecipesItem> list) {
        this.context = context;
        this.list = list;
        Log.d(TAG, "HighRaitingRecipesAdapter: 33");
    }

    @NonNull
    @Override
    public HighRaitingRecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HighRaitingRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.high_raiting_recipes, parent, false));
    }

    @Override
    public void onBindViewHolder(HighRaitingRecipesViewHolder holder, int position) {
        HighRaitingRecipesItem highRaitingRecipesItem = list.get(position);

        holder.titleView.setText(highRaitingRecipesItem.getName());
        Picasso.get().load(highRaitingRecipesItem.getImage()).resize(500,500).centerCrop().into(holder.imageView1);
        Log.d(TAG, "onBindViewHolder: in adapter holder");

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

    @Override
    public int getItemCount() {
        return list.size();
    }
}
