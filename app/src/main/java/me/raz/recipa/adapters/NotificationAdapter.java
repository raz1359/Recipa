package me.raz.recipa.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.raz.recipa.R;
import me.raz.recipa.data.Item;

public class NotificationAdapter extends ArrayAdapter<Item> {

    Context context;
    List <Item> objects;
    private static final String TAG = "razz";
    SimpleDateFormat format1 = new SimpleDateFormat("d MMM,yyyy" + " | " + "hh:hh a");
    Date currentTime = Calendar.getInstance().getTime();
    String strFormat1 = format1.format(currentTime);

    String formattedDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(currentTime);

    public NotificationAdapter(Context context, int resource, int textViewResourceld, List<Item> objects) {
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
        tvDate.setText(strFormat1);
        tvTitle.setText(temp.getTitle());
        tvSubTitle.setText(temp.getSubtitle());



        
        Log.d(TAG, currentTime.toString());
        Log.d(TAG, formattedDate);
        Log.d(TAG, strFormat1);


        return convertView;
    }

}
