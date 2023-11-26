package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import java.util.ArrayList;

public class Directory {

    private String directoryID;
    private String directoryName;
    private String location;
    private Integer imageUri;
    private ArrayList<Review> reviews;
    private String tag;


    public Directory(String directoryID, String directoryName, String location, Integer image, String tag){
        this.directoryID = directoryID;
        this.directoryName = directoryName;
        this.location = location;
        this.imageUri = image;
        this.tag = tag;
        this.reviews = new ArrayList<>();
    }

    public Directory(String directoryName, String location, Integer image, String tag){
        this("0", directoryName, location, image, tag);
    }

    public String getId(){
        return this.directoryID;
    }
    public void setId(String directoryID){
        this.directoryID = directoryID;
    }
    public String getDirectoryName(){
        return this.directoryName;
    }
    public void setDirectoryName(String directoryName){
        this.directoryName = directoryName;
    }
    public String getLocation(){
        return this.location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public Integer getImage() {
        return this.imageUri;
    }
    public void setImage(Integer image){
        this.imageUri = image;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    public ArrayList<Review> getReviews(){
        return reviews;
    }
    public void setReviews(ArrayList<Review> reviews){
        this.reviews = reviews;
    }

    public void addReview(Review review){
        this.reviews.add(review);
    }
    public void removeReview(Review review){
        this.reviews.remove(review);
    }

}
