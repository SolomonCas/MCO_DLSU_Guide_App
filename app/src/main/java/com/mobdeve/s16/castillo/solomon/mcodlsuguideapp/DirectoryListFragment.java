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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class DirectoryListFragment extends Fragment {
    private SwipeRefreshLayout srl_directory_list_main;
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory_list, container, false);

        this.srl_directory_list_main = view.findViewById(R.id.srl_directory_list_main);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rv_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize and set the adapter
        adapter = new MyAdapter(getContext());
        recyclerView.setAdapter(adapter);

        this.srl_directory_list_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_directory_list_main.setRefreshing(true);
                updateDataAndAdapter();
                srl_directory_list_main.setRefreshing(false);
            }
        });

        return view;
    }
    private void updateDataAndAdapter() {
        MyFirestoreReferences.getDirectoryCollectionReference()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Directory> directories = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                directories.add(document.toObject(Directory.class));
                            }

                            adapter.setData(directories);
                            adapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        // When the user comes back, we perform an update.
        updateDataAndAdapter();
    }

}