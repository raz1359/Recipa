package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Notification_activity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    ArrayList<Item> notificationList;
    ListView lv;
    MyAdapter myAdapter;
    private static final String TAG = "Raz";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Bitmap heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart_notification);
        Bitmap user = BitmapFactory.decodeResource(getResources(),R.drawable.user_notification);

        Item t1 = new Item("'Name' Add Your Recipe", "aa" , "Your italy pasta been add to Raz Vorman Favorite list" , heart);
        Item t2 = new Item("Account Setup Successful!", "27 Aug,2022  |  17:34 PM" , "Your account creation is successful, you can now experience our services." , user);
        Item t3 = new Item("liam (:", "5 Mar,2023 | 11:15","liam the queen",heart);
        notificationList = new ArrayList<Item>();
        notificationList.add(t1);
        notificationList.add(t2);
        notificationList.add(t3);

        myAdapter = new MyAdapter(this,0,0,notificationList);
        lv = findViewById(R.id.lv);
        lv.setAdapter(myAdapter);

        imageView = findViewById(R.id.ivBack);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}