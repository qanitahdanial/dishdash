package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurants.db";
    private static final int DATABASE_VERSION = 3;

    private final Context context;

    // Table and column names
    private static final String TABLE_NAME = "Restaurants";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_CUISINE = "cuisine";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_MENU_LINK = "menu_link";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_CUISINE + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_RATING + " TEXT, " +
                COLUMN_MENU_LINK + " TEXT);";
        db.execSQL(TABLE_CREATE);
        populateDatabaseFromCSV(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void populateDatabaseFromCSV(SQLiteDatabase db) {
        try {
            Log.d("DatabaseHelper", "Starting CSV population");
            InputStream is = context.getResources().openRawResource(R.raw.restaurants);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 6) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_NAME, values[0].trim());
                    contentValues.put(COLUMN_LOCATION, values[1].trim());
                    contentValues.put(COLUMN_CUISINE, values[2].trim());
                    contentValues.put(COLUMN_PRICE, values[3].trim());
                    contentValues.put(COLUMN_RATING, values[4].trim());
                    contentValues.put(COLUMN_MENU_LINK, values[5].trim());
                    long result = db.insert(TABLE_NAME, null, contentValues);
                    if (result != -1) {
                        Log.d("DatabaseHelper", "Inserted: " + values[0]);
                    } else {
                        Log.e("DatabaseHelper", "Failed to insert: " + values[0]);
                    }
                } else {
                    Log.e("DatabaseHelper", "Incorrect format in line: " + line);
                }
            }
            reader.close();
            Log.d("DatabaseHelper", "Finished CSV population");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error reading CSV file", e);
        }
    }

    public ArrayList<String> getCuisines() {
        ArrayList<String> cuisines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_CUISINE + " FROM " + TABLE_NAME, null)) {
            while (cursor.moveToNext()) {
                cuisines.add(cursor.getString(cursor.getColumnIndex(COLUMN_CUISINE)));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching cuisines", e);
        }
        return cuisines;
    }

    public ArrayList<Restaurant> getRestaurantsByCuisineAndLocation(String cuisine, String location) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CUISINE + " = ? AND " + COLUMN_LOCATION + " = ?",
                new String[]{cuisine, location})) {
            while (cursor.moveToNext()) {
                restaurants.add(new Restaurant(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CUISINE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MENU_LINK))
                ));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching restaurants by cuisine and location", e);
        }
        return restaurants;
    }

    @SuppressLint("Range")
    public String getMenuLinkById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String menuLink = null;
        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_MENU_LINK + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)})) {
            if (cursor.moveToFirst()) {
                menuLink = cursor.getString(cursor.getColumnIndex(COLUMN_MENU_LINK));
            }
        }
        return menuLink;
    }

    public ArrayList<Restaurant> getRestaurantsByCuisine(String cuisine) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CUISINE + " = ?", new String[]{cuisine})) {
            while (cursor.moveToNext()) {
                restaurants.add(new Restaurant(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CUISINE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MENU_LINK))
                ));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching restaurants by cuisine", e);
        }
        return restaurants;
    }
}
