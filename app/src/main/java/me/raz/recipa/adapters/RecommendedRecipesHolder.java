package me.raz.recipa.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import lombok.Setter;
import me.raz.recipa.R;

@Getter
@Setter
public class RecommendedRecipesHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "raz";
    private TextView titleView;
    private ImageView imageView;
    private ImageView likeBtn;

    public RecommendedRecipesHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.image);
        titleView = itemView.findViewById(R.id.title);
        likeBtn = itemView.findViewById(R.id.ibHeart);



    }
}
