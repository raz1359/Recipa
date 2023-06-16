package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Raz";
    public Button button;
    public ImageView imageView;
    public EditText fullName, nickname;
    public String uID;

    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        imageView = findViewById(R.id.ivbackProfile);
        imageView.setOnClickListener(this);

        fullName = findViewById(R.id.etfullName);
        fullName.setOnClickListener(this);

        nickname = findViewById(R.id.etnickname);
        nickname.setOnClickListener(this);

        button = findViewById(R.id.btnContinue);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempName = fullName.getText().toString();
                String tempNickname = nickname.getText().toString();

                reference.setValue(tempName);
            }
        });

        // Firebase initi
        db = FirebaseDatabase.getInstance("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = db.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();
        Log.d(TAG, uID);
    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}