package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search extends AppCompatActivity implements View.OnClickListener {

    // Declare UI elements
    ImageView searchImage;
    EditText searchBar;
    private static final String TAG = "raz";
    private BottomNavigationView bottomNavigationView;
    RecyclerView searchRecyclerView;
    String search, searchFromMain, breakfast, lunch, dinner;

    LinearLayoutManager linearLayoutManagerVertical;
    List<HighRaitingRecipesItem> listSearch = new ArrayList<HighRaitingRecipesItem>();
    List<String> favourites = new ArrayList<>();

    // Connect to real-time database
    DatabaseReference datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI elements
        searchImage = findViewById(R.id.searchIcon2);
        searchBar = findViewById(R.id.search_bar2);
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // Change background and icon based on focus
                if (hasFocus) {
                    searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search_selected));
                    searchImage.setImageResource(R.drawable.searchbar_onpng);
                } else {
                    searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search));
                    searchImage.setImageResource(R.drawable.search_icon2);
                }
            }
        });

        // Bottom navigation setup
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.searchBT);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.searchBT:
                    return true;

                case R.id.shoppingBT:
                    //startActivity(new Intent(getApplicationContext(), Search.class));
                    Intent intent = new Intent(this, Groceries.class);
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

        // RecyclerView setup for search results
        linearLayoutManagerVertical = new LinearLayoutManager(this);
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecyclerView = findViewById(R.id.recyclViewSearch);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setNestedScrollingEnabled(false);

        // Get searched recipe from MainActivity
        Intent intent = getIntent();
        searchFromMain = intent.getStringExtra("search");
        Log.d(TAG, "onCreate search: " + searchFromMain);

        // Get categories from MainActivity
        breakfast = intent.getStringExtra("breakfast");
        lunch = intent.getStringExtra("lunch");
        dinner = intent.getStringExtra("dinner");

        Search();

    }

    private void Search() {
        if (searchFromMain != null) {
            getSearchRecipeAPI(searchFromMain);
            searchBar.setText(searchFromMain);
            searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search_selected));
            searchImage.setImageResource(R.drawable.searchbar_onpng);
        } else if (breakfast != null) {
            getSearchRecipeAPI(breakfast);
            searchBar.setText(breakfast);
            searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search_selected));
            searchImage.setImageResource(R.drawable.searchbar_onpng);
        } else if (lunch != null) {
            getSearchRecipeAPI(lunch);
            searchBar.setText(lunch);
            searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search_selected));
            searchImage.setImageResource(R.drawable.searchbar_onpng);
        } else if (dinner != null) {
            getSearchRecipeAPI(dinner);
            searchBar.setText(dinner);
            searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search_selected));
            searchImage.setImageResource(R.drawable.searchbar_onpng);
        } else if (searchFromMain == null && breakfast == null && lunch == null && dinner == null)
            createRandomRecipeAPI();
    }

    // Method to update search results based on user input
    public void updateSearch(View view) {

        listSearch.clear();
        getSearchRecipeAPI(searchBar.getText().toString().trim());
    }

    private void getSearchRecipeAPI(String recipe) {

        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?query=" + recipe + "&number=5";

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
                                            JSONArray jsonArray = response.getJSONArray("results");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                String id = jsonObject.getString("id");
                                                String title = jsonObject.getString("title");
                                                String imageURL = jsonObject.getString("image");


                                                boolean isFavourite = false;


                                                Picasso.get().load(imageURL).resize(500, 500).centerCrop().noFade();


                                                HighRaitingRecipesItem tempHighRatingRecipesItem = new HighRaitingRecipesItem(title, imageURL, id, isFavourite);
                                                Log.d(TAG, title);
                                                Log.d(TAG, id);
                                                Log.d(TAG, imageURL);

                                                listSearch.add(tempHighRatingRecipesItem);

                                                //compare ids and checking that favorits are on the Search list
                                                for (int j = 0; j < listSearch.size(); j++) {
                                                    for (int x = 0; x < favourites.size(); x++) {
                                                        if (listSearch.get(j).getId().equals(favourites.get(x))) {
                                                            listSearch.get(j).setFavourite(true);
                                                        }
                                                    }
                                                }

                                                String temp = String.valueOf(listSearch.get(0).isFavourite() + " " + listSearch.get(0).getId());
                                                Log.d(TAG, temp);


                                            }
                                            searchRecyclerView.setLayoutManager(linearLayoutManagerVertical);
                                            searchRecyclerView.setAdapter(new RecommendedRecipesAdapter(getApplicationContext(), listSearch));

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

    // Method to create random Recycler View
    private void createRandomRecipeAPI() {

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


                                                Picasso.get().load(imageURL).resize(500, 500).centerCrop().noFade();


                                                HighRaitingRecipesItem tempHighRatingRecipesItem = new HighRaitingRecipesItem(title, imageURL, id, isFavourite);
                                                Log.d(TAG, title);
                                                Log.d(TAG, id);
                                                Log.d(TAG, imageURL);

                                                listSearch.add(tempHighRatingRecipesItem);

                                                //compare ids and checking that favorits are on the Search list
                                                for (int j = 0; j < listSearch.size(); j++) {
                                                    for (int x = 0; x < favourites.size(); x++) {
                                                        if (listSearch.get(j).getId().equals(favourites.get(x))) {
                                                            listSearch.get(j).setFavourite(true);
                                                        }
                                                    }
                                                }

                                                String temp = String.valueOf(listSearch.get(0).isFavourite() + " " + listSearch.get(0).getId());
                                                Log.d(TAG, temp);


                                            }
                                            searchRecyclerView.setLayoutManager(linearLayoutManagerVertical);
                                            searchRecyclerView.setAdapter(new RecommendedRecipesAdapter(getApplicationContext(), listSearch));

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
//                            @Override
//                            protected Map<String, String> getParams() {
//
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("number","10");
//                                return params;
//                            }
                           };

                        queue.add(jsonObjectRequest);
                    }
                });
    }

    @Override
    public void onClick(View view) {
    }
}
