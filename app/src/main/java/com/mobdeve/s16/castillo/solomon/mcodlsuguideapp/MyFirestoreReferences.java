package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class MyFirestoreReferences {
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore firebaseFirestoreInstance = null;
    private static StorageReference storageReferenceInstance = null;
    private static CollectionReference directoriesRef = null;
    private static CollectionReference reviewsRef = null;
    private static CollectionReference usersRef = null;
    public final static String
            DIRECTORIES_COLLECTION = "Directory",
            REVIEWS_COLLECTION = "Review",
            USERS_COLLECTION = "User",


            REVIEW_USERNAME_FIELD = "userRef",
            REVIEW_CONTENT_FIELD = "reviewContent",
            REVIEW_IMAGE_FIELD = "imageURI",
            REVIEW_RATE_FIELD = "rate",


            USER_NAME_FIELD = "username",
            USER_ID_NUMBER_FIELD = "idNo",
            USER_EMAIL_FIELD = "email",
            USER_PASSWORD_FIELD = "password",


            TIMESTAMP_FIELD = "timestamp";

    public static FirebaseFirestore getFirestoreInstance() {
        if(firebaseFirestoreInstance == null) {
            firebaseFirestoreInstance = FirebaseFirestore.getInstance();
        }
        return firebaseFirestoreInstance;
    }

    public static StorageReference getStorageReferenceInstance() {
        if (storageReferenceInstance == null) {
            storageReferenceInstance = FirebaseStorage.getInstance().getReference();
        }
        return storageReferenceInstance;
    }

    public static CollectionReference getDirectoryCollectionReference() {
        if(directoriesRef == null) {
            directoriesRef = getFirestoreInstance().collection(DIRECTORIES_COLLECTION);
        }
        return directoriesRef;
    }

    public static CollectionReference getReviewCollectionReference() {
        if(reviewsRef == null) {
            reviewsRef = getFirestoreInstance().collection(REVIEWS_COLLECTION);
        }
        return reviewsRef;
    }

    public static CollectionReference getUserCollectionReference() {
        if(usersRef == null) {
            usersRef = getFirestoreInstance().collection(USERS_COLLECTION);
        }
        return usersRef;
    }

    public static DocumentReference getDirectoryDocumentReference(String stringRef) {
        return getDirectoryCollectionReference().document(stringRef);
    }

    public static DocumentReference getReviewDocumentReference(String stringRef) {
        return getReviewCollectionReference().document(stringRef);
    }

    public static DocumentReference getUserDocumentReference(String stringRef) {
        return getUserCollectionReference().document(stringRef);
    }

    public static void downloadImageIntoImageView(Directory d, ImageView iv) {
        String path = "directories/" + d.getDirectoryRef().getId() + "-" + Uri.parse(d.getImageUri()).getLastPathSegment();

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(task.getResult())
                                .error(R.drawable.ic_error_foreground)
                                .placeholder(circularProgressDrawable)
                                .into(iv);
                    }
                });
    }
    public static String generateNewImagePath(DocumentReference d, Uri imageUri) {
        return "directories/" + d.getId() + "-" + imageUri.getLastPathSegment();
    }

    public static void downloadImageIntoReviewImageView(Review r, ImageView iv) {
        String path = "reviews/" +  r.getUserRef().getId() + "-" + Uri.parse(r.getImageUri()).getLastPathSegment();

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
                        circularProgressDrawable.setCenterRadius(30);
                        Picasso.get()
                                .load(task.getResult())
                                .error(R.drawable.ic_error_foreground)
                                .placeholder(circularProgressDrawable)
                                .into(iv);
                    }
                });
    }
    public static String generateNewReviewImagePath(DocumentReference userRef, Uri imageUri) {
        return "reviews/" + userRef.getId() + "-" + imageUri.getLastPathSegment();
    }
}
