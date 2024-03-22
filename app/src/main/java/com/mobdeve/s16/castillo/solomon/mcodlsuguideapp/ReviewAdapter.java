package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private ArrayList<DocumentReference> reviews;

        public ReviewAdapter(ArrayList<DocumentReference> reviews){
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentReference reviewRef =
                MyFirestoreReferences.getReviewDocumentReference(this.reviews.get(position).getId());
        Tasks.whenAllSuccess(reviewRef.get())
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> list) {
                        holder.bindData(
                                ((DocumentSnapshot) list.get(0)).toObject(Review.class)
                        );
                    }
                });
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

        public void bindData(Review review){
            Log.d("Bind Data", review.getImageUri());
            this.rb_reviewer_rate.setRating(review.getRate());
            if(review.getImageUri() != null && !review.getImageUri().equals("")){
                MyFirestoreReferences.downloadImageIntoReviewImageView(review, this.iv_review_image);
            }
            this.tv_review_content.setText(review.getReviewContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String formattedDate = dateFormat.format(review.getDate());
            this.tv_review_date.setText(formattedDate);
            DocumentReference userRef =
                    MyFirestoreReferences.getUserDocumentReference(review.getUserRef().getId());
            Tasks.whenAllSuccess(userRef.get())
                    .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            setUsername(
                                    ((DocumentSnapshot) list.get(0)).toObject(User.class)
                            );
                        }
                    });
        }
        private void setUsername(User user){

            this.tv_review_user.setText(user.getUsername());
        }
    }
}
