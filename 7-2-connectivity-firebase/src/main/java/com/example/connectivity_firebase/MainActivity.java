package com.example.connectivity_firebase;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.example.connectivity_firebase.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialFireBase("Test");

        binding.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String outputText = binding.messageContent.getText().toString();
                databaseReference.setValue(outputText);
            }
        });
    }

    private void initialFireBase(String referenceName){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(referenceName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String inputText = snapshot.getValue(String.class);
                // update the field automatically from the firebase database
                binding.messageDisplay.setText(inputText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle a cancellation
            }
        });
    }
}