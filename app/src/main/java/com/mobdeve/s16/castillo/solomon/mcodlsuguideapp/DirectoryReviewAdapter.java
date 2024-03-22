package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DirectoryReviewAdapter extends RecyclerView.Adapter<DirectoryReviewAdapter.ViewHolder>{

    private ArrayList<DocumentReference> reviews;

    public DirectoryReviewAdapter(ArrayList<DocumentReference> reviews) {
        this.reviews = reviews;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directory_review_layout, parent, false);
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
        public void bindData(Review review){
            this.tv_directory_review_text.setText(review.getReviewContent());
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

            this.tv_directory_review_user.setText(user.getUsername());
        }
    }

}

