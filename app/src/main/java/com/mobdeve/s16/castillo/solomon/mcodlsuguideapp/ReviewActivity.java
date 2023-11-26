package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import static com.mobdeve.s16.castillo.solomon.mcodlsuguideapp.DataHelper.initializeUserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Review> reviews;
    private MyAdapter adapter;
    private User[] users = initializeUserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        this.recyclerView = findViewById(R.id.rv_review_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra("reviews")){
                reviews = intent.getParcelableArrayListExtra("reviews");
                ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, users);
                recyclerView.setAdapter(reviewAdapter);
            }
        }
    }
}