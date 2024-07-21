package com.example.layoutdemo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.layoutdemo.databinding.FragmentLayoutDemoListBinding;
import com.example.layoutdemo.databinding.FragmentLayoutDemoRecyclerBinding;

import java.util.ArrayList;

public class LayoutDemoFragment extends Fragment {

    static int LINEAR_DEMO = R.layout.fragment_layout_demo_linear;
    static int RELATIVE_DEMO = R.layout.fragment_layout_demo_relative;
    static int LIST_DEMO = R.layout.fragment_layout_demo_list;
    static int RECYCLER_DEMO = R.layout.fragment_layout_demo_recycler;
    static String LAYOUT_TYPE = "type";

    private int layout = R.layout.fragment_layout_demo_linear;

    private FragmentLayoutDemoListBinding listBinding;
    private FragmentLayoutDemoRecyclerBinding recyclerBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (this.getArguments() != null)
            this.layout = getArguments().getInt(LAYOUT_TYPE);

        if (this.layout == R.layout.fragment_layout_demo_list) {
            listBinding = FragmentLayoutDemoListBinding.inflate(inflater, container, false);

            ListDemoAdapter adapter = new ListDemoAdapter(getActivity(), R.layout.list_example, getFruits());
            listBinding.demoListView.setAdapter(adapter);

            // To set onItemClickListener - method 1
            listBinding.demoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Fruit fruit = (Fruit)adapterView.getItemAtPosition(i);
                    Toast.makeText(getContext(), fruit.getFruitName(), Toast.LENGTH_SHORT).show();
                }
            });

            return listBinding.getRoot();
        }
        else if (this.layout == R.layout.fragment_layout_demo_recycler) {
            recyclerBinding = FragmentLayoutDemoRecyclerBinding.inflate(inflater, container, false);

            RecyclerDemoAdapter adapter = new RecyclerDemoAdapter(getFruits(), R.layout.recycler_example);
            // To lay out children in a staggered grid formation
            recyclerBinding.demoRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerBinding.demoRecycler.setAdapter(adapter);

            return recyclerBinding.getRoot();
        }
        else {
            View view = inflater.inflate(layout, container, false);
            return view;
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        listBinding = null;
        recyclerBinding = null;
    }

    // Recommended method to generate new LayoutDemoFragment
    // Instead of calling new LayoutDemoFragment() directly
    static Fragment newInstance(int layout) {
        Fragment fragment = new LayoutDemoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_TYPE, layout);
        fragment.setArguments(bundle);

        return fragment;
    }

    // To generate an array of fruit example for ListView and RecyclerView Demonstration
    private ArrayList<Fruit> getFruits() {
        ArrayList<Fruit> fruits = new ArrayList<>();
        fruits.add(new Fruit(R.drawable.apple, "Apple"));
        fruits.add(new Fruit(R.drawable.bananas, "Bananas"));
        fruits.add(new Fruit(R.drawable.cherry, "Cherry"));
        fruits.add(new Fruit(R.drawable.grapes, "Grapes"));
        fruits.add(new Fruit(R.drawable.lemon, "Lemon"));
        fruits.add(new Fruit(R.drawable.orange, "Orange"));
        fruits.add(new Fruit(R.drawable.melon, "Melon"));
        fruits.add(new Fruit(R.drawable.peach, "Peach"));
        fruits.add(new Fruit(R.drawable.pear, "Pear"));
        fruits.add(new Fruit(R.drawable.pomegranate, "Pomegranate"));
        fruits.add(new Fruit(R.drawable.strawberry, "Strawberry"));
        fruits.add(new Fruit(R.drawable.watermelon, "Watermelon"));

        return fruits;
    }

}
