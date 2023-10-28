package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
        });
    }
}