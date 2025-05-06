package com.example.finalproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FinalRestaurantActivity extends AppCompatActivity {

    private Restaurant finalRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_restaurant); // Matches your provided XML file

        // Retrieve views
        TextView finalRestaurantName = findViewById(R.id.finalRestaurantName);
        TextView finalRestaurantDetails = findViewById(R.id.finalRestaurantDetails);
        TextView finalChoiceMessage = findViewById(R.id.finalChoiceMessage);

        // Retrieve the final restaurant object from the Intent
        finalRestaurant = getIntent().getParcelableExtra("finalRestaurant");

        // Populate views with restaurant details
        if (finalRestaurant != null) {
            finalRestaurantName.setText(finalRestaurant.getName());
            finalRestaurantDetails.setText(
                    "Cuisine: " + finalRestaurant.getCuisineType() + "\n" +
                            "Rating: " + finalRestaurant.getRating() + "\n" +
                            "Price: " + finalRestaurant.getPrice() + "\n" +
                            "Location: " + finalRestaurant.getLocation()
            );
            finalChoiceMessage.setText("Enjoy your meal at " + finalRestaurant.getName() + "!");
        } else {
            finalRestaurantName.setText("No restaurant selected.");
            finalRestaurantDetails.setText("No details available.");
            finalChoiceMessage.setText("Please go back and make another selection.");
        }
    }
}
