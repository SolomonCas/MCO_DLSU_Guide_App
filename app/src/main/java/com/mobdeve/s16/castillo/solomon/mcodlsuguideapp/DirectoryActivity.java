package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DirectoryActivity extends AppCompatActivity {

    private ImageView ivDirectoryImg;
    private TextView tvDirectoryName, tvDirectoryTag, tvDirectoryLocation, tv_average_rate_val;
    private Button btn_more_review;
    private RecyclerView recyclerView;
    private ImageButton ib_back_btn;
    private RatingBar rb_average_rate;
    private DirectoryReviewAdapter directoryReviewAdapter;
    private String directoryIDString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        this.ivDirectoryImg = findViewById(R.id.iv_directory_img);
        this.tvDirectoryName = findViewById(R.id.tv_directory_name);
        this.rb_average_rate = findViewById(R.id.rb_average_rate);
        this.tv_average_rate_val = findViewById(R.id.tv_average_rate_val);
        this.tvDirectoryTag = findViewById(R.id.tv_tag_val);
        this.tvDirectoryLocation = findViewById(R.id.tv_directory_location);
        this.btn_more_review = findViewById(R.id.btn_more_review);

        this.recyclerView = findViewById(R.id.rv_directory_review_layout);
        this.ib_back_btn = findViewById(R.id.ib_back_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // Retrieve data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            directoryIDString = intent.getStringExtra(IntentKeys.DIRECTORY_ID_KEY.name());
        }

        this.btn_more_review.setOnClickListener(v ->{
            Intent directoryIntent = new Intent(this, ReviewActivity.class);
            directoryIntent.putExtra(IntentKeys.DIRECTORY_ID_KEY.name(), directoryIDString);
            startActivity(directoryIntent);
        });

        this.ib_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setAverageRate(ArrayList<DocumentReference> reviews){

        MyFirestoreReferences.getReviewCollectionReference()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Review> reviewRefs = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                reviewRefs.add(document.toObject(Review.class));
                            }

                            float totalRating = 0;
                            for(int i = 0; i < reviews.size(); i++){
                                for (Review r : reviewRefs) {
                                    if(r.getReviewID().getId().equals(reviews.get(i).getId())){
                                        totalRating += r.getRate();
                                    }
                                }
                            }

                            totalRating /= reviews.size();
                            rb_average_rate.setRating(totalRating);
                            tv_average_rate_val.setText(String.format("%.1f", totalRating));

                        }
                    }
                });
    }
    private void setViews(Directory directory) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyFirestoreReferences.downloadImageIntoImageView(directory, ivDirectoryImg);

                tvDirectoryName.setText(directory.getDirectoryName());
                tvDirectoryTag.setText(directory.getTag());
                tvDirectoryLocation.setText(directory.getLocation());
                if(directory.getReviewsRef().size() > 0){
                    btn_more_review.setVisibility(View.VISIBLE);
                    directoryReviewAdapter = new DirectoryReviewAdapter(directory.getReviewsRef());
                    recyclerView.setAdapter(directoryReviewAdapter);
                    setAverageRate(directory.getReviewsRef());
                }


            }
        });
    }

    private void updateDataAndAdapter() {
        DocumentReference directoryRef = MyFirestoreReferences.getDirectoryDocumentReference(directoryIDString);
        Tasks.whenAllSuccess(directoryRef.get())
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        setViews(
                                ((DocumentSnapshot) list.get(0)).toObject(Directory.class)
                        );
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