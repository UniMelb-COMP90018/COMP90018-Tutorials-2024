package com.example.storage_internalstorage;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storage_internalstorage.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final String FILE_NAME = "myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // button events
        binding.saveButton.setOnClickListener(view -> save(binding.editText.getText().toString()));
        binding.loadButton.setOnClickListener(view -> binding.editText.setText(load()));
        binding.clearButton.setOnClickListener(view -> binding.editText.setText(""));
    }

    private void save(String input)
    {
        FileOutputStream out;
        BufferedWriter writer;

        try {
            out = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(input);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String load()
    {
        FileInputStream in;
        BufferedReader reader;
        StringBuilder myString = new StringBuilder();

        try {
            in = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null){
                myString.append(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myString.toString();
    }
}