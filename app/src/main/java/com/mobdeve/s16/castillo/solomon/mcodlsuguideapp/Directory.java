package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;



import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Directory {
    @DocumentId
    private DocumentReference directoryRef;
    private String directoryName;
    private String location;
    private String imageUri;
    private GeoPoint point;
    private ArrayList<DocumentReference> reviewsRef;
    private String tag;

    public Directory(){

    }
    public Directory(String directoryName, String location, GeoPoint point, String image, String tag){
        this.directoryName = directoryName;
        this.location = location;
        this.point = point;
        this.imageUri = image;
        this.tag = tag;
        this.reviewsRef = new ArrayList<>();
    }

    public DocumentReference getDirectoryRef(){
        return this.directoryRef;
    }
    public void setId(DocumentReference directoryRef){
        this.directoryRef = directoryRef;
    }
    public String getDirectoryName(){
        return this.directoryName;
    }
    public void setDirectoryName(String directoryName){
        this.directoryName = directoryName;
    }
    public GeoPoint getPoint(){
        return this.point;
    }
    public void setPoint(GeoPoint point){
        this.point = point;
    }
    public String getLocation(){
        return this.location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getImage() {
        return this.imageUri;
    }
    public void setImage(String image){
        this.imageUri = image;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    public ArrayList<DocumentReference> getReviews(){
        return reviewsRef;
    }
    public void setReviews(ArrayList<DocumentReference> reviewsRef){
        this.reviewsRef = reviewsRef;
    }

    public void addReview(DocumentReference review){
        this.reviewsRef.add(review);
    }
    public void removeReview(DocumentReference review){
        this.reviewsRef.remove(review);
    }


}
