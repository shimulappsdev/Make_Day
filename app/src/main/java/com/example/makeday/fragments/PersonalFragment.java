package com.example.makeday.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.TaskAdapter;
import com.example.makeday.databinding.FragmentPersonalBinding;
import com.example.makeday.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PersonalFragment extends Fragment {

    FragmentPersonalBinding binding;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    String currentUser;
    TaskAdapter adapter;
    List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(getLayoutInflater(), container, false);

        taskList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        SharedPreferences categoryPreference = getActivity().getSharedPreferences("categoryPref", Context.MODE_PRIVATE);

        binding.listProgressBar.setVisibility(View.VISIBLE);

        database.collection("tasks").document(currentUser).collection("myTask").whereEqualTo("task_category", "Personal").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                taskList.clear();
                List<Task> data = value.toObjects(Task.class);
                taskList.addAll(data);
                adapter = new TaskAdapter(getActivity(), taskList);
                binding.personalRecyclerView.setAdapter(adapter);

                SharedPreferences.Editor editor = categoryPreference.edit();
                editor.putInt("personalCount", taskList.size());
                editor.apply();

                binding.listProgressBar.setVisibility(View.GONE);

            }
        });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("todo", "startTodo");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
}