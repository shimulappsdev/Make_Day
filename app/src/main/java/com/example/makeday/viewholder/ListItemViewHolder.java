package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

public class ListItemViewHolder extends RecyclerView.ViewHolder {
    public TextView itemNameTV, itemQuantityTV, itemUnitTV, itemStatusBtn, itemAmountTV;
    public ImageView itemRemoveBtn;
    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemNameTV = itemView.findViewById(R.id.itemNameTV);
        itemQuantityTV = itemView.findViewById(R.id.itemQuantityTV);
        itemUnitTV = itemView.findViewById(R.id.itemUnitTV);
        itemStatusBtn = itemView.findViewById(R.id.itemStatusBtn);
        itemRemoveBtn = itemView.findViewById(R.id.itemRemoveBtn);
        itemAmountTV = itemView.findViewById(R.id.itemAmountTV);
    }
}
