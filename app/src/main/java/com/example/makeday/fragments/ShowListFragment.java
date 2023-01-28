package com.example.makeday.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.ShowListItemAdapter;
import com.example.makeday.databinding.FragmentShowListBinding;
import com.example.makeday.models.Item;
import com.example.makeday.models.ListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowListFragment extends Fragment {

    FragmentShowListBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser, listId;
    ShowListItemAdapter adapter;
    List<ListModel> listModelList;
    List<Item> itemList;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowListBinding.inflate(getLayoutInflater(), container, false);

        listModelList = new ArrayList<>();
        itemList = new ArrayList<>();

        intent = getActivity().getIntent();
        listId = intent.getStringExtra("listId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        database = FirebaseFirestore.getInstance();

        database.collection("lists")
                .document(currentUser)
                .collection("myLists")
                .whereEqualTo("list_id", listId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<ListModel> data = new ArrayList<>();
                        data = value.toObjects(ListModel.class);
                        if (data != null){
                            binding.titleTV.setText(data.get(0).getList_title());
                            binding.dateTV.setText(data.get(0).getList_date());
                            binding.typeTV.setText(data.get(0).getList_type());
                        }

                    }
                });

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
                        adapter = new ShowListItemAdapter(getActivity(), itemList);
                        binding.listItemRecyclerView.setAdapter(adapter);
                        binding.itemCount.setText(itemList.size()+" Items");
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.editListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("listId", listId);
            intent.putExtra("editList", "editList");
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("list", "startList");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
}