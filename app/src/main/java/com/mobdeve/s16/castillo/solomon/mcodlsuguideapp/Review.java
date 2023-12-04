package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;



import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Review {
    @DocumentId
    private DocumentReference reviewRef;
    private String reviewContent;
    private String imageUri;
    private float rate;
    private DocumentReference userRef;
    private @ServerTimestamp Date date;

    public Review(){

    }

    public Review(String reviewContent, String imageUri, float rate, DocumentReference user){
        this.reviewContent = reviewContent;
        this.imageUri = imageUri;
        this.rate = rate;
        this.userRef = user;
    }

    public DocumentReference getReviewRef() {
        return this.reviewRef;
    }
    public void setReviewID(DocumentReference reviewID) {
        this.reviewRef = reviewID;
    }
    public String getReviewContent() {
        return this.reviewContent;
    }
    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
    public String getImageUri() {
        return this.imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    public float getRate() {
        return this.rate;
    }
    public void setRate(float rate) {
        this.rate = rate;
    }
    public DocumentReference getUserRef() {
        return this.userRef;
    }
    public void setUser(DocumentReference user) {
        this.userRef = user;
    }
    public Date getDate(){
        return this.date;
    }
    public void setDate(Date date){
        this.date = date;
    }

    @Override
    public String toString() {
        return "Review content: " + reviewContent + ", User: " + userRef;
    }

}
