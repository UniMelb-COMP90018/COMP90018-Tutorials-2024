package com.example.layoutdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layoutdemo.databinding.ActivityMainBinding;

import java.util.List;


import com.example.layoutdemo.databinding.ListExampleBinding;
import com.example.layoutdemo.databinding.RecyclerExampleBinding;

//RecyclerView Adapter for Fruits
public class RecyclerDemoAdapter extends RecyclerView.Adapter<RecyclerDemoAdapter.ViewHolder> {

    RecyclerExampleBinding binding;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
        }
    }

    // an array of fruits need to display at recyclerView
    private List<Fruit> fruits;
    // the resource id of item layout
    private int resourceId;

    // to initialize adapter with fruits array, and the resource id of layout
    public RecyclerDemoAdapter(List<Fruit> fruits, int resourceId) {
        this.fruits = fruits;
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    //  Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an item.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerExampleBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @Override
    // to bind the resources to viewHolder, including fruit image resource id and fruit name
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        binding.recyclerExampleImage.setImageResource(fruits.get(position).getFruitImage());
        binding.recyclerExampleText.setText(fruits.get(position).getFruitName());

        // To show how to add click listener to a item in recyclerView
        // Set onClickListener for the fruit array;
        // When clicking a item from the list, a new Toast shows the name of the clicked fruit;
        binding.recyclerExampleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),fruits.get(holder.getAdapterPosition()).getFruitName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    // to get the size of the fruits array
    public int getItemCount() {
        return fruits.size();
    }

}