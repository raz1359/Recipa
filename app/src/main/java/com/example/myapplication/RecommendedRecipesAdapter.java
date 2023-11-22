package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendedRecipesAdapter extends RecyclerView.Adapter <HighRaitingRecipesViewHolder> {

    Context context;
    List<HighRaitingRecipesItem> list;
    private static final String TAG = "raz";

    public RecommendedRecipesAdapter(Context context, List<HighRaitingRecipesItem> list) {
        this.context = context;
        this.list = list;
        Log.d(TAG, "RecommendedRecipesAdapter: after constructor");
    }

    @NonNull
    @Override
    public HighRaitingRecipesViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new HighRaitingRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.recommended_recipes, parent , false));
    }

    @Override
    public void onBindViewHolder(HighRaitingRecipesViewHolder holder, int position) {
        HighRaitingRecipesItem highRaitingRecipesItem = list.get(position);

        holder.titleView.setText(highRaitingRecipesItem.getName());
        Picasso.get().load(highRaitingRecipesItem.getImage()).resize(500,500).centerCrop().into(holder.imageView1);
        Log.d(TAG, "onBindViewHolder: in adapter holder");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
