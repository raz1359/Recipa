package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.BlockingDeque;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Button button;
    public ImageView imageView, searchImage;
    public EditText searchBar;
    private static final String TAG = "raz";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_bar);

        button = findViewById(R.id.btnSignout);
        button.setOnClickListener(this);

        imageView = findViewById(R.id.notification);
        imageView.setOnClickListener(this);

        imageView = findViewById(R.id.emptyProfile);
        imageView.setOnClickListener(this);

            searchImage= findViewById(R.id.searchIcon);


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
    }

    @Override
    public void onClick(View view) {
        Intent intent;
                switch (view.getId()) {
                    case R.id.btnSignout:
                        Log.d(TAG, "onClick: ");
                        mAuth.signOut();
                        intent = new Intent(this ,login.class);
                        startActivity(intent);
                        break;
                    case R.id.notification:
                        intent = new Intent(this, Notification_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.emptyProfile:
                        intent = new Intent(this, FillProfile.class);
                        startActivity(intent);
                }

    }
}
//android:background="@drawable/edit_text_search_selector"