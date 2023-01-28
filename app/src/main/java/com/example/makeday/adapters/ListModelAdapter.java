package com.example.makeday.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.models.Item;
import com.example.makeday.models.ListModel;
import com.example.makeday.viewholder.LIstModeViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListModelAdapter extends RecyclerView.Adapter<LIstModeViewHolder> {
    private Context context;
    private List<ListModel> listModelList;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;

    public ListModelAdapter(Context context, List<ListModel> listModelList) {
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
        holder.listDeleteBtn.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("listId", listId);
            intent.putExtra("showList", "showList");
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("listId", listId);
            intent.putExtra("deleteList", "deleteList");
            context.startActivity(intent);

            SharedPreferences preferences = context.getSharedPreferences("listPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("listId", listId);
            editor.apply();

            return true;
        });

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
