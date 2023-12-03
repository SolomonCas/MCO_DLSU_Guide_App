package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.StorageReference;
import com.mapbox.geojson.Point;
import com.squareup.picasso.Picasso;


public class AddMarkerActivity extends AppCompatActivity {

    private ImageView iv_am_image;
    private ProgressDialog progressDialog = null;
    private EditText et_am_name_val;
    private EditText et_am_location_value;
    private Spinner spn_tag_value;
    private ImageButton ib_am_back_btn;
    private Button btn_am_add, btn_am_cancel, btn_select_image;
    private Point point;

    private Uri imageUri;
    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                imageUri = result.getData().getData();
                                Picasso.get().load(imageUri).into(iv_am_image);
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
        setContentView(R.layout.activity_add_marker);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("point")){
            this.point = (Point) intent.getSerializableExtra("point");
        }

        this.ib_am_back_btn = findViewById(R.id.ib_am_back_btn);
        this.iv_am_image = findViewById(R.id.iv_am_image);
        this.btn_select_image = findViewById(R.id.btn_select_image);
        this.et_am_name_val = findViewById(R.id.et_am_name_value);
        this.et_am_location_value = findViewById(R.id.et_am_location_value);
        this.spn_tag_value = findViewById(R.id.spn_tag_value);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.amenity_array,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_tag_value.setAdapter(adapter);
        this.btn_am_add = findViewById(R.id.btn_am_add);
        this.btn_am_cancel = findViewById(R.id.btn_am_cancel);

        this.btn_am_cancel.setOnClickListener(v -> {
            finish();
        });
        this.btn_am_add.setOnClickListener(v -> {
            if (!checkIfFields()) {
                progressDialog = new ProgressDialog(AddMarkerActivity.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                Directory directory = new Directory(
                        this.et_am_name_val.getText().toString(),
                        this.et_am_location_value.getText().toString(),
                        new GeoPoint(point.latitude(), point.longitude()),
                        this.imageUri.toString(), // Initially, leave the imageUri blank
                        this.spn_tag_value.getSelectedItem().toString()
                );

                CollectionReference directoryRef = MyFirestoreReferences.getDirectoryCollectionReference();


                directoryRef.add(directory)
                        .addOnSuccessListener(documentReference -> {
                            String imagePath = MyFirestoreReferences.generateNewImagePath(documentReference, imageUri);
                            StorageReference imageRef = MyFirestoreReferences.getStorageReferenceInstance()
                                    .child(imagePath);

                            imageRef.putFile(imageUri)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        // Update the Directory document in Firestore with the image URL
                                        progressDialog.dismiss();
                                        Toast.makeText(AddMarkerActivity.this, "Directory added successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddMarkerActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(AddMarkerActivity.this, "Failed to add directory", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
            }
        });
        this.ib_am_back_btn.setOnClickListener(v -> {
            finish();
        });
        this.btn_select_image.setOnClickListener(v ->{
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_OPEN_DOCUMENT);
            myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
        });
    }



    private boolean checkIfFields(){
        return (this.imageUri == null ||
                this.et_am_location_value.getText().toString().matches("") ||
                this.et_am_name_val.getText().toString().matches(""));
    }
}