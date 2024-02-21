// Import statements
package com.example.myapplication;

import static android.nfc.NdefRecord.createUri;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.activity.result.ActivityResult;
import  androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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


import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    // Declare UI elements
    private static final String TAG = "raz";
    Button btnContinue,btnGalley,btnCamera;
    ImageButton btnSignOut;
    public ImageView backArrow, profilePic;
    public Uri imageUri;
    public EditText etFullName, etNickname;
    public String uID, url, mCurrentPhotoPath;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    public Profile profile;
    ActivityResultLauncher<String> arlGallery;
    ActivityResultLauncher<Intent> arlCamera;



    // Connect to real time database
    DatabaseReference  datebaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        // Initialize UI elements
        backArrow = findViewById(R.id.ivbackProfile);
        backArrow.setOnClickListener(this);

        btnGalley = findViewById(R.id.galleyBT);
        btnCamera = findViewById(R.id.cameraBT);

        etFullName = findViewById(R.id.etfullName);
        etFullName.setOnClickListener(this);

        etNickname = findViewById(R.id.etnickname);
        etNickname.setOnClickListener(this);

        profilePic = findViewById(R.id.profileImage);
        profilePic.setOnClickListener(this);
        Log.d(TAG, "after");

        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);

        btnSignOut = findViewById(R.id.btnSignout);
        btnSignOut.setOnClickListener(this);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();
        Log.d(TAG, uID);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Set up ActivityResultLauncher for gallery
        arlGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    imageUri = result;
                    Picasso.get().load(result).resize(470, 470).centerCrop().noFade().into(profilePic);
                    uploadPicture();
                }
            }
        });

        arlCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult() ,
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult( ActivityResult result ) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap =(Bitmap) bundle.get("data");
                            Picasso.get().load(getImageUri(getApplicationContext(), bitmap))
                                    .resize(470, 470).centerCrop().noFade().into(profilePic);
                            Log.d(TAG , "onActivityResult: " + getImageUri(getApplicationContext(), bitmap));
                            imageUri = getImageUri(getApplicationContext(), bitmap);
                            uploadPicture();

                        }
                    }
                }
        );

                // Set onClickListener for profile picture selection
                btnGalley.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View view ) {
                        arlGallery.launch("image/*");
                    }
                });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_CALENDAR}, 100);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                arlCamera.launch(intent);
            }
        });

        // Retrieve data from Firebase
        retrieveDate();
    }
    public Uri getImageUri( Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage
                (inContext.getContentResolver(), inImage, "Title", null);
        Log.d(TAG , "getImageUri: " + path);
        return Uri.parse(path);
    }



    // Method to upload the selected picture to Firebase Storage
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
                                Picasso.get().load(uri).resize(370,370).centerCrop().into(profilePic);
                                url = imageUri.toString();
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

    // Method to retrieve data from Firebase
    private void retrieveDate() {
        // Retrieve FullName, Nickname, Profile Image
        datebaseReference.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profileTemp = snapshot.getValue(Profile.class);

                if (profileTemp != null && profileTemp.getImageUri() != null) {

                    // Set EditText values and load profile image
                    etFullName.setText(profileTemp.getFullName());
                    etNickname.setText(profileTemp.getNickName());
                    url = profileTemp.getImageUri();
                    Picasso.get().load(url).resize(470, 470).centerCrop().noFade().into(profilePic);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled

            }
        });
    }

    // onClick method, called when a UI element is clicked
    @Override
    public void onClick(View view) {

        Intent intent;
        if (view.getId() == R.id.btnSignout) {
            mAuth.signOut();
            intent = new Intent(this, login.class);
            startActivity(intent);
            finish();

        } else if (view.getId() == R.id.ivbackProfile) {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent,0);

        } else {
            String tempName = etFullName.getText().toString();
            String tempNickname = etNickname.getText().toString();

            profile = new Profile(tempName, tempNickname, url, "");

            datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(profile);
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 0);

        }
    }

}