package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    public Button button;
    public ImageView imageView;
    public EditText fullName, nickname;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference =db.getReference().child("Users");

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

                HashMap<String, String> usermap = new HashMap<>();
                usermap.put("name", tempName);
                usermap.put("nickname", tempNickname);

                reference.push().setValue(usermap);
            }
        });
    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}