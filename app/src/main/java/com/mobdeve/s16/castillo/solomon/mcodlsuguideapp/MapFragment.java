package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory_list, container, false);
        // Initialize your RecyclerView and load data
        return view;
    }
}
