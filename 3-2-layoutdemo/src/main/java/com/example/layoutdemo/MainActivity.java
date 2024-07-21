package com.example.layoutdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.layoutdemo.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        // Setting for Fragments
        Fragment linear_layout = LayoutDemoFragment.newInstance(LayoutDemoFragment.LINEAR_DEMO);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, linear_layout)
                .addToBackStack(null)
                .commit();


        // Setting for Navigation Bar
        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){

                int id = item.getItemId();

                // To show Linear layout demonstration
                if (id == R.id.navigation_linear) {
                    Fragment linear_layout = LayoutDemoFragment.newInstance(LayoutDemoFragment.LINEAR_DEMO);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, linear_layout)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                // To show Relative layout demonstration
                else if (id == R.id.navigation_relative) {
                    Fragment relative_layout = LayoutDemoFragment.newInstance(LayoutDemoFragment.RELATIVE_DEMO);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, relative_layout)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                // To show List view demonstration
                else if (id == R.id.navigation_list) {
                    Fragment list_layout = LayoutDemoFragment.newInstance(LayoutDemoFragment.LIST_DEMO);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, list_layout)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                // To show Recycler demonstration
                else if (id == R.id.navigation_recycler) {
                    Fragment recycler_layout = LayoutDemoFragment.newInstance(LayoutDemoFragment.RECYCLER_DEMO);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, recycler_layout)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
}
