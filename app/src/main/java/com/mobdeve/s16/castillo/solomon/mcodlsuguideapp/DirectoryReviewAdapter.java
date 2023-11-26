package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DirectoryReviewAdapter extends RecyclerView.Adapter<DirectoryReviewAdapter.ViewHolder>{

    private ArrayList<Review> reviews;
    private User[] users;


    public DirectoryReviewAdapter(ArrayList<Review> reviews, User[] users) {
        this.reviews = reviews;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directory_review_layout, parent, false);
        return new ViewHolder(view);
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
        return Math.min(reviews.size(), 2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_directory_review_user;
        private TextView tv_directory_review_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_directory_review_user = itemView.findViewById(R.id.tv_directory_review_user);
            this.tv_directory_review_text = itemView.findViewById(R.id.tv_directory_review_text);
        }
        public void bindData(Review review, User user){
            this.tv_directory_review_user.setText(user.getUsername());
            this.tv_directory_review_text.setText(review.getReviewContent());
        }
    }

}

