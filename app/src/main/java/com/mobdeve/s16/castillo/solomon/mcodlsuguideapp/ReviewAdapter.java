package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private ArrayList<Review> reviews;
    private User[] users;

    public ReviewAdapter(ArrayList<Review> reviews, User[] users){
        this.reviews = reviews;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_layout, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = new User("unkownuser", "", "", 0);
        // Search for the User
        for(User i : users){
            if(i.getUserID().equals(reviews.get(position).getUserID())){
                user = i;
            }
        }
        holder.bindData(reviews.get(position), user);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_review_user;
        private ImageView iv_review_image;
        private RatingBar rb_reviewer_rate;
        private TextView tv_review_content;
        private TextView tv_review_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_review_user = itemView.findViewById(R.id.tv_review_user);
            this.iv_review_image = itemView.findViewById(R.id.iv_review_image);
            this.rb_reviewer_rate = itemView.findViewById(R.id.rb_reviewer_rate);
            this.tv_review_content = itemView.findViewById(R.id.tv_review_content);
            this.tv_review_date = itemView.findViewById(R.id.tv_review_date);
        }

        public void bindData(Review review, User user){
            this.tv_review_user.setText(user.getUsername());
            this.rb_reviewer_rate.setRating(review.getRate());
            if(review.getImageUri() != null){
                this.iv_review_image.setImageResource(review.getImageUri());
            }
            this.tv_review_content.setText(review.getReviewContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String formattedDate = dateFormat.format(review.getDate());
            this.tv_review_date.setText(formattedDate);
        }
    }
}
