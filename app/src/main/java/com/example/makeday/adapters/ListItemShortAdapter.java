package com.example.makeday.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.models.Item;
import com.example.makeday.viewholder.ListItemViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItemShortAdapter extends RecyclerView.Adapter<ListItemViewHolder> {
    private Context context;
    private List<Item> itemList;

    public ListItemShortAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_items_short_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        Item item = itemList.get(position);

        holder.itemNameTV.setText(item.getItem_name());
        holder.itemQuantityTV.setText(item.getItem_quantity());
        holder.itemUnitTV.setText(item.getItem_unit());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
