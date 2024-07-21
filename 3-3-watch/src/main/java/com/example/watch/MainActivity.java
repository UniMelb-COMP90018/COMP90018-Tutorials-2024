package com.example.watch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.watch.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.statusLabel.setText(R.string.question_label);

        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.statusLabel.setText((R.string.agree_label));
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.statusLabel.setText((R.string.disagree_label));
            }
        });
    }
}
