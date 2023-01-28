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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {
    private Context context;
    private List<Item> itemList;

    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;

    public ListItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_items_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        Item item = itemList.get(position);

        holder.itemNameTV.setText(item.getItem_name());
        holder.itemQuantityTV.setText(item.getItem_quantity());
        holder.itemUnitTV.setText(item.getItem_unit());
        holder.itemStatusBtn.setText(item.getItem_status());

        int rate = Integer.parseInt(item.getItem_amount());
        int quantity = Integer.parseInt(item.getItem_quantity());
        int amount = Integer.valueOf(rate*quantity);

        holder.itemAmountTV.setText(""+amount);


        String listId = item.getList_id();
        String itemId = item.getItem_id();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        holder.itemRemoveBtn.setOnClickListener(view -> {
            String name = item.getItem_name();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Remove "+name);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("lists")
                            .document(currentUser)
                            .collection("myLists")
                            .document(listId)
                            .collection("list_Items")
                            .document(itemId).delete();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        });

        holder.itemStatusBtn.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
