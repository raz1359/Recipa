package me.raz.recipa.adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;
import lombok.Setter;
import me.raz.recipa.R;

@Getter
@Setter
public class ShoppingListRvHolder extends RecyclerView.ViewHolder {

    private CheckBox isChecked;
    private TextView amount;

    public ShoppingListRvHolder(@NonNull View itemView ) {
        super(itemView);

        isChecked = itemView.findViewById(R.id.checked);
        amount = itemView.findViewById(R.id.amountTv);
    }
}
