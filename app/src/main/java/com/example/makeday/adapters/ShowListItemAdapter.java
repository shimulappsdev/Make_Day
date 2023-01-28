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

public class ShowListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {
    private Context context;
    private List<Item> itemList;

    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;

    public ShowListItemAdapter(Context context, List<Item> itemList) {
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

        String status = item.getItem_status();
        String listId = item.getList_id();
        String itemId = item.getItem_id();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        holder.itemRemoveBtn.setVisibility(View.GONE);

        holder.itemStatusBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (status.equals("Pending")){
                builder.setTitle("Have you done?");
            }
            if (status.equals("Done")){
                builder.setTitle("Oops pending yet?");
            }
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (status.equals("Pending")){
                        Map<String, Object> listItemMap = new HashMap<>();
                        listItemMap.put("item_status", "Done");
                        database.collection("lists")
                                .document(currentUser)
                                .collection("myLists")
                                .document(listId)
                                .collection("list_Items")
                                .document(itemId).update(listItemMap);

                    }else {
                        Map<String, Object> listItemMap = new HashMap<>();
                        listItemMap.put("item_status", "Pending");
                        database.collection("lists")
                                .document(currentUser)
                                .collection("myLists")
                                .document(listId)
                                .collection("list_Items")
                                .document(itemId).update(listItemMap);
                    }

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        });

        if (status.equals("Pending")){
            holder.itemStatusBtn.setBackgroundResource(R.drawable.status_back);
        }
        if (status.equals("Done")){
            holder.itemStatusBtn.setBackgroundResource(R.drawable.btn_back);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
