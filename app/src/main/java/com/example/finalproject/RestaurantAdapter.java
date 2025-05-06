package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.widget.Toast;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private ArrayList<Restaurant> restaurantList;
    private OnRestaurantDecisionListener listener;

    // Interface for handling user actions
    public interface OnRestaurantDecisionListener {
        void onYesClicked(Restaurant restaurant);

        void onNoClicked(Restaurant restaurant);
    }

    public RestaurantAdapter(ArrayList<Restaurant> restaurantList, OnRestaurantDecisionListener listener) {
        this.restaurantList = restaurantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant, listener);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRestaurantName;
        private TextView tvRestaurantDetails;
        private Button btnYes, btnNo, btnViewMenu;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantDetails = itemView.findViewById(R.id.tvRestaurantDetails);
            btnYes = itemView.findViewById(R.id.btnYes);
            btnNo = itemView.findViewById(R.id.btnNo);
            btnViewMenu = itemView.findViewById(R.id.btnViewMenu); // Button to view menu
        }

        public void bind(Restaurant restaurant, OnRestaurantDecisionListener listener) {
            String restaurantName = restaurant.getName() != null ? restaurant.getName() : "Unknown Name";
            String restaurantPrice = restaurant.getPrice() != null && !restaurant.getPrice().isEmpty()
                    ? (restaurant.getPrice().startsWith("$") ? restaurant.getPrice() : "$" + restaurant.getPrice())
                    : "Price not available";
            String restaurantRating = restaurant.getRatingAsDouble() > 0
                    ? restaurant.getRatingAsDouble() + " stars"
                    : "Rating not available";
            String restaurantMenuLink = restaurant.getMenuLink() != null ? restaurant.getMenuLink() : "";

            tvRestaurantName.setText("Rank " + restaurant.getRank() + ": " + restaurantName);
            tvRestaurantDetails.setText("Price: " + restaurantPrice + " | Rating: " + restaurantRating);

            btnYes.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onYesClicked(restaurant);
                }
            });

            btnNo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNoClicked(restaurant);
                }
            });

            if (!restaurantMenuLink.isEmpty()) {
                btnViewMenu.setVisibility(View.VISIBLE);
                btnViewMenu.setOnClickListener(v -> {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantMenuLink));
                        itemView.getContext().startActivity(browserIntent);
                    } catch (Exception e) {
                        Toast.makeText(itemView.getContext(), "Invalid menu link", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                btnViewMenu.setVisibility(View.GONE);
            }
        }
    }
}
