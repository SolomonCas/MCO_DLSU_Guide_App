package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private Directory[] directories;
    public MyAdapter(Directory[] data) {
        this.directories = data;
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
        holder.bindData(directories[position]);

    }

    @Override
    public int getItemCount() {
        return directories.length;
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
            this.iv_dl_photo.setImageResource(directory.getImage());
            this.tv_dl_tags.setText("Tag: " + directory.getTags());
        }
    }
}


