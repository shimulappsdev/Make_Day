package com.example.makeday.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.DeleteListModelAdapter;
import com.example.makeday.adapters.ListModelAdapter;
import com.example.makeday.databinding.FragmentDeleteListBinding;
import com.example.makeday.models.ListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteListFragment extends Fragment {
    FragmentDeleteListBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    DeleteListModelAdapter adapter;
    List<ListModel> listModelList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteListBinding.inflate(getLayoutInflater(), container, false);

        listModelList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        database = FirebaseFirestore.getInstance();

        database.collection("lists")
                .document(currentUser)
                .collection("myLists").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listModelList.clear();
                        List<ListModel> data = value.toObjects(ListModel.class);
                        listModelList.addAll(data);
                        adapter = new DeleteListModelAdapter(getActivity(), listModelList);
                        binding.deleteListRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
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