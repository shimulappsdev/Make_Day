package com.example.makeday.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.models.Item;
import com.example.makeday.models.ListModel;
import com.example.makeday.viewholder.LIstModeViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeleteListModelAdapter extends RecyclerView.Adapter<LIstModeViewHolder> {
    private Context context;
    private List<ListModel> listModelList;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;

    public DeleteListModelAdapter(Context context, List<ListModel> listModelList) {
        this.context = context;
        this.listModelList = listModelList;
    }

    @NonNull
    @Override
    public LIstModeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LIstModeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LIstModeViewHolder holder, int position) {

        ListModel listModel = listModelList.get(position);

        holder.listTitleTV.setText(listModel.getList_title());
        holder.listTypeTV.setText(listModel.getList_type());
        holder.listDateTV.setText(listModel.getList_date());

        String listId = listModel.getList_id();

        List<Item> itemList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }
        database = FirebaseFirestore.getInstance();


        database.collection("lists")
                .document(currentUser)
                .collection("myLists")
                .document(listId)
                .collection("list_Items").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        itemList.clear();
                        List<Item> data = value.toObjects(Item.class);
                        itemList.addAll(data);
                        ListItemShortAdapter adapter = new ListItemShortAdapter(context, itemList);
                        holder.listItemRecyclerView.setAdapter(adapter);
                    }
                });
        holder.listLayout.setBackgroundResource(getRandomColor());
        holder.listDeleteBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete The List");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("lists")
                            .document(currentUser)
                            .collection("myLists")
                            .document(listId)
                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "The List Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        });

        SharedPreferences preferences = context.getSharedPreferences("listPref", Context.MODE_PRIVATE);
        String selectList = preferences.getString("listId", "List"+System.currentTimeMillis());
        if (listId.equals(selectList)){
            holder.listLayout.setBackgroundResource(R.color.yellow);
        }

    }

    private int getRandomColor() {
        List<Integer> color = new ArrayList<>();
        color.add(R.color.note_back1);
        color.add(R.color.note_back2);
        color.add(R.color.note_back3);
        color.add(R.color.note_back4);
        color.add(R.color.note_back5);
        color.add(R.color.note_back6);
        color.add(R.color.note_back7);
        color.add(R.color.note_back8);
        color.add(R.color.note_back9);
        color.add(R.color.note_back10);

        Random random = new Random();
        int number = random.nextInt(color.size());
        return color.get(number);
    }

    @Override
    public int getItemCount() {
        return listModelList.size();
    }
}
