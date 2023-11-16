package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    Button btn_signup;
    Button btn_go_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(v -> {
            finish();
        });

        btn_go_to_login = findViewById(R.id.btn_go_to_login);
        btn_go_to_login.setOnClickListener(v -> {
            finish();
        });
    }
}