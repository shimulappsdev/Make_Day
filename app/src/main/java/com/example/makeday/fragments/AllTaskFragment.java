package com.example.makeday.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.adapters.ParticipantAdapter;
import com.example.makeday.adapters.TaskAdapter;
import com.example.makeday.databinding.FragmentAllTaskBinding;
import com.example.makeday.models.Participant;
import com.example.makeday.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllTaskFragment extends Fragment {

    FragmentAllTaskBinding binding;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    String currentUser;
    TaskAdapter adapter;
    List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllTaskBinding.inflate(getLayoutInflater(), container, false);

        taskList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.listProgressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getActivity().getSharedPreferences("tasksPref", Context.MODE_PRIVATE);

        database.collection("tasks").document(currentUser).collection("myTask").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                taskList.clear();
                List<Task> data = value.toObjects(Task.class);
                taskList.addAll(data);
                adapter = new TaskAdapter(getActivity(), taskList);
                binding.allTaskRecyclerView.setAdapter(adapter);
                binding.listProgressBar.setVisibility(View.GONE);

                int allTask = taskList.size();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("allTask", allTask);
                editor.apply();

//                for (DocumentChange data: value.getDocumentChanges()){
//
//                    if (data.getType() == DocumentChange.Type.ADDED){
//                        taskList.add(data.getDocument().toObject(Task.class));
//                    }
//                    adapter = new TaskAdapter(getActivity(), taskList);
//                    binding.allTaskRecyclerView.setAdapter(adapter);
//                }

            }
        });


        return binding.getRoot();
    }
}