package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Item> {

    Context context;
    List <Item> objects;
    private static final String TAG = "razz";

   Date currentTime = Calendar.getInstance().getTime();
   //String formattedDate = DateFormat.getDateInstance(DateFormat.AM_PM_FIELD).format(currentTime);

   //String[] splitDate = formattedDate.split(" | ");


    public MyAdapter(Context context, int resource, int textViewResourceld, List<Item> objects) {
        super(context, resource, textViewResourceld ,objects);

        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_notifs, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvSubTitle = convertView.findViewById(R.id.tvSubTitle);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        ImageView ivImage = convertView.findViewById(R.id.ivImage);

        Log.d(TAG, "getView: ");
        Item temp = objects.get(position);

        ivImage.setImageBitmap(temp.getBitmap());
        tvDate.setText(currentTime.toString());
        tvTitle.setText(temp.getTitle());
        tvSubTitle.setText(temp.getSubtitle());



        
        Log.d(TAG, currentTime.toString());
        //Log.d(TAG, formattedDate);
        //Log.d(TAG, splitDate[0].trim());
        //Log.d(TAG, splitDate[1].trim());
        //Log.d(TAG, splitDate[2].trim());


        return convertView;
    }

}
