package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class RateAndReviewActivity extends AppCompatActivity {

    private RatingBar rbUserRating;
    private EditText etUserReview;
    private ImageButton ib_rate_and_review_back_btn;
    private Button btn_rate_and_review_submit, btn_rate_review_select_image;
    private ImageView iv_rate_review_image;
    private Uri imageUri;
    private ProgressDialog progressDialog = null;
    String userRef;
    String directoryRef;

    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                imageUri = result.getData().getData();
                                Picasso.get().load(imageUri).into(iv_rate_review_image);
                            }
                        } catch(Exception exception){
                            Log.d("TAG",""+exception.getLocalizedMessage());
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);

        Intent intent = getIntent();
        if (intent != null) {
            this.userRef = intent.getStringExtra(IntentKeys.USER_ID_KEY.name());
            Log.d("RateAndReviewActivity", userRef);
            this.directoryRef = intent.getStringExtra(IntentKeys.DIRECTORY_ID_KEY.name());
        }

        rbUserRating = findViewById(R.id.rb_user_rating);
        etUserReview = findViewById(R.id.et_user_review);
        ib_rate_and_review_back_btn = findViewById(R.id.ib_rate_and_review_back_btn);
        btn_rate_and_review_submit = findViewById(R.id.btn_rate_and_review_submit);
        btn_rate_review_select_image = findViewById(R.id.btn_rate_review_select_image);
        iv_rate_review_image = findViewById(R.id.iv_rate_review_image);

        btn_rate_and_review_submit.setOnClickListener(v ->{
            if(!checkIfFields()){
                progressDialog = new ProgressDialog(RateAndReviewActivity.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                DocumentReference userReference = MyFirestoreReferences.getUserDocumentReference(this.userRef);
                Review review = null;
                if(imageUri == null){
                    review = new Review(
                            etUserReview.getText().toString(),
                            "",
                            rbUserRating.getRating(),
                            userReference

                    );
                }
                else {
                    review = new Review(
                            etUserReview.getText().toString(),
                            imageUri.toString(),
                            rbUserRating.getRating(),
                            userReference

                    );
                }


                CollectionReference reviewRef = MyFirestoreReferences.getReviewCollectionReference();


                reviewRef.add(review)
                        .addOnSuccessListener(documentReference -> {
                            if(imageUri != null){
                                String imagePath = MyFirestoreReferences.generateNewReviewImagePath(documentReference, imageUri);
                                StorageReference imageRef = MyFirestoreReferences.getStorageReferenceInstance()
                                        .child(imagePath);

                                imageRef.putFile(imageUri)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            // Update the Directory document in Firestore with the image URL
                                            progressDialog.dismiss();
                                            addToDirectory(documentReference);
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                        });
                            }
                            else{
                                addToDirectory(documentReference);
                            }
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Failed to add review", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
            }
        });

        ib_rate_and_review_back_btn.setOnClickListener(v->{
            finish();
        });

        btn_rate_review_select_image.setOnClickListener(v ->{
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_OPEN_DOCUMENT);
            myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
        });

    }

    private void addToDirectory(DocumentReference reviewRef){
        DocumentReference directoryReference = MyFirestoreReferences.getDirectoryDocumentReference(this.directoryRef);
        directoryReference.update("reviewsRef", FieldValue.arrayUnion(reviewRef))
                .addOnSuccessListener(aVoid -> {
                    // Successfully added reviewRef to the 'reviewsRef' array in the directory document
                    Toast.makeText(this, "Review added to Directory", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to add reviewRef to the 'reviewsRef' array in the directory document
                    Log.e("RateAndReviewActivity", "Failed to add review to Directory: " + e.getMessage());
                });
    }

    private boolean checkIfFields(){
        return (etUserReview.equals("") || rbUserRating.getRating() == 0.0);
    }

}
