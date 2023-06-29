package com.example.myapplication;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.prefs.Preferences;

public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    public static final  String NameLast_text = "" , NicknameLast_name = "";

    private static final String TAG = "raz";
    public Button button;
    public ImageView imageView;
    public EditText fullName, nickname;
    public String uID;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference  datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");
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
        Log.d(TAG, "after");

        button = findViewById(R.id.btnContinue);

        final SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(this);



        fullName.setText(pref1.getString(NameLast_text, ""));
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                pref1.edit().putString(NameLast_text , editable.toString()).commit();
            }

        });

        nickname.setText(pref2.getString(NicknameLast_name, ""));
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                pref2.edit().putString(NicknameLast_name , editable.toString()).commit();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String tempName = fullName.getText().toString();
                final String tempNickname = nickname.getText().toString();

                 datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("Fullname").setValue(tempName);
                 datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("Nickname").setValue(tempNickname);
                // datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("Email").setValue(email);

            }
        });

        // Firebase initi
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