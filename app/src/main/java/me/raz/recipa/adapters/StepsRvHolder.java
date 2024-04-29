package me.raz.recipa.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import lombok.Setter;
import me.raz.recipa.R;

@Getter
@Setter
public class StepsRvHolder extends RecyclerView.ViewHolder {

    private TextView stepNumTV;
    private TextView instructionTV;

    // Constructor for the ViewHolder
    public StepsRvHolder(@NonNull View itemView) {
        super(itemView);

        stepNumTV = itemView.findViewById(R.id.stepTV);
        instructionTV = itemView.findViewById(R.id.instructionTV);
    }
}
