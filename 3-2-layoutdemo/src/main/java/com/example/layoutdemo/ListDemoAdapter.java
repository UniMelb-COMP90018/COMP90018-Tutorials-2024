package com.example.layoutdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.layoutdemo.databinding.ListExampleBinding;

import java.util.List;


public class ListDemoAdapter extends ArrayAdapter<Fruit> {
    ListExampleBinding binding;
    private int resourceId;

    public ListDemoAdapter(Context context, int resource, List<Fruit> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = ListExampleBinding.inflate(LayoutInflater.from(parent.getContext()));
        Fruit fruit = getItem(position);

        binding.listExampleImage.setImageResource(fruit.getFruitImage());
        binding.listExampleText.setText(fruit.getFruitName());

        return binding.getRoot();
    }
}