package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectoryActivity extends AppCompatActivity {

    private ImageView ivDirectoryImg;
    private TextView tvDirectoryName, tvDirectoryTag, tvDirectoryLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        ivDirectoryImg = findViewById(R.id.iv_directory_img);
        tvDirectoryName = findViewById(R.id.tv_directory_name);
        tvDirectoryTag = findViewById(R.id.tv_directory_tag);
        tvDirectoryLocation = findViewById(R.id.tv_directory_location);

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
        }
    }
}