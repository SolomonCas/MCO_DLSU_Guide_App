package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import static com.mobdeve.s16.castillo.solomon.mcodlsuguideapp.DataHelper.initializeUserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DirectoryActivity extends AppCompatActivity {

    private ImageView ivDirectoryImg;
    private TextView tvDirectoryName, tvDirectoryTag, tvDirectoryLocation, tv_average_rate_val;
    private Button btn_more_review;
    private RecyclerView recyclerView;
    private ImageButton ib_back_btn;
    private RatingBar rb_average_rate;

    private ArrayList<Review> reviews;
    private User[] users = initializeUserData();


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
            // Check if the Intent has the necessary extras
            if (intent.hasExtra("image")) {
                int imageResId = intent.getIntExtra("image", R.drawable.default_image);
                ivDirectoryImg.setImageResource(imageResId);
            }

            if (intent.hasExtra("directoryName")) {
                String directoryName = intent.getStringExtra("directoryName");
                tvDirectoryName.setText(directoryName);
            }

            if (intent.hasExtra("tag")) {
                String tag = intent.getStringExtra("tag");
                tvDirectoryTag.setText(tag);
            }

            if (intent.hasExtra("location")) {
                String location = intent.getStringExtra("location");
                tvDirectoryLocation.setText(location);
            }

            if (intent.hasExtra("reviews")) {
                reviews = intent.getParcelableArrayListExtra("reviews");
                DirectoryReviewAdapter directoryReviewAdapter = new DirectoryReviewAdapter(reviews, users);
                recyclerView.setAdapter(directoryReviewAdapter);

                this.btn_more_review.setVisibility(View.VISIBLE);

                float averageRating = getAverageRate(reviews);
                this.rb_average_rate.setRating(averageRating);
                this.tv_average_rate_val.setText(String.format("%.1f", averageRating));
            }

        }

        this.btn_more_review.setOnClickListener(v ->{
            Intent directoryIntent = new Intent(this, ReviewActivity.class);
            if(reviews.size() > 0){
                directoryIntent.putParcelableArrayListExtra("reviews", reviews);
            }
            startActivity(directoryIntent);
        });

        this.ib_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private float getAverageRate(ArrayList<Review> reviews){
        float totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRate();
        }
        return totalRating / reviews.size();
    }
}