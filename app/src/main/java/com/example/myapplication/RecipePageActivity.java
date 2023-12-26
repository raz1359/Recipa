package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipePageActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "raz";

    private TextView descTv, recipeTitleTv;
    private ImageView recipeIv, likeIv, ivBack, ivShoppingCart, ivFavorites ;
    private String id;


    List<String> favourites = new ArrayList<>();


    // Connect to real time database
    DatabaseReference datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        descTv = findViewById(R.id.desc_tv);
        recipeTitleTv = findViewById(R.id.tvRecipeName);
        recipeIv = findViewById(R.id.recipe_iv);
        likeIv = findViewById(R.id.ibHeart);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        ivShoppingCart = findViewById(R.id.shopping_icon);
        ivShoppingCart.setOnClickListener(this);

        ivFavorites = findViewById(R.id.favorite_icon);
        ivFavorites.setOnClickListener(this);


        checkFavorite();

        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLike();
            }
        });

        loadData();
    }

    private void checkFavorite() {
        datebaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                favourites = Arrays.asList(task.getResult().getValue().toString().split(","));
                                if (favourites.contains(id)) {
                                    likeIv.setImageResource(R.drawable.heart_clicked);
                                } else {
                                    likeIv.setImageResource(R.drawable.heart_not_clicked);
                                }
                            } else {
                                // Handle the case when favourites is null or empty
                                likeIv.setImageResource(R.drawable.heart_not_clicked);
                            }
                        }
                    }
                });
    }

    private void toggleLike() {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");

        dbReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    favourites = Arrays.asList(task.getResult().getValue().toString().split(","));

                    try {
                        if (favourites.contains(id)) {
                            likeIv.setImageResource(R.drawable.heart_not_clicked);
                            favourites.remove(id);
                        } else {
                            likeIv.setImageResource(R.drawable.heart_clicked);
                            favourites.add(id);

                        }

                        String favs = buildFavsString(favourites);

                        dbReference.setValue(favs);
                    } catch (Exception e) {
                        // Log the exception
                        Log.e(TAG, "Error in toggleLike()", e);
                    }
                }
            }
        });
    }

    private String buildFavsString(List<String> favourites) {

        String favouritesStr = "";

        for (String item : favourites) {
            if (favouritesStr.isEmpty()) favouritesStr = item;

            else favouritesStr += "," + item;
        }
        return favouritesStr;
    }

    private void loadData() {


        String url = "https://api.spoonacular.com/recipes/" + id + "/information?apiKey=9a5a4e3d51fa4468aab3ffa22a94a122";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String recipeId = response.getString("id");
                            String recipeTitle = response.getString("title");
                            String imageUrl = response.getString("image");
                            String desc = response.getString("summary");

                            recipeTitleTv.setText(recipeTitle);
                            descTv.setText(Jsoup.parse(desc).text());
                            Picasso.get().load(imageUrl).resize(530, 500).centerCrop().noFade().into(recipeIv);


                            Log.d(TAG, "Recipe ID: " + recipeId);
                            Log.d(TAG, "Recipe Title: " + recipeTitle);
                            Log.d(TAG, "Image URL: " + imageUrl);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Volley error
                        error.printStackTrace();

                    }
                });

        queue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ivBack:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                break;
            case R.id.favorite_icon:
                intent = new Intent(this, Favorite.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                break;
            case R.id.shopping_icon:
                intent = new Intent(this, Groceries.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
