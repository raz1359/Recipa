package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecommendedRecipesAdapter extends RecyclerView.Adapter <RecommendedRecipesHolder> {

    Context context;
    List<HighRaitingRecipesItem> list;
    private static final String TAG = "raz";

    public RecommendedRecipesAdapter(Context context, List<HighRaitingRecipesItem> list) {
        this.context = context;
        this.list = list;
        Log.d(TAG, "RecommendedRecipesAdapter: after constructor");
    }

    @NonNull
    @Override
    public RecommendedRecipesHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recommended_recipes,parent,false);
        return new RecommendedRecipesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedRecipesHolder holder, int position) {
        HighRaitingRecipesItem highRaitingRecipesItem = list.get(position);

        holder.titleView.setText(highRaitingRecipesItem.getName());
        Picasso.get().load(highRaitingRecipesItem.getImage()).resize(500,500).centerCrop().into(holder.imageView1);
        Log.d(TAG, "onBindViewHolder: in adapter holder");

        if (list.get(position).isFavourite == true)
            holder.likeBtn.setImageResource(R.drawable.heart_clicked);

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).isFavourite){
                    holder.likeBtn.setImageResource(R.drawable.heart_not_clicked);
                    removeFavourite(position);
                }else{
                    holder.likeBtn.setImageResource(R.drawable.heart_clicked);
                    addFavourite(position);
                }
                //Toast.makeText(holder.itemView.getContext(), "Like btn clicked, position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), RecipePageActivity.class);

                Log.d(TAG, "onClick id: " + list.get(position).getId());
                intent.putExtra("id", list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    private void addFavourite(int position){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");

        dbReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            list.get(position).setFavourite(true);

                            String favourites = buildFavsString();

                            dbReference.setValue(favourites);
                        }
                    }
                });
    }

    private void removeFavourite(int position){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");

        dbReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    list.get(position).setFavourite(false);

                    String favouritesString = buildFavsString();

                    dbReference.setValue(favouritesString);
                }
            }
        });
    }

    private String buildFavsString(){
        String favouritesStr = "";
        for (HighRaitingRecipesItem item : list){
            if (item.isFavourite) {

                if (favouritesStr.isEmpty()) favouritesStr = item.id;

                else favouritesStr += "," + item.id;
            }
        }
        return favouritesStr;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
