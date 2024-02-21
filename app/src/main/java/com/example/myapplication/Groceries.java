package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Groceries extends AppCompatActivity {

    RecyclerView shoppingListRv;
    LinearLayoutManager linearLayoutManagerVertical;
    List <Ingredient> shoppingList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    DatabaseReference dbReferenceShoppingList;
    FirebaseAuth mAuth;
    String uID;
    FirebaseUser currentUser;

    private static final String TAG = "raz";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        // Initialize Firebase authentication and database references
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //get current user
        uID = currentUser.getUid(); // get current user unique ID

        linearLayoutManagerVertical = new LinearLayoutManager(this);
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);

        shoppingListRv = findViewById(R.id.shopingListRV);

        dbReferenceShoppingList = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(uID)
                .child("shoppingList");


        if (shoppingList == null) {
            findViewById(R.id.tvDescription).setAlpha(1);
            findViewById(R.id.tvEmpty).setAlpha(1);
            findViewById(R.id.lady_empty).setAlpha(1);
            shoppingListRv.setAlpha(0);

        } else {
            findViewById(R.id.tvDescription).setAlpha(0);
            findViewById(R.id.tvEmpty).setAlpha(0);
            findViewById(R.id.lady_empty).setAlpha(0);
            shoppingListRv.setAlpha(1);

            shoppingListRv.setLayoutManager(linearLayoutManagerVertical);
            shoppingListRv.setAdapter(new ShoppingListAdapter(getApplicationContext(),shoppingList));
        }

        dbReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String name = childSnapshot.getKey();
                    boolean isChecked = (boolean) childSnapshot.child("checked").getValue();
                    int amount = Integer.parseInt(childSnapshot.child("amount").getValue().toString());

                    Ingredient ingredient = new Ingredient(name,isChecked,amount);
                    Log.d(TAG , "onDataChange: " + childSnapshot + "  " + ingredient);
                    shoppingList.add(ingredient);
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.shoppingBT);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.shoppingBT:
                    return true;

                case R.id.searchBT:
                    //startActivity(new Intent(getApplicationContext(), Search.class));
                    Intent intent = new Intent(this, Search.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,0);
                    finish();
                    return true;
                case R.id.homeBT:
                    Intent intent1 = new Intent(this, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent1,0);
                    finish();
                    return true;

            }
            return false;
        });

    }
}