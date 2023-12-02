package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private ArrayList<Directory> directories;
    private Context context;
    public MyAdapter(Context context) {
        this.directories = new ArrayList<>();
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(directories.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the clicked directory
                Directory clickedDirectory = directories.get(position);

                // Create an intent to open DirectoryActivity
                Intent intent = new Intent(context, DirectoryActivity.class);

                intent.putExtra(IntentKeys.DIRECTORY_ID_KEY.name(), clickedDirectory.getDirectoryRef().getId());

                // Start DirectoryActivity
                context.startActivity(intent);
            }
        });

    }

    public void setData(ArrayList<Directory> directories) {
        this.directories = directories;
    }

    @Override
    public int getItemCount() {
        return directories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_dl_name;
        private TextView tv_dl_location;
        private ImageView iv_dl_photo;
        private TextView tv_dl_tags;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_dl_name = itemView.findViewById(R.id.tv_dl_name);
            this.tv_dl_location = itemView.findViewById(R.id.tv_dl_location);
            this.iv_dl_photo = itemView.findViewById(R.id.iv_dl_photo);
            this.tv_dl_tags = itemView.findViewById(R.id.tv_dl_tags);
        }

        public void bindData(Directory directory){
            this.tv_dl_name.setText(directory.getDirectoryName());
            this.tv_dl_location.setText("Location: " + directory.getLocation());
//            Picasso.get().load(directory.getImage()).into(this.iv_dl_photo);
            MyFirestoreReferences.downloadImageIntoImageView(directory, this.iv_dl_photo);
            this.tv_dl_tags.setText("Tag: " + directory.getTag());
        }
    }
}


