package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText locationInput;
    private LinearLayout cuisineSwitchLayout;
    private Button btnSubmit;
    private DatabaseHelper db;
    private ArrayList<String> selectedCuisines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        db = new DatabaseHelper(this); // Initialize DatabaseHelper
        locationInput = findViewById(R.id.locationInput); // User input for location
        cuisineSwitchLayout = findViewById(R.id.cuisineSwitchLayout); // Layout to add switches
        btnSubmit = findViewById(R.id.btnSubmit); // Submit button

        // Dynamically create switches for cuisines
        setupCuisineSwitches();

        // Handle submit button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });
    }

    /**
     * Dynamically creates switches for each cuisine retrieved from the database.
     */
    private void setupCuisineSwitches() {
        ArrayList<String> cuisines = db.getCuisines(); // Fetch cuisines from the database

        if (cuisines.isEmpty()) {
            Toast.makeText(this, "No cuisines available in the database", Toast.LENGTH_SHORT).show();
            return; // Exit if no cuisines exist
        }

        for (String cuisine : cuisines) {
            Log.d("MainActivity", "Adding switch for cuisine: " + cuisine);
            Switch cuisineSwitch = new Switch(this); // Create a new switch for each cuisine
            cuisineSwitch.setText(cuisine); // Set the cuisine name as the switch text
            cuisineSwitchLayout.addView(cuisineSwitch); // Add the switch to the layout
        }
    }

    /**
     * Handles form submission by validating inputs and navigating to the next activity.
     */
    private void handleFormSubmission() {
        selectedCuisines.clear(); // Clear previous selections
        String location = locationInput.getText().toString().trim(); // Get location input

        // Collect selected cuisines
        for (int i = 0; i < cuisineSwitchLayout.getChildCount(); i++) {
            View child = cuisineSwitchLayout.getChildAt(i);
            if (child instanceof Switch) {
                Switch cuisineSwitch = (Switch) child;
                if (cuisineSwitch.isChecked()) {
                    selectedCuisines.add(cuisineSwitch.getText().toString()); // Add selected cuisine
                }
            }
        }

        // Validate location
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter your location", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!location.equalsIgnoreCase("Greenville NC")) {
            Toast.makeText(this, "Sorry, we only support Greenville NC at this time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate selected cuisines
        if (selectedCuisines.isEmpty()) {
            Toast.makeText(this, "Please select at least one cuisine", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log selected cuisines and location
        Log.d("MainActivity", "Selected Cuisines: " + selectedCuisines + ", Location: " + location);

        // Start the CuisineActivity
        Intent intent = new Intent(MainActivity.this, CuisineActivity.class);
        intent.putStringArrayListExtra("cuisines", selectedCuisines); // Pass selected cuisines
        intent.putExtra("location", location); // Pass location
        startActivity(intent);
    }
}
