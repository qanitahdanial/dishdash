package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CuisineActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout restaurantListLayout;
    private Button btnNext;
    private ArrayList<String> selectedCuisines;
    private ArrayList<Restaurant> selectedRestaurants = new ArrayList<>();
    private int currentCuisineIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        restaurantListLayout = findViewById(R.id.restaurantListLayout);
        btnNext = findViewById(R.id.btnNext);
        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        selectedCuisines = intent.getStringArrayListExtra("cuisines");

        if (selectedCuisines == null || selectedCuisines.isEmpty()) {
            Toast.makeText(this, "No cuisines selected.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayRestaurantsForCuisine();

        btnNext.setOnClickListener(v -> {
            saveSelectedRestaurants();
            if (currentCuisineIndex < selectedCuisines.size() - 1) {
                currentCuisineIndex++;
                displayRestaurantsForCuisine();
            } else {
                navigateToSelectionScreen(); // Update navigation
            }
        });
    }

    private void displayRestaurantsForCuisine() {
        restaurantListLayout.removeAllViews();
        String cuisine = selectedCuisines.get(currentCuisineIndex);
        ArrayList<Restaurant> restaurants = db.getRestaurantsByCuisine(cuisine);

        if (restaurants.isEmpty()) {
            Toast.makeText(this, "No restaurants found for " + cuisine, Toast.LENGTH_SHORT).show();
            return;
        }

        for (Restaurant restaurant : restaurants) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(restaurant.getName() + " | Price: " + restaurant.getPrice() + " | Rating: " + restaurant.getRating());
            checkBox.setTag(restaurant);
            restaurantListLayout.addView(checkBox);
        }
    }

    private void saveSelectedRestaurants() {
        for (int i = 0; i < restaurantListLayout.getChildCount(); i++) {
            View view = restaurantListLayout.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    selectedRestaurants.add((Restaurant) checkBox.getTag());
                }
            }
        }
    }

    private void navigateToSelectionScreen() {
        Intent intent = new Intent(this, DecisionActivity.class); // Use new SelectionActivity
        intent.putParcelableArrayListExtra("selectedRestaurants", selectedRestaurants);
        startActivity(intent);
        finish();
    }
}
