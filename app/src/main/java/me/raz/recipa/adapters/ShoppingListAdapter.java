package me.raz.recipa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import me.raz.recipa.R;
import me.raz.recipa.data.Ingredient;

public class ShoppingListAdapter extends RecyclerView.Adapter <ShoppingListRvHolder> {

    // Context and data list
    Context context;
    List<Ingredient> list;

    // Firebase database reference
    private DatabaseReference dbReferenceShoppingList;

    // Tag for logging
    private static final String TAG = "raz";

    //Constructor
    public ShoppingListAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
        // Initialize Firebase database reference
        dbReferenceShoppingList = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("shoppingList");

    }
    @NonNull
    @Override
    public ShoppingListRvHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType ) {
        // Inflate the layout for each list item
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new ShoppingListRvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListRvHolder holder , int position ) {
        // Get the current ingredient
        Ingredient ingredient = list.get(position);
        String tempName = ingredient.getName();

        // Bind data to ViewHolder
        holder.getAmount().setText("X" + ingredient.getAmount());
        holder.getIsChecked().setChecked(ingredient.isChecked());
        holder.getIsChecked().setText(ingredient.getName());


        // Checkbox listener to update checked status in Firebase
        holder.getIsChecked().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton , boolean b ) {
                if (holder.getIsChecked().isChecked()) {
                    dbReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot ) {
                            if(snapshot.child(tempName).child("checked").getValue().toString().equals("false"))
                                dbReferenceShoppingList.child(tempName).child("checked").setValue(true);

                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError error ) {
                            // Handle database error
                        }
                    });
                } else {
                    dbReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot ) {
                            if(snapshot.child(tempName).child("checked").getValue().toString().equals("true"))
                                dbReferenceShoppingList.child(tempName).child("checked").setValue(false);
                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError error ) {

                        }
                    });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
