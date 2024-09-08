package com.example.storage.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.storage.sharedpreferences.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final String PREFERENCE_NAME = "data";
    private final String OBJECT_NAME = "name";
    private final String OBJECT_AGE = "age";

    private final String CACHED_FILENAME = "temporaryData";
    private final String DELIMITER = ":::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.saveData.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit();
            editor.putString(OBJECT_NAME, String.valueOf(binding.name.getText()));
            editor.putInt(OBJECT_AGE, Integer.parseInt(String.valueOf(binding.age.getText())));
            editor.apply();
        });

        binding.restoreData.setOnClickListener(view2 -> {
            SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
            binding.name.setText(preferences.getString(OBJECT_NAME, ""));
            binding.age.setText(String.valueOf(preferences.getInt(OBJECT_AGE, 0)));
        });
    }
}