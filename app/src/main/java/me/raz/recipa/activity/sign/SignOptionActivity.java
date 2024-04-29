package me.raz.recipa.activity.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.raz.recipa.R;
import me.raz.recipa.activity.MainActivity;


public class SignOptionActivity extends AppCompatActivity {

    Button fragment1bt, fragment2btn;

    // Firebase authentication instance
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final String TAG = "login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements and fragments
        fragment1bt = findViewById(R.id.fragment1btn);
        fragment2btn = findViewById(R.id.fragment2btn);

        // Set the initial fragment to FragmentLogin1
        replaceFragment(new SignUpActivity(this));
        findViewById(R.id.lineSignUp).setAlpha(1);
        TextView signUp = findViewById(R.id.fragment2btn);
        signUp.setTextColor(Color.parseColor("#8ADBA5"));

        // Set click listeners for fragment buttons
        fragment1bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to FragmentLogin1
                replaceFragment(new SignUpActivity(SignOptionActivity.this));
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

                repalceFragment(new SignInActivity(SignOptionActivity.this));
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
            startActivity(new Intent(SignOptionActivity.this, MainActivity.class));
        }
    }
    // [END on_start_check_user]

    // Replace the current fragment with a new one
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    // Replace the current fragment with a new one
    private void repalceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    // Method to update UI based on the user's authentication state
    private void updateUI(FirebaseUser user) {
        // Implement UI updates as needed
    }

}