package com.example.makeday.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView noteTitleTV, noteBodyTV;
    public ConstraintLayout noteLayout;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTitleTV = itemView.findViewById(R.id.noteTitleTV);
        noteBodyTV = itemView.findViewById(R.id.noteBodyTV);
        noteLayout = itemView.findViewById(R.id.noteLayout);
    }
}
