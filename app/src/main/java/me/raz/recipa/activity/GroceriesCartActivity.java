package me.raz.recipa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.raz.recipa.R;
import me.raz.recipa.adapters.ShoppingListAdapter;
import me.raz.recipa.data.Ingredient;

public class GroceriesCartActivity extends AppCompatActivity {


    // UI Elements
    RecyclerView shoppingListRv;
    LinearLayoutManager linearLayoutManagerVertical;
    ImageView deleteShoppingList;
    BottomNavigationView bottomNavigationView;

    // Firebase
    DatabaseReference dbReferenceShoppingList;
    FirebaseAuth mAuth;
    String uID;
    FirebaseUser currentUser;

    // Data
    List<Ingredient> shoppingList = new ArrayList<>();

    // Tag for logging
    private static final String TAG = "raz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        // Initialize Firebase authentication and database references
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //get current user
        uID = currentUser.getUid(); // get current user unique ID

        // Initialize UI elements
        linearLayoutManagerVertical = new LinearLayoutManager(this);
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        shoppingListRv = findViewById(R.id.shopingListRV);
        deleteShoppingList = findViewById(R.id.deleteShoppingCart);

        // Firebase reference to the shopping list
        dbReferenceShoppingList = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(uID)
                .child("shoppingList");


        // Retrieve shopping list data from Firebase
        dbReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String name = childSnapshot.getKey();
                    boolean isChecked = Boolean.parseBoolean(childSnapshot.child("checked").getValue().toString());
                    int amount = Integer.parseInt(childSnapshot.child("amount").getValue().toString());

                    Ingredient ingredient = new Ingredient(name , isChecked , amount);
                    shoppingList.add(ingredient);
                }
                // Show or hide elements based on shopping list data
                updateUI();
            }
            @Override
            public void onCancelled( @NonNull DatabaseError error ) {
                // Handle database error
            }
        });

        // Delete shopping list onClick
        deleteShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                // Clear shopping list in Firebase
                dbReferenceShoppingList.setValue(null);
                shoppingList.clear();

                // Update UI
                updateUI();
            }
        });

        // Bottom navigation setup
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.shoppingBT);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.shoppingBT:
                    return true;

                case R.id.searchBT:
                    // Open search activity
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,0);
                    finish();
                    return true;
                case R.id.homeBT:
                    // Open main activity
                    Intent intent1 = new Intent(this, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent1,0);
                    finish();
                    return true;
            }
            return false;
        });

    }

    // Update UI elements based on shopping list data
    private void updateUI() {
        findViewById(R.id.tvDescription).setAlpha(shoppingList.isEmpty() ? 1 : 0);
        findViewById(R.id.tvEmpty).setAlpha(shoppingList.isEmpty() ? 1 : 0);
        findViewById(R.id.lady_empty).setAlpha(shoppingList.isEmpty() ? 1 : 0);
        findViewById(R.id.deleteShoppingCart).setAlpha(shoppingList.isEmpty() ? 0 : 1);
        shoppingListRv.setAlpha(shoppingList.isEmpty() ? 0 : 1);

        if (!shoppingList.isEmpty()) {
            shoppingListRv.setLayoutManager(linearLayoutManagerVertical);
            shoppingListRv.setAdapter(new ShoppingListAdapter(getApplicationContext(), shoppingList));
        }
    }
}