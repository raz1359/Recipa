package com.example.myapplication;

import static com.example.myapplication.login.mAuth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class FragmentLogin1 extends Fragment implements View.OnClickListener {

    View view;
    ImageView icon;
    TextView passwordFail;
    static EditText etEmail,etPassword, etPasswordConfirm;
    Button btnCreate;
    private static final String TAG = "Raz";
    public Context currentContext;



    public FragmentLogin1(Context currentContext) {
        this.currentContext = currentContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login1, container, false);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.editTextEmail);
        etPassword = view.findViewById(R.id.editTextPassword);
        etPasswordConfirm = view.findViewById(R.id.editTextConfirmPassword);
        btnCreate = view.findViewById(R.id.create);
        icon = view.findViewById(R.id.icon_error);
        passwordFail = view.findViewById(R.id.passwordNotEqual);

        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String emailTemp = etEmail.getText().toString().trim();
        String passwordTemp = etPassword.getText().toString().trim();
        String passwordConfrimTemp = etPasswordConfirm.getText().toString().trim();



        Log.d(TAG, "email: " + emailTemp + " password: " + passwordTemp + " confirm password: " + passwordConfrimTemp
        + " is passwords equal: " + passwordEqualse(passwordTemp,passwordConfrimTemp));

        if(passwordEqualse(passwordTemp , passwordConfrimTemp)) {
            Log.d(TAG, "before create");
                createAccount(emailTemp, passwordTemp);
            Log.d(TAG, "After create");
        } else {
            // etPasswordConfirm.setError("password not Equalse");
            // etPassword.setText("");
            // etPasswordConfirm.setText("");
            // etPassword.setHintTextColor(getResources().getColor(R.color.red));
            // etPasswordConfirm.setHintTextColor(getResources().getColor(R.color.red));
            // icon.setAlpha(1);
            // ס    passwordFail.setAlpha(1);
            // need to change icon Alpha to 1 so you can see it


        }


    }

    public Boolean passwordEqualse (String password1 , String password2) {

        if (password1.equals(password2))
            return true;
        return false;
    }


    public void createAccount(String email, String password) {
        // START create_user_with_email
        Log.d(TAG, "createAccount: Inside create account");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) currentContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(currentContext,FillProfile.class);
                            startActivity(intent);
                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                etPassword.setError(getString(R.string.error_weak_password));
                                etPassword.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                etEmail.setError(getString(R.string.error_invalid_email));
                                etEmail.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                etEmail.setError(getString(R.string.error_user_exists));
                                etEmail.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(currentContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}

