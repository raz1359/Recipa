package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter <shoppingListRvHolder> {

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
    public shoppingListRvHolder onCreateViewHolder( @NonNull ViewGroup parent , int viewType ) {
        // Inflate the layout for each list item
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new shoppingListRvHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull shoppingListRvHolder holder , int position ) {
        // Get the current ingredient
        Ingredient ingredient = list.get(position);
        String tempName = ingredient.getName();

        // Bind data to ViewHolder
        holder.name.setText(ingredient.getName());
        holder.amount.setText("X" + ingredient.getAmount());
        holder.isChecked.setChecked(ingredient.isChecked());


        // Checkbox listener to update checked status in Firebase
        holder.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton , boolean b ) {
                if (holder.isChecked.isChecked()) {
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
