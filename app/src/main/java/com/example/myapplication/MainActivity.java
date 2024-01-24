package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // Declare UI elements
    public Button button;
    public ImageView notificationImage, searchImage,profileImage, favoritesImage;
    public EditText searchBar;
    private static final String TAG = "raz";
    private BottomNavigationView bottomNavigationView;
    RecyclerView highRaitingRecyclerView,recommendedRecipesRecyclerView;
    ImageButton ibHeart;

    // Firebase authentication and user information
    FirebaseAuth mAuth;
    public String uID;
    FirebaseUser currentUser;


    // RecyclerViews and adapters for displaying high rating and recommended recipes
    LinearLayoutManager linearLayoutManagerHorizontal , linearLayoutManagerVertical;
    List<HighRaitingRecipesItem> listHighRating = new ArrayList<HighRaitingRecipesItem>();
    List<HighRaitingRecipesItem> listRecommendedRecipes = new ArrayList<HighRaitingRecipesItem>();
    List<String> favourites = new ArrayList<>();


    // Firebase Realtime Database reference
    DatabaseReference datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");


    // onCreate method, called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase authentication and get current user information
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //get current user
        uID = currentUser.getUid(); // get current user unique ID

        // Initialize UI elements
        searchBar = findViewById(R.id.search_bar);
        notificationImage = findViewById(R.id.notification);
        notificationImage.setOnClickListener(this);
        favoritesImage = findViewById(R.id.favorite_icon);
        favoritesImage.setOnClickListener(this);
        profileImage = findViewById(R.id.emptyProfile);
        profileImage.setOnClickListener(this);
        searchImage = findViewById(R.id.searchIcon);

        // Bottom navigation setup
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.homeBT);


        // High rating RecyclerView setup
        highRaitingRecyclerView = findViewById(R.id.recyclViewHighRating);
        highRaitingRecyclerView.setHasFixedSize(true);
        linearLayoutManagerHorizontal = new LinearLayoutManager(this);
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Recommended recipes RecyclerView setup
        linearLayoutManagerVertical = new LinearLayoutManager(this);
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recommendedRecipesRecyclerView = findViewById(R.id.recyclerViewRecommendedRecipes);
        recommendedRecipesRecyclerView.setHasFixedSize(true);
        recommendedRecipesRecyclerView.setNestedScrollingEnabled(false);

        // Add focus change listener to the search bar for UI updates
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // Change background and icon based on focus
                if (hasFocus) {
                    searchBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_search_selected));
                    searchImage.setImageResource(R.drawable.searchbar_onpng);
                } else {
                    searchBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_search));
                    searchImage.setImageResource(R.drawable.search_icon2);
                }
            }
        });

        // Bottom navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeBT:
                    return true;

                case R.id.searchBT:
                    //startActivity(new Intent(getApplicationContext(), Search.class));
                    Intent intent = new Intent(this, Search.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,0);
                    finish();
                    return true;
                case R.id.shoppingBT:
                    Intent intent1 = new Intent(this, Groceries.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent1,0);
                    finish();
                    return true;

            }
            return false;

        });

        // Retrieve user data from Firebase Realtime Database
        retrieveData();

        // Fetch high rating and recommended recipes from API
        getHighRatingRecipeAPI();
        getRecommendedRecipeAPI();

    }

    private void getRecommendedRecipeAPI() {

        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=5";


        RequestQueue queue = Volley.newRequestQueue(this);

        datebaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        //checking if favourites list is null
                        if (task.getResult().getValue() != null) {
                            favourites = Arrays.asList(task.getResult().getValue().toString().split(","));
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("recipes");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                String id = jsonObject.getString("id");
                                                String title = jsonObject.getString("title");
                                                String imageURL = jsonObject.getString("image");


                                                boolean isFavourite = false;

                                                // if (favourites.contains(id)) isFavourite = true;

                                                Picasso.get().load(imageURL).resize(500, 500).centerCrop().noFade();


                                                HighRaitingRecipesItem tempHighRatingRecipesItem = new HighRaitingRecipesItem(title, imageURL, id, isFavourite);
                                                Log.d(TAG, title);
                                                Log.d(TAG, id);
                                                Log.d(TAG, imageURL);

                                                listRecommendedRecipes.add(tempHighRatingRecipesItem);

                                                //compare ids and checking that favorits are on the recommended list
                                                for (int j = 0; j < listRecommendedRecipes.size(); j++) {
                                                    for (int x = 0; x < favourites.size(); x++) {
                                                        if (listRecommendedRecipes.get(j).getId().equals(favourites.get(x))) {
                                                            listRecommendedRecipes.get(j).setFavourite(true);
                                                        }
                                                    }
                                                }

                                                String temp = String.valueOf(listRecommendedRecipes.get(0).isFavourite() + " " + listRecommendedRecipes.get(0).getId());
                                                Log.d(TAG, temp);


                                            }
                                            recommendedRecipesRecyclerView.setLayoutManager(linearLayoutManagerVertical);
                                            recommendedRecipesRecyclerView.setAdapter(new RecommendedRecipesAdapter(getApplicationContext(), listRecommendedRecipes));


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        error.printStackTrace();
                                    }
                                }

                              ) {

                            public Map<String, String> getHeaders() {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("X-Rapidapi-Key", "d061eda37cmshd8c99b385f1e685p177488jsn6dcbb4585857\n");
                                headers.put("X-Rapidapi-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
                                return headers;
                            }
                        };
                        queue.add(jsonObjectRequest);
                    }
                });
    }

    private void getHighRatingRecipeAPI() {

        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=5";

        RequestQueue queue = Volley.newRequestQueue(this);

        datebaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        //checking if favourites list is null
                        if (task.getResult().getValue() != null) {
                            favourites = Arrays.asList(task.getResult().getValue().toString().split(","));
                            Log.d(TAG, "onComplete: " + favourites.toString());
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("recipes");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                String id = jsonObject.getString("id");
                                                String title = jsonObject.getString("title");
                                                String imageURL = jsonObject.getString("image");


                                                boolean isFavourite = false;

                                                // if (favourites.contains(id)) isFavourite = true;

                                                Picasso.get().load(imageURL).resize(500, 500).centerCrop().noFade();


                                                HighRaitingRecipesItem tempHighRatingRecipesItem = new HighRaitingRecipesItem(title, imageURL, id, isFavourite);
                                                Log.d(TAG, title);
                                                Log.d(TAG, id);
                                                Log.d(TAG, imageURL);

                                                listHighRating.add(tempHighRatingRecipesItem);
                                                Log.d(TAG, "onResponse: " + listRecommendedRecipes);

                                                //compare ids and checking that favorits are on the recommended list
                                                for (int j = 0; j < listRecommendedRecipes.size(); j++) {
                                                    for (int x = 0; x < favourites.size(); x++) {
                                                        if (listRecommendedRecipes.get(j).getId().equals(favourites.get(x))) {
                                                            Log.d(TAG, "onResponse: 5656565");
                                                            listRecommendedRecipes.get(j).setFavourite(true);
                                                        }
                                                    }
                                                    Log.d(TAG, "onResponse: " + listRecommendedRecipes.get(j).toString());
                                                }

                                                String temp = String.valueOf(listRecommendedRecipes.get(0).isFavourite() + " " + listRecommendedRecipes.get(0).getId());
                                                Log.d(TAG, temp);


                                                Log.d(TAG, "onResponse: 11");

                                            }
                                            Log.d(TAG, "onResponse: 444" + listHighRating.toString());
                                            highRaitingRecyclerView.setLayoutManager(linearLayoutManagerHorizontal);
                                            highRaitingRecyclerView.setAdapter(new HighRaitingRecipesAdapter(getApplicationContext(), listHighRating));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        error.printStackTrace();
                                    }
                                }
                                ) {

                            public Map<String, String> getHeaders() {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("X-Rapidapi-Key", "d061eda37cmshd8c99b385f1e685p177488jsn6dcbb4585857\n");
                                headers.put("X-Rapidapi-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
                                return headers;
                            }

                    };

                            queue.add(jsonObjectRequest);
                    }
                });

    }

    private void retrieveData() {
        datebaseReference.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profileTemp = snapshot.getValue(Profile.class);

                if (profileTemp != null) {
                    String url = profileTemp.getImageUri();
                    Picasso.get().load(url).resize(280, 280).centerCrop().noFade().into(profileImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.notification:
                intent = new Intent(this, Notification_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
                break;
            case R.id.favorite_icon:
                intent = new Intent(this, Favorite  .class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
                break;
            case R.id.emptyProfile:
                intent = new Intent(this, FillProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());

        return false;
    }
}
