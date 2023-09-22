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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.prefs.Preferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FillProfile extends AppCompatActivity implements View.OnClickListener {

    public final  String NameLast_text = "" , NicknameLast_name = "";

    private final String TAG = "raz";
    public Button button;
    public ImageView imageView, profilePic;
    public Uri imageUri;
    public EditText fullName, nickname;
    public String uID;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;


    // Connect to real time database
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

        profilePic = findViewById(R.id.profileImage);
        profilePic.setOnClickListener(this);
        Log.d(TAG, "after");

        button = findViewById(R.id.btnContinue);

        final SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String tempName = fullName.getText().toString();
                final String tempNickname = nickname.getText().toString();

                 datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("Fullname").setValue(tempName);
                 datebaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("Nickname").setValue(tempNickname);

            }
        });

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

                pref1.edit().putString(NameLast_text , fullName.getText().toString()).commit();
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
                pref2.edit().putString(NicknameLast_name , nickname.getText().toString()).commit();

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic.setOnClickListener(view -> choosePicture());

        // Firebase initi
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();
        Log.d(TAG, uID);
    }


    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profilePic.setImageBitmap(getCircularBitmap(decodeUri(imageUri)));
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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int diameter = Math.min(width, height);
        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private Bitmap decodeUri(android.net.Uri uri) {
        try {
            // Decode the image from the URI using BitmapFactory
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}