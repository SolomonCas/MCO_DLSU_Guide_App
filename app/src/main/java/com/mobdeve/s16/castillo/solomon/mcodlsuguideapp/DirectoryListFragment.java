package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DirectoryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rv_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize data
        Directory[] directories = DataHelper.initializeData();

        // Initialize and set the adapter
        adapter = new MyAdapter(directories);
        recyclerView.setAdapter(adapter);

        return view;
    }


}