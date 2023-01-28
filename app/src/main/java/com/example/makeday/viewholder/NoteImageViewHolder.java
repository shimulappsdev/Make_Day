package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class NoteImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView noteImageView, removeImage;
    public NoteImageViewHolder(@NonNull View itemView) {
        super(itemView);
        noteImageView = itemView.findViewById(R.id.noteImageView);
        removeImage = itemView.findViewById(R.id.removeImage);
    }
}
