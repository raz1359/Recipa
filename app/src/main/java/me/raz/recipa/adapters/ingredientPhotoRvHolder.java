package me.raz.recipa.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;

import me.raz.recipa.R;


public class ingredientPhotoRvHolder extends RecyclerView.ViewHolder {

    @Getter
    private TextView text;

    // Constructor for the ViewHolder
    public ingredientPhotoRvHolder(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.ingredientName);
    }

}
