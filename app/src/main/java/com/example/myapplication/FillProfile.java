package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    public ImageView imageView;
    public EditText fullName, nickname;

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
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        String tempName = fullName.getText().toString().trim();
        String tempNickname = nickname.getText().toString().trim();
    }
}