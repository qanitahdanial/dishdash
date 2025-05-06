package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DecisionActivity extends AppCompatActivity {

    private RecyclerView rvRestaurantList;
    private ArrayList<Restaurant> selectedRestaurants;
    private RestaurantAdapter adapter; // Declare the adapter as a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);

        rvRestaurantList = findViewById(R.id.rvRestaurantList);

        // Retrieve the list of selected restaurants from the intent
        selectedRestaurants = (ArrayList<Restaurant>) getIntent().getSerializableExtra("selectedRestaurants");

        if (selectedRestaurants == null || selectedRestaurants.isEmpty()) {
            Log.e("DecisionActivity", "selectedRestaurants is empty or null");
            finish();
            return;
        }

        // Randomize and rank the restaurants
        randomizeAndRankRestaurants(selectedRestaurants);

        // Initialize the adapter
        adapter = new RestaurantAdapter(selectedRestaurants, new RestaurantAdapter.OnRestaurantDecisionListener() {
            @Override
            public void onYesClicked(Restaurant restaurant) {
                Intent intent = new Intent(DecisionActivity.this, FinalRestaurantActivity.class);
                intent.putExtra("finalRestaurant", (Parcelable) restaurant);
                startActivity(intent);
            }

            @Override
            public void onNoClicked(Restaurant restaurant) {
                selectedRestaurants.remove(restaurant);
                adapter.notifyDataSetChanged(); // Update adapter safely
            }
        });

        // Set up the RecyclerView
        rvRestaurantList.setLayoutManager(new LinearLayoutManager(this));
        rvRestaurantList.setAdapter(adapter);
    }

    public void randomizeAndRankRestaurants(ArrayList<Restaurant> selectedRestaurants) {
        for (int i = 0; i < selectedRestaurants.size(); i++) {
            selectedRestaurants.get(i).setRank(i + 1); // Set rank starting from 1
        }
    }
}
