package me.raz.recipa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import me.raz.recipa.R;
import me.raz.recipa.adapters.HighRatingRecipesAdapter;
import me.raz.recipa.adapters.RecommendedRecipesAdapter;
import me.raz.recipa.data.HighRatingRecipesItem;
import me.raz.recipa.data.Profile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // Declare UI elements
    public Button button;
    public ImageView notificationImage, searchImage,profileImage, favoritesImage, searchIcon;
    public EditText searchBar;
    private static final String TAG = "raz";
    private BottomNavigationView bottomNavigationView;
    private RecyclerView highRaitingRecyclerView,recommendedRecipesRecyclerView;
    private ImageButton ibHeart;

    // Firebase authentication and user information1on
    private FirebaseAuth mAuth;
    public String uID;
    private FirebaseUser currentUser;


    // RecyclerViews and adapters for displaying high rating and recommended recipes
    private LinearLayoutManager linearLayoutManagerHorizontal , linearLayoutManagerVertical;
    private List<HighRatingRecipesItem> listHighRating = new ArrayList<>();
    private List<HighRatingRecipesItem> listRecommendedRecipes = new ArrayList<>();
    private List<String> favourites = new ArrayList<>();


    // Firebase Realtime Database reference
    private DatabaseReference datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");


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

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction( TextView textView , int i , KeyEvent keyEvent ) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);

                    intent.putExtra("search" ,searchBar.getText().toString().trim());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
               return false;
            }
        });

        // Bottom navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeBT:
                    return true;

                case R.id.searchBT:
                    //startActivity(new Intent(getApplicationContext(), Search.class));
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,0);
                    finish();
                    return true;
                case R.id.shoppingBT:
                    Intent intent1 = new Intent(this, GroceriesCartActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent1,0);
                    finish();
                    return true;

            }
            return false;

        });

        CoordinatorLayout.LayoutParams layoutParams= ((CoordinatorLayout.LayoutParams)
                bottomNavigationView.getLayoutParams());
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

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


                                                HighRatingRecipesItem tempHighRatingRecipesItem = new HighRatingRecipesItem(title, imageURL, id, isFavourite);

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

                                                listHighRating.add(new HighRatingRecipesItem(title, imageURL, id, isFavourite));

                                                //compare ids and checking that favorits are on the recommended list
                                                for (int j = 0; j < listRecommendedRecipes.size(); j++) {
                                                    for (int x = 0; x < favourites.size(); x++) {
                                                        if (listRecommendedRecipes.get(j).getId().equals(favourites.get(x))) {
                                                            listRecommendedRecipes.get(j).setFavourite(true);
                                                        }
                                                    }
                                                }


                                            }
                                            highRaitingRecyclerView.setLayoutManager(linearLayoutManagerHorizontal);
                                            highRaitingRecyclerView.setAdapter(new HighRatingRecipesAdapter(getApplicationContext(), listHighRating));

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

    //make categories work
    public void breakfast(View view) {

        Intent intent = new Intent(this, SearchActivity.class);

        intent.putExtra("breakfast" ,"breakfast");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void lunch(View view) {

        Intent intent = new Intent(this, SearchActivity.class);

        intent.putExtra("lunch" ,"lunch");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void dinner(View view) {

        Intent intent = new Intent(this, SearchActivity.class);

        intent.putExtra("dinner" ,"dinner");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.notification:
                intent = new Intent(this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
                break;
            case R.id.favorite_icon:
                intent = new Intent(this, FavoriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
                break;
            case R.id.emptyProfile:
                intent = new Intent(this, FillProfileInformationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

        private int height;

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
            height = child.getHeight();
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                           BottomNavigationView child, @NonNull
                                           View directTargetChild, @NonNull View target,
                                           int axes, int type)
        {
            return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                                   @NonNull View target, int dxConsumed, int dyConsumed,
                                   int dxUnconsumed, int dyUnconsumed,
                                   @ViewCompat.NestedScrollType int type)
        {
            if (dyConsumed > 0) {
                slideDown(child);
            } else if (dyConsumed < 0) {
                slideUp(child);
            }
        }

        private void slideUp(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(0).setDuration(200);
        }

        private void slideDown(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(height).setDuration(200);
        }
    }

}
