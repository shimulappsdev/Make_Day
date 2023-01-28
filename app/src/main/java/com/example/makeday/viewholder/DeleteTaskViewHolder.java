package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class DeleteTaskViewHolder extends RecyclerView.ViewHolder {

    public TextView taskTitleTV, categoryTV, priorityTV, locationTV, dateTV, startTimeTV, endTimeTV, leftTime, statusTV;
    public ImageView deleteBtn;
    public ConstraintLayout deleteLayout;

    public DeleteTaskViewHolder(@NonNull View itemView) {
        super(itemView);
        taskTitleTV = itemView.findViewById(R.id.taskTitleTV);
        categoryTV = itemView.findViewById(R.id.categoryTV);
        priorityTV = itemView.findViewById(R.id.priorityTV);
        locationTV = itemView.findViewById(R.id.locationTV);
        dateTV = itemView.findViewById(R.id.dateTV);
        startTimeTV = itemView.findViewById(R.id.startTimeTV);
        endTimeTV = itemView.findViewById(R.id.endTimeTV);
        leftTime = itemView.findViewById(R.id.leftTime);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
        statusTV = itemView.findViewById(R.id.statusTV);
        deleteLayout = itemView.findViewById(R.id.deleteLayout);
    }
}
