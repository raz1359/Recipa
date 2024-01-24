package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class stepsRvAdapter extends RecyclerView.Adapter<stepsRvHolder> {

    Context context;
    ArrayList<stepsItem> items;

    // Constructor for the adapter
    public stepsRvAdapter (ArrayList<stepsItem> items, Context context) {

        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public stepsRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.steps_item, parent, false);
        return new stepsRvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stepsRvHolder holder, int position) {

        holder.stepNumTV.setText("Step " + items.get(position).getStepNum());
        holder.instructionTV.setText(items.get(position).getInstruction());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
