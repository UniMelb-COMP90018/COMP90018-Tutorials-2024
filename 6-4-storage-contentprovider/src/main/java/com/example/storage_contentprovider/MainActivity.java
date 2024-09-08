package com.example.storage_contentprovider;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storage_contentprovider.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String newId;
    private ContentProviderClient provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addData.setOnClickListener(view -> {
            // Insert Data
            try {
                Uri uri = Uri.parse("content://com.example.storage_database.provider/book");
                provider = getContentResolver().acquireContentProviderClient(uri);
                ContentValues values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 55.55);
                Uri newUri = provider != null ? provider.insert(uri, values) : null;
                newId = newUri != null ? newUri.getPathSegments().get(1) : null;

                provider.close();
            }
            catch (RemoteException e)
            {
                Log.e("Content Provider Insert Data", "URI is not reachable");
            }
        });

        binding.queryData.setOnClickListener(view -> {
            // Query Data
            try {
                Uri uri = Uri.parse("content://com.example.storage_database.provider/book");
                provider = getContentResolver().acquireContentProviderClient(uri);
                Cursor cursor = provider.query(uri, null, null, null, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
 @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
 @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
 @SuppressLint("Range") int pages = cursor.getInt(cursor.getColumnIndex("pages"));
 @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "book name is " + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book pages is " + pages);
                        Log.d("MainActivity", "book price is " + price);
                    }
                    cursor.close();
                }

                provider.close();
            }
            catch (Exception e)
            {
                Log.e("Content Provider Query", "URI is not reachable");
            }
        });

        binding.updateData.setOnClickListener(view -> {
            // Update Data
            Uri uri = Uri.parse("content://com.example.storage_database.provider/book/" + newId);
            ContentValues values = new ContentValues();
            values.put("name", "A Storm of Swords");
            values.put("pages", 1216);
            values.put("price", 24.05);
            getContentResolver().update(uri, values, null, null);
        });

        binding.deleteData.setOnClickListener(view -> {
            // Delete Data
            Uri uri = Uri.parse("content://com.example.storage_database.provider/book/" + newId);
            getContentResolver().delete(uri, null, null);
        });
    }

}