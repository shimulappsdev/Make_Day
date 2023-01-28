package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class LIstModeViewHolder extends RecyclerView.ViewHolder {
    public TextView listTitleTV, listTypeTV, listDateTV;
    public RecyclerView listItemRecyclerView;
    public ConstraintLayout listLayout;
    public ImageView listDeleteBtn;
    public LIstModeViewHolder(@NonNull View itemView) {
        super(itemView);
        listTitleTV = itemView.findViewById(R.id.listTitleTV);
        listTypeTV = itemView.findViewById(R.id.listTypeTV);
        listDateTV = itemView.findViewById(R.id.listDateTV);
        listItemRecyclerView = itemView.findViewById(R.id.listItemRecyclerView);
        listLayout = itemView.findViewById(R.id.listLayout);
        listDeleteBtn = itemView.findViewById(R.id.listDeleteBtn);
    }
}
