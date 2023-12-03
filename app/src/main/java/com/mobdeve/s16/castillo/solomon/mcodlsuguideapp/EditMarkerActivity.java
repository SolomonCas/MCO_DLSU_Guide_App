package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.mapbox.geojson.Point;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditMarkerActivity extends AppCompatActivity {

    private ImageView iv_em_image;
    private ProgressDialog progressDialog = null;
    private EditText et_em_name_val, et_em_location_value;
    private Spinner spn_em_tag_value;
    private ImageButton ib_em_back_btn, ib_em_delete;
    private Button btn_em_edit, btn_em_cancel, btn_em_select_image;
    private Point point;
    private DocumentReference dRef;
    private String directoryRef;
    private StorageReference oldImageRef;

    private Uri imageUri;
    private Uri newImageUri;
    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                newImageUri = result.getData().getData();
                                Picasso.get().load(newImageUri).into(iv_em_image);
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
        setContentView(R.layout.activity_edit_marker);

        this.ib_em_back_btn = findViewById(R.id.ib_em_back_btn);
        this.iv_em_image = findViewById(R.id.iv_em_image);
        this.btn_em_select_image = findViewById(R.id.btn_em_select_image);
        this.et_em_name_val = findViewById(R.id.et_em_name_value);
        this.et_em_location_value = findViewById(R.id.et_em_location_value);
        this.spn_em_tag_value = findViewById(R.id.spn_em_tag_value);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.amenity_array,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_em_tag_value.setAdapter(adapter);
        this.btn_em_edit = findViewById(R.id.btn_em_edit);
        this.btn_em_cancel = findViewById(R.id.btn_em_cancel);
        this.ib_em_delete = findViewById(R.id.ib_em_delete);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("point") && intent.hasExtra("directoryRef")){
            this.point = (Point) intent.getSerializableExtra("point");
            this.directoryRef = intent.getStringExtra("directoryRef");


            DocumentReference dirRef = MyFirestoreReferences.getDirectoryDocumentReference(this.directoryRef);
            Tasks.whenAllSuccess(dirRef.get())
                    .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            setData(
                                    ((DocumentSnapshot) list.get(0)).toObject(Directory.class)
                            );
                        }
                    });
        }

        this.btn_em_cancel.setOnClickListener(v -> {
            finish();
        });
        this.btn_em_edit.setOnClickListener(v -> {
            if (!checkIfFields()) {
                progressDialog = new ProgressDialog(EditMarkerActivity.this);
                progressDialog.setTitle("Updating");
                progressDialog.show();

                if(newImageUri != null){
                    deleteOldImageAndAddNewImage(
                            new Directory(
                                    this.et_em_name_val.getText().toString(),
                                    this.et_em_location_value.getText().toString(),
                                    new GeoPoint(point.latitude(), point.longitude()),
                                    this.newImageUri.toString(),
                                    this.spn_em_tag_value.getSelectedItem().toString())
                    );
                }

                updateDirectory(
                        new Directory(
                                this.et_em_name_val.getText().toString(),
                                this.et_em_location_value.getText().toString(),
                                new GeoPoint(this.point.latitude(), this.point.longitude()),
                                this.imageUri.toString(),
                                this.spn_em_tag_value.getSelectedItem().toString()
                        )
                );
            } else {
                Toast.makeText(EditMarkerActivity.this, "Fill All Fields", Toast.LENGTH_LONG).show();
            }
        });

        this.ib_em_back_btn.setOnClickListener(v -> {
            finish();
        });
        this.btn_em_select_image.setOnClickListener(v ->{
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_OPEN_DOCUMENT);
            myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
        });

        this.ib_em_delete.setOnClickListener(v ->{
            progressDialog = new ProgressDialog(EditMarkerActivity.this);
            progressDialog.setTitle("Updating");
            progressDialog.show();
            deleteDirectoryAndReviews();
        });
    }

    private void deleteDirectoryAndReviews(){
        this.dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Directory d = documentSnapshot.toObject(Directory.class);
                Log.d("deleteDirectoryAndReviews", String.valueOf(d.getReviewsRef().size()));
                if(d.getReviewsRef() != null){
                    ArrayList<DocumentReference> reviews = d.getReviewsRef();
                    FirebaseFirestore db = MyFirestoreReferences.getFirestoreInstance();
                    WriteBatch batch = db.batch();

                    // Delete each review document
                    for (DocumentReference docRef : reviews) {
                        batch.delete(docRef);
                    }

                    batch.commit()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // All reviews deleted successfully
                                    deleteDirectory(); // Proceed to delete the directory itself
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditMarkerActivity.this, "Failed to delete reviews", Toast.LENGTH_SHORT).show();
                                    Log.e("EditMarkerActivity", "Error deleting reviews: " + e.getMessage());
                                }
                            });

                }
                else{
                    deleteDirectory();
                }

            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
           Toast.makeText(EditMarkerActivity.this, "Delete Failed", Toast.LENGTH_LONG).show();
        });
    }

    private void deleteDirectory() {
        this.dRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Directory deleted successfully
                        progressDialog.dismiss();
                        Toast.makeText(EditMarkerActivity.this, "Directory and its reviews deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditMarkerActivity.this, "Failed to delete directory", Toast.LENGTH_SHORT).show();
                        Log.e("EditMarkerActivity", "Error deleting directory: " + e.getMessage());
                    }
                });
    }

    private void deleteOldImageAndAddNewImage(Directory d){
        oldImageRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("EditMarkerActivity", "Old image deleted successfully");
                    StorageReference newImageRef = MyFirestoreReferences.getStorageReferenceInstance()
                            .child(MyFirestoreReferences.generateNewImagePath(dRef, newImageUri));

                    newImageRef.putFile(newImageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                updateDirectory(d);
                            })
                            .addOnFailureListener(e ->{
                                progressDialog.dismiss();
                                Toast.makeText(EditMarkerActivity.this, "Failed to upload new image", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e ->{
                    Log.e("EditMarkerActivity", "Failed to delete old image: " + e.getMessage());
                });
    }

    private void setData(Directory d){
        this.et_em_location_value.setText(d.getLocation());
        this.et_em_name_val.setText(d.getDirectoryName());

        MyFirestoreReferences.downloadImageIntoImageView(d, this.iv_em_image);
        this.imageUri = Uri.parse(d.getImageUri());

        String oldImagePath = MyFirestoreReferences.generateNewImagePath(d.getDirectoryRef(), imageUri);
        this.oldImageRef = MyFirestoreReferences.getStorageReferenceInstance().child(oldImagePath);
        this.dRef = d.getDirectoryRef();
    }

    private void updateDirectory(Directory directory) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("directoryName", directory.getDirectoryName());
        updatedData.put("location", directory.getLocation());
        updatedData.put("image", directory.getImageUri());
        updatedData.put("tag", directory.getTag());

        GeoPoint updateGeopoint = directory.getPoint();
        updatedData.put("point", updateGeopoint);


        this.dRef.update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditMarkerActivity.this, "Directory updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditMarkerActivity.this, "Failed to update directory", Toast.LENGTH_SHORT).show();
                });
    }



    private boolean checkIfFields(){
        return (this.imageUri == null ||
                this.et_em_location_value.getText().toString().matches("") ||
                this.et_em_name_val.getText().toString().matches(""));
    }
}