package me.raz.recipa.activity.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.raz.recipa.R;
import me.raz.recipa.activity.MainActivity;

public class SignInActivity extends Fragment implements View.OnClickListener {

    // UI elements
    View view;
    EditText etEmail, etPassword;
    Button btLogin;
    private FirebaseAuth mAuth;


    // Constants
    private static final String TAG = "Raz";
    Context currentContext;

    // Constructor
    public SignInActivity(Context currentContext) {
        this.currentContext = currentContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login2, container, false);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        etEmail = view.findViewById(R.id.editTextEmail);
        etPassword = view.findViewById(R.id.editTextPassword);
        btLogin = view.findViewById(R.id.create);

        // Set onClickListener for Login button
        btLogin.setOnClickListener(this);
    }

    // onClick method
    @Override
    public void onClick(View view) {

        String emailTemp = etEmail.getText().toString().trim();
        String passwordTemp = etPassword.getText().toString().trim();

        Log.d(TAG, "before create");
            signIn(emailTemp,passwordTemp);
        Log.d(TAG, "after create");

    }

    // Method to SignIn user
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) currentContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            Log.w(TAG,  "signInWithEmail:failure", task.getException());
                            Toast.makeText(currentContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                });
    }
}




