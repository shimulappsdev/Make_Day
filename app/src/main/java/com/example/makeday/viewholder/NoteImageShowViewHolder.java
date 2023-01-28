package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class NoteImageShowViewHolder extends RecyclerView.ViewHolder {
    public ImageView noteImageView;
    public NoteImageShowViewHolder(@NonNull View itemView) {
        super(itemView);
        noteImageView = itemView.findViewById(R.id.noteImageView);
    }
}
