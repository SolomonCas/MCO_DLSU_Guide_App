package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.net.Uri;
import android.widget.ImageView;

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
    private static FirebaseFirestore firebaseFirestoreInstance = null;
    private static StorageReference storageReferenceInstance = null;
    private static CollectionReference directoriesRef = null;
    private static CollectionReference reviewsRef = null;
    private static CollectionReference usersRef = null;
    public final static String
            DIRECTORIES_COLLECTION = "Directory",
            REVIEWS_COLLECTION = "Review",
            USERS_COLLECTION = "User",


            DIRECTORY_NAME_FIELD = "directoryName",
            DIRECTORY_RATE_FIELD = "rate",
            DIRECTORY_TAG_FIELD = "tag",
            DIRECTORY_COORDINATE_FIELD = "coordinate",
            DIRECTORY_LOCATION_FIELD = "location",
            DIRECTORY_REVIEWS_FIELD = "reviewRef",


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
        String path = "directories/" + d.getDirectoryName() + d.getPoint().getLatitude() + d.getPoint().getLongitude() + "-" + Uri.parse(d.getImage()).getLastPathSegment();

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
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
    public static String generateNewImagePath(Directory d, Uri imageUri) {
        return "directories/" + d.getDirectoryName() + d.getPoint().getLatitude() + d.getPoint().getLongitude() + "-" + imageUri.getLastPathSegment();
    }

    public static void downloadImageIntoReviewImageView(Review r, ImageView iv) {
        String path = "reviews/" +  r.getUserRef().getId() + "-" + Uri.parse(r.getImageUri()).getLastPathSegment();

        getStorageReferenceInstance().child(path).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
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
