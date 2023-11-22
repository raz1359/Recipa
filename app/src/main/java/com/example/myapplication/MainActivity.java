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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public Button button;
    public ImageView notificationImage, searchImage,profileImage;
    public EditText searchBar;
    private static final String TAG = "raz";
    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    public String uID;
    FirebaseUser currentUser;
    RecyclerView highRaitingRecyclerView,recommendedRecipesRecyclerView;
    // creating two linearLayoutManger
    LinearLayoutManager linearLayoutManagerHorizontal , linearLayoutManagerVertical;
    List<HighRaitingRecipesItem> listHighRating = new ArrayList<HighRaitingRecipesItem>();
    List<HighRaitingRecipesItem> listRecommendedRecipes = new ArrayList<HighRaitingRecipesItem>();


    // Connect to real time database
    DatabaseReference datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase initi
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();

        searchBar = findViewById(R.id.search_bar);


        notificationImage = findViewById(R.id.notification);
        notificationImage.setOnClickListener(this);

        profileImage = findViewById(R.id.emptyProfile);
        profileImage.setOnClickListener(this);

        searchImage = findViewById(R.id.searchIcon);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.homeBT);

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

        retriveDate();


        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_search_selected));
                    searchImage.setImageResource(R.drawable.searchbar_onpng);
                } else {
                    searchBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.edit_text_search));
                    searchImage.setImageResource(R.drawable.search_icon2);
                }
            }
        });

        // HighRaiting RecycleView
        highRaitingRecyclerView = findViewById(R.id.recyclViewHighRating);
        highRaitingRecyclerView.setHasFixedSize(true);
        linearLayoutManagerHorizontal = new LinearLayoutManager(this);
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);

        // RecommendedRecipes RecycleView
        linearLayoutManagerVertical = new LinearLayoutManager(this);
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recommendedRecipesRecyclerView = findViewById(R.id.recyclerViewRecommendedRecipes);
        recommendedRecipesRecyclerView.setHasFixedSize(true);
        recommendedRecipesRecyclerView.setNestedScrollingEnabled(false);


         getHighRatingRecipeAPI();


    }




    private void getHighRatingRecipeAPI() {

        String url = "https://api.spoonacular.com/recipes/complexSearch/?apiKey=9a5a4e3d51fa4468aab3ffa22a94a122&query=bread";
        //Log.d(TAG, "onResponse: ");
        RequestQueue requestQueue;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String title = jsonObject.getString("title");
                                String imageURL = jsonObject.getString("image");

                                Picasso.get().load(imageURL).resize(500,500).centerCrop().noFade();



                                HighRaitingRecipesItem tempHightRatingRecipesItem = new HighRaitingRecipesItem(title,imageURL,id);
                                Log.d(TAG, title);
                                Log.d(TAG, id);
                                Log.d(TAG, imageURL);

                                listHighRating.add(tempHightRatingRecipesItem);
                                listRecommendedRecipes.add(tempHightRatingRecipesItem);

                                Log.d(TAG, "onResponse: 11");

                            }
                            Log.d(TAG, "onResponse: 444" + listHighRating.toString());
                            highRaitingRecyclerView.setLayoutManager(linearLayoutManagerHorizontal);
                            highRaitingRecyclerView.setAdapter(new HighRaitingRecipesAdapter(getApplicationContext(),listHighRating));

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
                });

        queue.add(jsonObjectRequest);

    }

    private void retriveDate() {
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
