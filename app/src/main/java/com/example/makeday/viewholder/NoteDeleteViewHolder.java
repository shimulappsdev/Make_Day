package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class NoteDeleteViewHolder extends RecyclerView.ViewHolder {
    public TextView noteTitleTV, noteBodyTV;
    public ImageView noteDeleteBtn;
    public ConstraintLayout deleteNoteLayout;
    public NoteDeleteViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTitleTV = itemView.findViewById(R.id.noteTitleTV);
        noteBodyTV = itemView.findViewById(R.id.noteBodyTV);
        noteDeleteBtn = itemView.findViewById(R.id.noteDeleteBtn);
        deleteNoteLayout = itemView.findViewById(R.id.deleteNoteLayout);
    }
}
