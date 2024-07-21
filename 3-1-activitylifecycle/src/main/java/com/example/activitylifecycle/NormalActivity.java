package com.example.activitylifecycle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An example activity using a standard design
 */
public class NormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
    }
}