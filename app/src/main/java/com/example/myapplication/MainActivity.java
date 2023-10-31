package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

import java.util.concurrent.BlockingDeque;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public Button button;
    public ImageView notificationImage, searchImage,profileImage;
    public EditText searchBar;
    private static final String TAG = "raz";
    private BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    public String uID;
    FirebaseUser currentUser;

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

        button = findViewById(R.id.btnSignout);
        button.setOnClickListener(this);

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


    }

    private void retriveDate() {
        datebaseReference.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profileTemp = snapshot.getValue(Profile.class);

                if (profileTemp != null) {
                    String url = profileTemp.getImageUri();
                    Picasso.get().load(url).resize(250, 250).centerCrop().into(profileImage);


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
            case R.id.btnSignout:
                Log.d(TAG, "onClick: ");
                mAuth.signOut();
                intent = new Intent(this, login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent,0);
                break;
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
