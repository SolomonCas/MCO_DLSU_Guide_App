package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_register_username_value;
    private EditText et_register_id_no_value;
    private EditText et_register_email_value;
    private EditText et_register_password_value;
    private EditText et_register_confirm_password;
    private Button btn_signup;
    private Button btn_go_to_login; // Added button for going to login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_register_username_value = findViewById(R.id.et_register_username_value);
        et_register_id_no_value = findViewById(R.id.et_register_id_no_value);
        et_register_email_value = findViewById(R.id.et_register_email_value);
        et_register_password_value = findViewById(R.id.et_register_password_value);
        et_register_confirm_password = findViewById(R.id.et_register_confirm_password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_go_to_login = findViewById(R.id.btn_go_to_login); // Initialize the button

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(v -> {

            String username = et_register_username_value.getText().toString();
            String idNumber = et_register_id_no_value.getText().toString();
            String email = et_register_email_value.getText().toString();
            String password = et_register_password_value.getText().toString();
            String confirmPassword = et_register_confirm_password.getText().toString();

            if(checkIfFields() && checkPassword(password, confirmPassword) && checkEmail(email) &&
                    checkID(idNumber)){
                // Use Firebase Authentication to create a new user
                mAuth.createUserWithEmailAndPassword(idNumber + "@dlsu.edu.ph", password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User(
                                        username,
                                        email,
                                        password,
                                        Integer.parseInt(idNumber)
                                );
                                addUserInCollection(user);

                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(RegisterActivity.this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
                            Log.d("REGISTER", e.toString());
                        });
            }

        });

        // Set the onClickListener for the btn_go_to_login button
        btn_go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                finish();
            }
        });
    }

    private void addUserInCollection(User user){
        CollectionReference userRef = MyFirestoreReferences.getUserCollectionReference();
        userRef.add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("Add User", e.toString());
                });
    }
    private boolean checkIfFields(){
        if (et_register_username_value.getText().toString().equals("") ||
                et_register_id_no_value.getText().toString().equals("") ||
                et_register_email_value.getText().toString().equals("") ||
                et_register_password_value.getText().toString().equals("") ||
                et_register_confirm_password.getText().toString().equals("")){
            Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password, String confirmPassword){
        if(password.length() != 6){
            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkEmail(String email){
        if(!email.contains("@dlsu.edu.ph")){
            Toast.makeText(RegisterActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkID (String idNo){
        if(idNo.length() != 8){
            Toast.makeText(RegisterActivity.this, "Invalid DLSU ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        int id = 0;
        int num = 8;
        for (int i = 0; i < idNo.length(); i++){
            id += (num * Character.getNumericValue(idNo.charAt(i)));
            num--;
        }

        Log.d("ID RESULT", String.valueOf(id % 11));

        if(id % 11 != 0){
            Toast.makeText(RegisterActivity.this, "Invalid DLSU ID", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}