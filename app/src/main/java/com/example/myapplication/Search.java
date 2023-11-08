package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search extends AppCompatActivity implements View.OnClickListener {

    public ImageView searchImage;
    public EditText searchBar;
    public TextView changeText;
    public Button buttonAPI;
    private static final String TAG = "raz";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchImage= findViewById(R.id.searchIcon);

        changeText = findViewById(R.id.textView7);

        buttonAPI = findViewById(R.id.buttonAPI);
        buttonAPI.setOnClickListener(this);




//        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search_selected));
//                    searchImage.setImageResource(R.drawable.searchbar_onpng);
//                } else {
//                    searchBar.setBackground(ContextCompat.getDrawable(Search.this, R.drawable.edit_text_search));
//                    searchImage.setImageResource(R.drawable.search_icon2);
//                }
//            }
//        });

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


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.buttonAPI) {
            String url = "https://api.spoonacular.com/recipes/complexSearch?apiKey=9a5a4e3d51fa4468aab3ffa22a94a122&query=chicken&=";
            Log.d(TAG, "onResponse: ");
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
                                    Log.d(TAG, title);
                                }
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
    }
}
