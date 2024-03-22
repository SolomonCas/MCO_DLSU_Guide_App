package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText et_login_id_no_value;
    private EditText et_login_password_value;
    private Button btn_login;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_id_no_value = findViewById(R.id.et_login_id_no_value);
        et_login_password_value = findViewById(R.id.et_login_password_value);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(v -> {
            String idNumber = et_login_id_no_value.getText().toString().trim();
            String password = et_login_password_value.getText().toString().trim();

            if (!checkIfFields(idNumber, password)){
                mAuth.signInWithEmailAndPassword(idNumber + "@dlsu.edu.ph", password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getUser(idNumber);
                                } else {
                                    // Handle unsuccessful login
                                    Toast.makeText(LoginActivity.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
            }
        });

        btn_register.setOnClickListener(v -> {
            // Navigate to RegisterActivity
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
    }

    private boolean checkIfFields(String idNo, String password){
        return (idNo.equals("") || password.equals(""));
    }

    private void getUser(String idNumber){

        CollectionReference userRef = MyFirestoreReferences.getUserCollectionReference();
        userRef.whereEqualTo("idNo", Integer.parseInt(idNumber))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Get the first document matching the idNumber
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Get the DocumentReference for this document
                        DocumentReference userDocumentReference = documentSnapshot.getReference();

                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.putExtra(IntentKeys.USER_ID_KEY.name(), userDocumentReference.getId());
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(this, "User with ID Number " + idNumber + " not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("LoginActivity", "Failed to fetch user data: " + e.getMessage());
                });


    }
}