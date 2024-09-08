package com.example.storage_database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.storage_database.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MyDatabaseHelper dbHelper;

    boolean deleteLessThan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        binding.modifyDeleteData.setText("<");

        binding.createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();
            }
        });

        binding.addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                // Compose the first record of database
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);
                // Insert the first row of database

                values.clear();

                // Compose the second record of database
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);
                // Insert the second of database

                updateQueryCountLabel(String.valueOf(queryAllDataCount()));
            }
        });

        binding.updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?", new String[] { "The Da Vinci Code"});

                updateQueryCountLabel(String.valueOf(queryAllDataCount()));
            }
        });

        binding.deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages " + binding.modifyDeleteData.getText() + "?", new String[] { "500" });

                updateQueryCountLabel(String.valueOf(queryAllDataCount()));
            }
        });


        binding.modifyDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLessThan = !deleteLessThan;
                if (deleteLessThan)
                {
                    binding.modifyDeleteData.setText("<");
                }
                else
                {
                    binding.modifyDeleteData.setText(">");
                }
            }
        });

        binding.queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Query all data from "Book"
                Cursor cursor = db.query("Book", null, null, null, null ,null, null);

                updateQueryCountLabel(String.valueOf(cursor.getCount()));

                if (cursor.moveToFirst()) {
                    do {
                        // Get all the record from cursor
 @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
 @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
 @SuppressLint("Range") int pages = cursor.getInt(cursor.getColumnIndex("pages"));
 @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "------------------------");
                        Log.d("MainActivity", "book name is " + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book pages is " + pages);
                        Log.d("MainActivity", "book price is " + price);
                        Log.d("MainActivity", "------------------------");
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

    }

    private int queryAllDataCount()
    {
        Cursor cursor = dbHelper.getWritableDatabase().query("Book", null, null, null, null ,null, null);
        return cursor.getCount();
    }

    private void updateQueryCountLabel(String value)
    {
        binding.queryCount.setText("N = " + value);
    }
}