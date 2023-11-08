package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.prefs.Preferences;

public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    public static final  String NameLast_text = "" , NicknameLast_name = "";

    private static final String TAG = "raz";
    public Button button;
    public ImageView backArrow, profilePic;
    public Uri imageUri;
    public EditText etFullName, etNickname;
    public String uID;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    public Profile profile;


    // Connect to real time database
    DatabaseReference  datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        backArrow = findViewById(R.id.ivbackProfile);
        backArrow.setOnClickListener(this);

        etFullName = findViewById(R.id.etfullName);
        etFullName.setOnClickListener(this);

        etNickname = findViewById(R.id.etnickname);
        etNickname.setOnClickListener(this);

        profilePic = findViewById(R.id.profileImage);
        profilePic.setOnClickListener(this);
        Log.d(TAG, "after");

        button = findViewById(R.id.btnContinue);
        button.setOnClickListener(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        // Firebase initi
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();
        Log.d(TAG, uID);


        retriveDate();
    }

    private void retriveDate() {
        // Retrive fullname, Nickname, Profile Image
        datebaseReference.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profileTemp = snapshot.getValue(Profile.class);

                if (profileTemp != null) {
                    Log.d(TAG, "onDataChange: " + profileTemp.getNickName() + " " + profileTemp.getFullName() + " " + profileTemp.getImageUri());
                    etFullName.setText(profileTemp.fullName);
                    etNickname.setText(profileTemp.getNickName());
                    String url = profileTemp.getImageUri();
                    Picasso.get().load(url).resize(470, 470).centerCrop().noFade().into(profilePic);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = storageReference.child("image/" + randomKey);

        riverRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                        datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("ProfileImage").setValue(imageUri.toString());

                        riverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUri = uri;
                                Log.d(TAG, "onSuccess: download uri: = " + uri.toString());
                                Picasso.get().load(uri).resize(370,370).centerCrop().into(profilePic);
                                datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("ProfileImage").setValue(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

    @Override
    public void onClick(View view) {

        Log.d(TAG, "onClick: " + imageUri.toString());

        if (view.getId() == R.id.ivbackProfile) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent,0);
        } else {
            String tempName = etFullName.getText().toString();
            String tempNickname = etNickname.getText().toString();

            profile = new Profile(tempName,tempNickname,imageUri.toString());
            Log.d(TAG, "onClick: " + profile.getFullName() + " " + profile.getNickName() + " " + profile.getImageUri());

            datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(profile);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent,0);
        }
    }

}