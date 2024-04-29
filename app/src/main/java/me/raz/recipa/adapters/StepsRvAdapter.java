package me.raz.recipa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.raz.recipa.R;
import me.raz.recipa.data.StepsItem;

public class StepsRvAdapter extends RecyclerView.Adapter<StepsRvHolder> {

    Context context;
    List<StepsItem> items;

    // Constructor for the adapter
    public StepsRvAdapter(List<StepsItem> items, Context context) {

        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public StepsRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.steps_item, parent, false);
        return new StepsRvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsRvHolder holder, int position) {

        holder.getStepNumTV().setText("Step " + items.get(position).getStepNum());
        holder.getInstructionTV().setText(items.get(position).getInstruction());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
