package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String directoryIDString;
    private Directory directory;
    private ImageButton ib_reviews_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        this.ib_reviews_back_btn = findViewById(R.id.ib_reviews_back_btn);
        this.recyclerView = findViewById(R.id.rv_review_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(IntentKeys.DIRECTORY_ID_KEY.name())){
            Log.d("ReviewActivity", "intent");
            directoryIDString = intent.getStringExtra(IntentKeys.DIRECTORY_ID_KEY.name());
            Log.d("ReviewActivity", directoryIDString);
        }

        this.ib_reviews_back_btn.setOnClickListener(v -> {
            finish();
        });
    }
    private void updateDataAndAdapter() {
        DocumentReference directoryRef = MyFirestoreReferences.getDirectoryDocumentReference(directoryIDString);
        Tasks.whenAllSuccess(directoryRef.get())
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        directory = ((DocumentSnapshot) list.get(0)).toObject(Directory.class);
                        ReviewAdapter reviewAdapter = new ReviewAdapter(directory.getReviews());
                        recyclerView.setAdapter(reviewAdapter);

                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();

        // When the user comes back, we perform an update.
        updateDataAndAdapter();
    }
}