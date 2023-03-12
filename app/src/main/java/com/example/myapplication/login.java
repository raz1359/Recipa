package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends AppCompatActivity {

    Button fragment1bt, fragment2btn;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final String TAG = "login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        fragment1bt = findViewById(R.id.fragment1btn);
        fragment2btn = findViewById(R.id.fragment2btn);

       replaceFragment(new FragmentLogin1(this));
       findViewById(R.id.lineSignUp).setAlpha(1);
        TextView signUp = findViewById(R.id.fragment2btn);
        signUp.setTextColor(Color.parseColor("#8ADBA5"));


        fragment1bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new FragmentLogin1(login.this));
                overridePendingTransition(0,0);

                findViewById(R.id.lineSignUp).setAlpha(1);
                findViewById(R.id.lineSignIn).setAlpha(0);

                TextView signin = findViewById(R.id.fragment2btn);
                signin.setTextColor(Color.parseColor("#8ADBA5"));

                TextView signUp = findViewById(R.id.fragment1btn);
                signUp.setTextColor(Color.parseColor("#1BAC4B"));



            }
        });


        fragment2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                repalceFragment(new FragmentLogin2(login.this));
                overridePendingTransition(0,0);
                findViewById(R.id.lineSignUp).setAlpha(0);
                findViewById(R.id.lineSignIn).setAlpha(1);

                TextView signUp = findViewById(R.id.fragment1btn);
                signUp.setTextColor(Color.parseColor("#8ADBA5"));

                TextView signin = findViewById(R.id.fragment2btn);
                signin.setTextColor(Color.parseColor("#1BAC4B"));

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(login.this,MainActivity.class));
        }
    }
    // [END on_start_check_user]


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void repalceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void updateUI(FirebaseUser user) {

    }

}