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

import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.ParticipantShowAdapter;
import com.example.makeday.databinding.FragmentShowTaskBinding;
import com.example.makeday.models.Participant;
import com.example.makeday.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowTaskFragment extends Fragment {

    FragmentShowTaskBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    ParticipantShowAdapter adapter;
    List<Participant> participantList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowTaskBinding.inflate(getLayoutInflater(), container, false);

        participantList = new ArrayList<>();

        Intent intent = getActivity().getIntent();
        String taskId = intent.getStringExtra("taskId");

        database = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database.collection("tasks")
                .document(currentUser).collection("myTask")
                .whereEqualTo("task_id", taskId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        List<Task> data = new ArrayList<>();
                        data =  value.toObjects(Task.class);

                        if (value!= null){
                            binding.titleTV.setText(data.get(0).getTask_title());
                            binding.categoryTV.setText(data.get(0).getTask_category());
                            binding.priorityTV.setText(data.get(0).getTask_priority());
                            binding.startTimeTV.setText(data.get(0).getTask_startTime());
                            binding.endTimeTV.setText(data.get(0).getTask_endTime());
                            binding.dateTV.setText(data.get(0).getTask_date());
                            binding.statusTV.setText(data.get(0).getTask_status());
                            binding.modeTv.setText(data.get(0).getTask_mode());
                            binding.locationTV.setText(data.get(0).getTask_location());
                            binding.shortDesTV.setText(data.get(0).getTask_shortDes());
                        }

                    }
                });

        database.collection("tasks")
                .document(currentUser)
                .collection("myTask")
                .document(taskId)
                .collection("participants")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        participantList.clear();
                        List<Participant> data = value.toObjects(Participant.class);
                        participantList.addAll(data);
                        adapter = new ParticipantShowAdapter(getActivity(), participantList);
                        binding.participantRecyclerView.setAdapter(adapter);
                    }
                });


        binding.editBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(getActivity(), ContainerActivity.class);
            intent1.putExtra("updateTask", "updateTask");
            intent1.putExtra("taskId", taskId);
            startActivity(intent1);
            getActivity().finish();
        });

        binding.backBtn.setOnClickListener(view -> {
            Intent showIntent = new Intent(getActivity(), ContainerActivity.class);
            showIntent.putExtra("todo", "startTodo");
            startActivity(showIntent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
}