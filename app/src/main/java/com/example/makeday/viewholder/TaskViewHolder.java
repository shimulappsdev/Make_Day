package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    public TextView taskTitleTV, categoryTV, priorityTV, locationTV, dateTV, startTimeTV, endTimeTV, leftTime;
    public ImageButton pinBtn;
    public TextView doneSwitchBtn;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        taskTitleTV = itemView.findViewById(R.id.taskTitleTV);
        categoryTV = itemView.findViewById(R.id.categoryTV);
        priorityTV = itemView.findViewById(R.id.priorityTV);
        locationTV = itemView.findViewById(R.id.locationTV);
        dateTV = itemView.findViewById(R.id.dateTV);
        startTimeTV = itemView.findViewById(R.id.startTimeTV);
        endTimeTV = itemView.findViewById(R.id.endTimeTV);
        leftTime = itemView.findViewById(R.id.leftTime);
        pinBtn = itemView.findViewById(R.id.pinBtn);
        doneSwitchBtn = itemView.findViewById(R.id.doneSwitchBtn);

    }
}
