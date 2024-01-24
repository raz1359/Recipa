package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class stepsRvHolder extends RecyclerView.ViewHolder {

    TextView stepNumTV , instructionTV;

    // Constructor for the ViewHolder
    public stepsRvHolder(@NonNull View itemView) {
        super(itemView);

        stepNumTV = itemView.findViewById(R.id.stepTV);
        instructionTV = itemView.findViewById(R.id.instructionTV);
    }
}
