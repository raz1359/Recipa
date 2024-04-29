package me.raz.recipa.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.raz.recipa.R;
import me.raz.recipa.activity.RecipePageActivity;
import me.raz.recipa.data.HighRatingRecipesItem;

public class RecommendedRecipesAdapter extends RecyclerView.Adapter <RecommendedRecipesHolder> {

    Context context;
    List<HighRatingRecipesItem> list;
    private static final String TAG = "raz";

    // Constructor for the adapter
    public RecommendedRecipesAdapter(Context context, List<HighRatingRecipesItem> list) {
        this.context = context;
        this.list = list;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecommendedRecipesHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recommended_recipes,parent,false);
        return new RecommendedRecipesHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecommendedRecipesHolder holder, int position) {
        // Get the data model based on position
        HighRatingRecipesItem highRaitingRecipesItem = list.get(position);

        // Set the data for each item in the RecyclerView
        holder.getTitleView().setText(highRaitingRecipesItem.getName());
        Picasso.get().load(highRaitingRecipesItem.getImage()).resize(500,500).centerCrop().into(holder.getImageView());

        // Check if the recipe is marked as a favorite
        if (list.get(position).isFavourite())
            holder.getLikeBtn().setImageResource(R.drawable.heart_clicked);

        // Handle the click event for the like button
        holder.getLikeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If already a favorite, remove from favorites
                if (list.get(position).isFavourite()){
                    holder.getLikeBtn().setImageResource(R.drawable.heart_not_clicked);
                    removeFavourite(position);
                }else{
                    // If not a favorite, add to favorites
                    holder.getLikeBtn().setImageResource(R.drawable.heart_clicked);
                    addFavourite(position);
                }
            }
        });

        // Handle the click event for the entire item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start RecipePageActivity when an item is clicked
                Intent intent = new Intent(holder.itemView.getContext(), RecipePageActivity.class);

                intent.putExtra("id", list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);


            }
        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    // Method to add a recipe to the user's favorites
    private void addFavourite(int position){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipa-e3b07-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");

        dbReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {

                                String favorites = task.getResult().getValue().toString();

                                Log.d(TAG, "onComplete: 33" + favorites);


                                if (favorites != null) {

                                    list.get(position).setFavourite(true);
                                    favorites += "," + list.get(position).getId();
                                    dbReference.setValue(favorites);
                                    Log.d(TAG, "Updated favorites: " + favorites);
                                } else {

                                    list.get(position).setFavourite(true);

                                    String favourites = buildFavsString();

                                    dbReference.setValue(favourites);
                                    Log.d(TAG, "New favorites: " + favourites);

                                }

                            } else {
                            Log.d(TAG, "onComplete: task not successful");
                        }
                }
        });
    }

    // Method to remove a recipe from the user's favorites
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

    // Method to build a string of favorites for storage in the database
    private String buildFavsString() {
        if (list.isEmpty())
            return "";

        StringBuilder favouritesStr = new StringBuilder();
        for (HighRatingRecipesItem item : list) {
            if (item.isFavourite()) {
                favouritesStr.append(item.getId()).append(",");
            }
        }
        return favouritesStr.substring(0, favouritesStr.length() - 1);
    }
 }

