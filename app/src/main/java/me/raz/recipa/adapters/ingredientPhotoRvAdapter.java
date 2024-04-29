package me.raz.recipa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.raz.recipa.R;

public class ingredientPhotoRvAdapter extends RecyclerView.Adapter<ingredientPhotoRvHolder>{

    private List<String> names;

    // Constructor for the adapter
    public ingredientPhotoRvAdapter (List<String> names){
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

        holder.getText().setText(names.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}
