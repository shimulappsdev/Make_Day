package com.example.makeday.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.adapters.TaskAdapter;
import com.example.makeday.databinding.FragmentPinTaskBinding;
import com.example.makeday.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PinTaskFragment extends Fragment {

    FragmentPinTaskBinding binding;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    String currentUser;
    TaskAdapter adapter;
    List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPinTaskBinding.inflate(getLayoutInflater(), container, false);

        taskList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.listProgressBar.setVisibility(View.VISIBLE);

        database.collection("tasks").document(currentUser).collection("myTask").whereEqualTo("Task_mode", "Pined").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                taskList.clear();
                List<Task> data = value.toObjects(Task.class);
                taskList.addAll(data);
                adapter = new TaskAdapter(getActivity(), taskList);
                binding.pinTaskRecyclerView.setAdapter(adapter);

                binding.listProgressBar.setVisibility(View.GONE);

            }
        });

        return binding.getRoot();
    }
}