package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import kotlin.LateinitKt;

public class RecipePageActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare variables for UI elements and functionality
    private String TAG = "raz", recipeTitle;
    private TextView descTv, recipeTitleTv, ingredientNumber, addToCartBtn;
    private ImageView recipeIv, likeIv, ivBack, ivShoppingCart, ivFavorites, ivTextToSpeech ;
    private String id, desc;
    boolean isFavorite = true;
    Button minus , plus;
    DataSnapshot snapshot;
    TextToSpeech t1;
    BottomSheetBehavior mBotttomSheetBehavior;
    RecyclerView ingredientNamesRv, stepsRV;
    LinearLayoutManager linearLayoutHorizontal, linearLayoutVertical;
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this , 4, LinearLayoutManager.HORIZONTAL, false);

    int numRecipe = 1;
    // Firebase authentication and database references
    FirebaseAuth mAuth;
    String uID;
    FirebaseUser currentUser;
    ArrayList<String> favourites = new ArrayList<>();

    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    Map<String, Object> ingredientHashMap = new HashMap<>();
    DatabaseReference dbReferenceFav , dbReferenceShoppingList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);

        // Initialize Firebase authentication and database references
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //get current user
        uID = currentUser.getUid(); // get current user unique ID

        // Connect to the real-time database
        dbReferenceFav = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(uID)
                .child("favourites");

        dbReferenceShoppingList = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(uID)
                .child("shoppingList");

        dbReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshotTemp ) {
                snapshot = snapshotTemp;
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });


        // Get recipe ID from the intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        // Initialize UI elements
        descTv = findViewById(R.id.desc_tv);
        recipeTitleTv = findViewById(R.id.tvRecipeName);
        recipeIv = findViewById(R.id.recipe_iv);
        likeIv = findViewById(R.id.ibHeartRecipe);
        ivBack = findViewById(R.id.ivBack);
        ivShoppingCart = findViewById(R.id.shopping_icon);
        ivFavorites = findViewById(R.id.favorite_icon);
        ivTextToSpeech = findViewById(R.id.textToSpeech);
        ingredientNamesRv = findViewById(R.id.ingredientsPhotoRV);
        stepsRV = findViewById(R.id.stepsRV);
        ingredientNumber = findViewById(R.id.ingredientNumber);
        minus = findViewById(R.id.minus);
        addToCartBtn = findViewById(R.id.addToCart);
        plus = findViewById(R.id.plus);

        linearLayoutHorizontal = new LinearLayoutManager(this);
        linearLayoutHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutVertical = new LinearLayoutManager(this);
        linearLayoutVertical.setOrientation(LinearLayoutManager.VERTICAL);

        View bottomSheet = findViewById(R.id.bottomSheet);
        mBotttomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBotttomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Set click listeners for UI elements
        ivBack.setOnClickListener(this);
        ivShoppingCart.setOnClickListener(this);
        ivFavorites.setOnClickListener(this);


        // Load recipe data
        loadData();

        // Check if the recipe is a favorite and set the appropriate heart icon
        checkFavorite();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                numRecipe = Integer.valueOf(ingredientNumber.getText().toString());
                ingredientNumber.setText(String.valueOf(numRecipe + 1));
                numRecipe++;
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 numRecipe = Integer.valueOf(ingredientNumber.getText().toString());

                if (numRecipe > 1) {
                    ingredientNumber.setText(String.valueOf(numRecipe - 1));
                    numRecipe--;
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                loadData();
                ingredientHashMap.forEach((key, value) -> {
                    ((Ingredient)value).setAmount(numRecipe);
                });

                if (!snapshot.exists()) {
                    Log.d(TAG , "onClick:  first recipe");
                    dbReferenceShoppingList.setValue(ingredientHashMap);
                } else {
                    dbReferenceShoppingList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot ) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()){
                                String name = childSnapshot.getKey();
                                if (ingredientHashMap.containsKey(name)) {
                                    int currentAmount = Integer.parseInt(childSnapshot.child("amount").getValue().toString());
                                    dbReferenceShoppingList.child(name).child("amount").setValue(currentAmount + numRecipe);
                                    ingredientHashMap.remove(name);
                                } else {
                                    // עם המצרך לא היה קיים ברשימה תוסיף אותו לfirebase
                                    dbReferenceShoppingList.updateChildren(ingredientHashMap);
                                }
                            }
                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError error ) {

                        }
                    });
                }

            }
        });

        // Initialize TextToSpeech
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    t1.setLanguage(Locale.ENGLISH);
            }
        });
        // Set TextToSpeech functionality
        ivTextToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + Jsoup.parse(desc).text());
                t1.speak(Jsoup.parse(desc).text(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        // Set click listener for the heart icon to add/remove from favorites
        likeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the recipe is a favorite and update the heart icon
                Log.d(TAG, "OnHeartClick: ");
                dbReferenceFav.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful()) {

                            favourites = new ArrayList<>(Arrays.asList(task.getResult().getValue().toString().split(",")));
                            Log.d(TAG, "onComplete: 33" + favourites);

                            if (!isFavorite) {
                                Log.d(TAG, "onComplete: is fav true");
                                likeIv.setImageResource(R.drawable.heart_clicked);
                                addFavourite();
                            } else {
                                Log.d(TAG, "onComplete: not fav");
                                removeFavourite();
                                likeIv.setImageResource(R.drawable.heart_not_clicked);
                            }
                        }  else {
                                Log.d(TAG, "onComplete: task not successful");
                            }

                    }
                });
            }
        });

    }

    // Method to remove a recipe from favorites
    private void removeFavourite() {
        // Remove the recipe ID from the favorites list
        dbReferenceFav.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    favourites = new ArrayList<>(Arrays.asList(task.getResult().getValue().toString().split(",")));
                    favourites.remove(id);

                    // Update the favorites string in the database
                    String res = buildFavsString();
                    dbReferenceFav.setValue(res);
                }
            }
        });
        isFavorite = false;
    }


    // Method to check if a recipe is a favorite
    private void checkFavorite() {
                dbReferenceFav.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                Log.d(TAG, "onComplete: 888," + task.getResult().getValue().toString());
                                favourites = new ArrayList<>(Arrays.asList(task.getResult().getValue().toString().split(",")));
                                if (favourites.contains(id)) {
                                    likeIv.setImageResource(R.drawable.heart_clicked);
                                    isFavorite = true;
                                } else {
                                    likeIv.setImageResource(R.drawable.heart_not_clicked);
                                    isFavorite = false;
                                }
                            } else {
                                // Handle the case when favorites are null or empty
                                likeIv.setImageResource(R.drawable.heart_not_clicked);
                                isFavorite = false;
                            }
                        }
                    }
                });
    }


    // Method to add a recipe to favorites
    private void addFavourite() {
        dbReferenceFav.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    String favorites = task.getResult().getValue().toString();
                    Log.d(TAG, "onComplete: 999" + favorites);

                    if (favorites != null) {

                        isFavorite = true;
                        if (favorites.isEmpty()) favorites=id;
                        else favorites += "," + id;
                        dbReferenceFav.setValue(favorites);
                        Log.d(TAG, "Updated favorites: " + favorites);

                    } else {

                        isFavorite = true;
                        String favourites = buildFavsString();
                        dbReferenceFav.setValue(favourites);
                        Log.d(TAG, "New favorites: " + favourites);

                    }

                } else {
                Log.d(TAG, "onComplete: task not successful");
                }
            }
        });
    }

    // Method to build a comma-separated string from the favorites list
    private String buildFavsString() {

        String favouritesStr = "";

        for (String id: favourites){
            if (favouritesStr.isEmpty())
                favouritesStr = id;
            else
                favouritesStr += "," +id;
        }

        return favouritesStr;
    }

    // Method to load recipe data from the Spoonacular API
    private void loadData() {


        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + id.trim() + "/information";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String recipeId = response.getString("id");
                            recipeTitle = response.getString("title");
                            String imageUrl = response.getString("image");
                            desc = response.getString("summary");
                            JSONArray ingredients = response.getJSONArray("extendedIngredients");
                            JSONArray instructions = response.getJSONArray("analyzedInstructions");

                            ArrayList<String> ingredientsNames = new ArrayList<>();
                            for (int i = 0; i < ingredients.length(); i++) {
                                JSONObject ingredient = (JSONObject) ingredients.get(i);
                                String image = ingredient.getString("image");
                                String name = ingredient.getString("name");

                                ingredientArrayList.add(new Ingredient(ingredient.getString("id"), name));
                                ingredientHashMap.put(name, new Ingredient(false , numRecipe));


                                ingredientsNames.add(name);
                            }
                            setIngredientsNameRV(ingredientsNames);

                            ArrayList<stepsItem> steps = new ArrayList<>();
                            JSONArray beforeSteps;
                            for (int i = 0; i < instructions.length(); i++) {
                                JSONObject instruction = (JSONObject) instructions.get(i);
                                beforeSteps = instruction.getJSONArray("steps");
                                for (int j = 0; j < beforeSteps.length(); j++){
                                    JSONObject info = (JSONObject) beforeSteps.get(j);
                                    String number = info.getString("number");
                                    String step = info.getString("step");

                                    stepsItem stepsItemTemp = new stepsItem(number,step);
                                    steps.add(stepsItemTemp);
                                }
                            }
                            stepsRV.setLayoutManager(linearLayoutVertical);
                            stepsRV.setAdapter(new stepsRvAdapter(steps,getApplicationContext()));


                            recipeTitleTv.setText(recipeTitle);
                            descTv.setText(Jsoup.parse(desc).text());
                            Picasso.get().load(imageUrl).resize(530, 500).centerCrop().noFade().into(recipeIv);


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

    private void setIngredientsNameRV(ArrayList<String> ingredients){
        ingredientNamesRv.setLayoutManager(layoutManager);
        ingredientNamesRv.setAdapter(new ingredientPhotoRvAdapter(ingredients));
    }

    // Method to handle click events on UI elements
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
