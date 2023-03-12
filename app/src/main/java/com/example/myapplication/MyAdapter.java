package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Item> {

    Context context;
    List <Item> objects;


    public MyAdapter(Context context, int resource, int textViewResourceld, List<Item> objects) {
        super(context, resource, textViewResourceld ,objects);

        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_notifs, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvSubTitle = convertView.findViewById(R.id.tvSubTitle);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        ImageView ivImage = convertView.findViewById(R.id.ivImage);

        Item temp = objects.get(position);

        ivImage.setImageBitmap(temp.getBitmap());
        tvDate.setText(String.valueOf(temp.getDate()));
        tvTitle.setText(temp.getTitle());
        tvSubTitle.setText(temp.getSubtitle());

        return convertView;
    }

}
