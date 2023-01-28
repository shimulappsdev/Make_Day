package com.example.makeday.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.activities.MainActivity;
import com.example.makeday.adapters.TaskAdapter;
import com.example.makeday.databinding.FragmentTodoMainBinding;
import com.example.makeday.models.Task;
import com.example.makeday.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoMainFragment extends Fragment {

    FragmentTodoMainBinding binding;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    String currentUser;
    TaskAdapter adapter;
    List<Task> taskList;
    List<Task> pendingTaskList;
    List<Task> doneTaskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodoMainBinding.inflate(getLayoutInflater(), container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        SharedPreferences categoryPreference = getActivity().getSharedPreferences("categoryPref", Context.MODE_PRIVATE);
        int workCount = categoryPreference.getInt("workCount",0);
        int personalCount = categoryPreference.getInt("personalCount",0);
        int shoppingCount = categoryPreference.getInt("shoppingCount",0);
        int healthCount = categoryPreference.getInt("healthCount",0);
        int otherCount = categoryPreference.getInt("otherCount",0);

        binding.workTaskCount.setText(workCount+" Tasks");
        binding.personalTaskCount.setText(personalCount+" Tasks");
        binding.shoppingTaskCount.setText(shoppingCount+" Tasks");
        binding.healthTaskCount.setText(healthCount+" Tasks");
        binding.otherTaskCount.setText(otherCount+" Tasks");


        binding.bannerProgressBar.setVisibility(View.VISIBLE);
        binding.listProgressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                binding.profileName.setText("Hi "+user.getUser_name());
                if (user.getUser_profile() != null){
                    Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.profileImg);
                }else {
                    binding.profileImg.setImageResource(R.drawable.profilehint);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.profileImg.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profile", "profile");
            startActivity(intent);
        });

        binding.addTaskBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("addTask", "addTask");
            startActivity(intent);
        });

        binding.workCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("work", "work");
            startActivity(intent);
            getActivity().finish();
        });

        binding.personalCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("personal", "personal");
            startActivity(intent);
            getActivity().finish();
        });

        binding.shoppingCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("shopping", "shopping");
            startActivity(intent);
            getActivity().finish();
        });

        binding.healthCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("health", "health");
            startActivity(intent);
            getActivity().finish();
        });

        binding.otherCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("other", "other");
            startActivity(intent);
            getActivity().finish();
        });

        binding.seeAllTask.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("allTask", "allTask");
            startActivity(intent);
        });

        taskList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        String today = getToday();

        database.collection("tasks").document(currentUser).collection("myTask").whereEqualTo("task_date", today).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                taskList.clear();
                List<Task> data = value.toObjects(Task.class);
                taskList.addAll(data);
                adapter = new TaskAdapter(getActivity(), taskList);
                binding.todayRecyclerView.setAdapter(adapter);
                binding.taskCountTV.setText("Today you have "+taskList.size()+" task");

                binding.listProgressBar.setVisibility(View.GONE);
            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        databaseReference1.child("Last_Pined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task task = snapshot.getValue(Task.class);
                if (snapshot.exists()){
                    binding.taskTitleTV.setText(task.getTask_title());
                    binding.locationTV.setText(task.getTask_location());
                    binding.dateTV.setText(task.getTask_date());
                    binding.startTimeTV.setText(task.getTask_startTime());
                    binding.endTimeTV.setText(task.getTask_endTime());
                    binding.categoryTV.setText(task.getTask_category());
                    binding.priorityTV.setText(task.getTask_priority());
                }
                binding.bannerProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

//        pendingTaskList count start
        SharedPreferences preferences = getActivity().getSharedPreferences("tasksPref", Context.MODE_PRIVATE);

        pendingTaskList = new ArrayList<>();
        database.collection("tasks")
                .document(currentUser)
                .collection("myTask")
                .whereEqualTo("task_status", "Pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        pendingTaskList.clear();
                        List<Task> data = value.toObjects(Task.class);
                        pendingTaskList.addAll(data);
                        int pendingTask = pendingTaskList.size();
                        binding.pendingTaskTv.setText(pendingTask+" task pending");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("totalTask", pendingTask);
                        editor.apply();
                    }
                });

//        pendingTaskList count end

//        doneTaskList count start

        doneTaskList = new ArrayList<>();

        database.collection("tasks").document(currentUser).collection("myTask").whereEqualTo("task_status", "Done").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                doneTaskList.clear();
                List<Task> data = value.toObjects(Task.class);
                doneTaskList.addAll(data);

                int doneTask = taskList.size();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("doneTask", doneTask);
                editor.apply();

                binding.listProgressBar.setVisibility(View.GONE);

            }
        });

//        doneTaskList count end


        return binding.getRoot();
    }

    private String getToday() {
        int dayOfMonth, monthOfYear, year;
        Calendar calendar = Calendar.getInstance();
        dayOfMonth = calendar.get(Calendar.DATE);
        monthOfYear = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        return dayOfMonth+ " / "+(monthOfYear+1)+" / "+year;
    }
}