package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.s;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Favorite extends AppCompatActivity implements View.OnClickListener {

    // Declare variables for UI elements and functionality
    ImageView imageView;
    RecyclerView favoriteRecyclerView;
    FirebaseAuth mAuth;
    String uID;
    FirebaseUser currentUser;
    private static final String TAG = "Raz";
    boolean isFavorite = true;
    int totalRequests, completedRequests;


    LinearLayoutManager linearLayoutManagerVertical;
    List<HighRaitingRecipesItem> listFavoritesRecipes = new ArrayList<HighRaitingRecipesItem>();


    // Connect to the real-time database
    DatabaseReference datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Log.d(TAG, "onCreate: ");

        // Initialize Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //get current user
        uID = currentUser.getUid(); // get current user unique ID

        // Initialize UI elements
        imageView = findViewById(R.id.ivBack);
        imageView.setOnClickListener(this);

        favoriteRecyclerView = findViewById(R.id.favoritesRecyclerView);
        linearLayoutManagerVertical = new LinearLayoutManager(this);

        // Query the real-time database to get user's favorites
        datebaseReference.child("users").child(uID).child("favourites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: ");

                // Get the user's favorites from the database
                String favoriteList = snapshot.getValue().toString();
                Log.d(TAG, "onDataChange: " + favoriteList);

                if (favoriteList != null && !favoriteList.isEmpty()) {
                    Log.d(TAG, "onDataChange: 1111");
                    List<String> listRecipes = new ArrayList<>(Arrays.asList(favoriteList.split(",")));
                    Log.d(TAG, "ID list: " + listRecipes);

                    // Declare counters for total and completed requests
                    totalRequests = listRecipes.size();
                    completedRequests = 0;

                    // Iterate through the list of recipe IDs
                    for (int i = 0; i < totalRequests; i++) {
                        Log.d(TAG, "onDataChange: 2222");
                        String url = "https://api.spoonacular.com/recipes/" + listRecipes.get(i) + "/information?apiKey=9a5a4e3d51fa4468aab3ffa22a94a122";

                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, "onDataChange: 222");

                                        try {

                                            String recipeId = response.getString("id");
                                            String recipeTitle = response.getString("title");
                                            String imageUrl = response.getString("image");

                                            Log.d(TAG, "Recipe ID: " + recipeId);
                                            Log.d(TAG, "Recipe Title: " + recipeTitle);
                                            Log.d(TAG, "Image URL: " + imageUrl);

                                            HighRaitingRecipesItem favoriteItem = new HighRaitingRecipesItem(recipeTitle, imageUrl, recipeId, isFavorite);

                                            listFavoritesRecipes.add(favoriteItem);



                                            Log.d(TAG, "JSON Response: " + response.toString());
                                            JSONArray jsonArray = response.getJSONArray("");
                                            Log.d(TAG, "after JsonArray" + jsonArray);

                                            Log.d(TAG, "onDataChange: 24");



                                            if (jsonArray.length() > 0) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                                String id = jsonObject.getString("id");
                                                String title = jsonObject.getString("title");
                                                String imageURL = jsonObject.getString("image");

                                                Log.d(TAG, "get id title image: " + jsonObject.getString("id  ")
                                                        + jsonObject.getString("title"));

                                               // HighRaitingRecipesItem favoriteItem = new HighRaitingRecipesItem(title, imageURL, id, isFavorite);
                                                Log.d(TAG, title);
                                                Log.d(TAG, id);
                                                Log.d(TAG, imageURL);

                                                listFavoritesRecipes.add(favoriteItem);
                                                Log.d(TAG, "onResponse: 444");

                                                // Increment the completed requests counter
                                                completedRequests++;

                                                // Check if all requests are completed
                                                if (completedRequests == totalRequests) {
                                                    // All requests are complete, proceed with the remaining code
                                                    Log.d(TAG, "All requests are complete");
                                                }
                                                } else {
                                                // Handle the case when the array is empty
                                                Log.d(TAG, "onResponse: when array empty");

                                                // Increment the completed requests counter
                                                completedRequests++;

                                                // Check if all requests are completed
                                                if (completedRequests == totalRequests) {
                                                    // All requests are complete, proceed with the remaining code
                                                    Log.d(TAG, "All requests are complete");

                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            // Handle JSON parsing error
                                        } finally {
                                                // Increment the completed requests counter
                                                completedRequests++;

                                            // Check if all requests are completed
                                            if (completedRequests == totalRequests) {

                                                // All requests are complete, proceed with the remaining code
                                                Log.d(TAG, "All requests are complete");

                                                // Move adapter initialization and notification outside the loop
                                                favoriteRecyclerView.setLayoutManager(linearLayoutManagerVertical);
                                                favoriteRecyclerView.setAdapter(new RecommendedRecipesAdapter(getApplicationContext(), listFavoritesRecipes));
                                                favoriteRecyclerView.getAdapter().notifyDataSetChanged();
                                            }

                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle Volley error
                                        error.printStackTrace();

                                        // Increment the completed requests counter even if there's an error
                                        completedRequests++;

                                        // Check if all requests are completed
                                        if (completedRequests == totalRequests) {
                                            // All requests are complete, proceed with the remaining code
                                            Log.d(TAG, "All requests are complete, despite errors");

                                            // Move adapter initialization and notification outside the loop
                                            favoriteRecyclerView.setLayoutManager(linearLayoutManagerVertical);
                                            favoriteRecyclerView.setAdapter(new RecommendedRecipesAdapter(getApplicationContext(), listFavoritesRecipes));
                                            favoriteRecyclerView.getAdapter().notifyDataSetChanged();
                                        }
                                    }
                                });
                        queue.add(jsonObjectRequest);
                    }
                } else {
                    // Handle the case when favoriteList is null or empty
                    Log.d(TAG, "onDataChange: favoriteList is null or empty");
                    Toast.makeText(Favorite.this, "favoriteList is null or empty", Toast.LENGTH_SHORT).show();
                    // You might want to show a message or handle it appropriately
                }

                    // Move adapter initialization and notification outside the loop
                    favoriteRecyclerView.setLayoutManager(linearLayoutManagerVertical);
                    favoriteRecyclerView.setAdapter(new RecommendedRecipesAdapter(getApplicationContext(), listFavoritesRecipes));
                    favoriteRecyclerView.getAdapter().notifyDataSetChanged();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                Log.d(TAG, "onCancelled: ");
            }
        });

    }

    // onClick method, called when a UI element is clicked
    @Override
    public void onClick(View view) {
        Log.d(TAG, "Start the MainActivity ");
        // Start the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}

