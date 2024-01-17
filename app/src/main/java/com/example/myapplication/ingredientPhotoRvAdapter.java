package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ingredientPhotoRvAdapter extends RecyclerView.Adapter<ingredientPhotoRvHolder>{

    private ArrayList<String> names;

    public ingredientPhotoRvAdapter (ArrayList<String> names){
        this.names = names;
    }

    @NonNull
    @Override
    public ingredientPhotoRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredien_item, parent, false);
        return new ingredientPhotoRvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ingredientPhotoRvHolder holder, int position) {

        holder.text.setText(names.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}
