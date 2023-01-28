package com.example.makeday.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.models.Task;
import com.example.makeday.viewholder.TaskViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private Context context;
    private List<Task> taskList;

    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.tasklist_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        Task task = taskList.get(position);
        holder.taskTitleTV.setText(task.getTask_title());
        holder.categoryTV.setText(task.getTask_category());
        holder.priorityTV.setText(task.getTask_priority());
        holder.locationTV.setText(task.getTask_location());
        holder.dateTV.setText(task.getTask_date());
        holder.endTimeTV.setText(task.getTask_endTime());
        holder.startTimeTV.setText(task.getTask_startTime());
        holder.doneSwitchBtn.setText(task.getTask_status());

        String taskId = task.getTask_id();

        holder.itemView.setOnLongClickListener(view -> {
            SharedPreferences deletePre = context.getSharedPreferences("deletePref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = deletePre.edit();
            editor.putString("taskId", taskId);
            editor.apply();

            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("delete", "delete");
            context.startActivity(intent);
            return true;
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("showTask", "showTask");
            intent.putExtra("taskId", taskId);
            context.startActivity(intent);
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        String status = task.getTask_status();
        String taskMode = task.getTask_mode();

        holder.pinBtn.setOnClickListener(view -> {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            if (taskMode.equals("Unpin")){
                builder1.setTitle("Pin.!");
                builder1.setMessage("Is it too important?");
            }
            if (taskMode.equals("Pined")){
                builder1.setTitle("Want to Unpin?");
                builder1.setMessage("Not so important?");
            }

            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (taskMode.equals("Unpin")){
                        database = FirebaseFirestore.getInstance();
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("Task_mode", "Pined");
                        database.collection("tasks")
                                .document(currentUser)
                                .collection("myTask")
                                .document(taskId)
                                .update(taskMap);

//                      start set pin data to realtime database for show into banner
                        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
                        String title = task.getTask_title();
                        String category = task.getTask_category();
                        String priority = task.getTask_priority();
                        String startTime = task.getTask_startTime();
                        String endTime = task.getTask_endTime();
                        String date = task.getTask_date();
                        String location = task.getTask_location();
                        Map<String, Object> pinMap = new HashMap<>();
                        pinMap.put("task_id", taskId);
                        pinMap.put("task_title", title);
                        pinMap.put("task_category", category);
                        pinMap.put("task_priority", priority);
                        pinMap.put("task_startTime", startTime);
                        pinMap.put("task_endTime", endTime);
                        pinMap.put("task_date", date);
                        pinMap.put("task_location", location);
                        databaseReference.child("Last_Pined").setValue(pinMap);
//                      end set pin data to realtime database for show into banner
                    }else {
                        database = FirebaseFirestore.getInstance();
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("Task_mode", "Unpin");
                        database.collection("tasks")
                                .document(currentUser)
                                .collection("myTask")
                                .document(taskId)
                                .update(taskMap);
                    }
                }
            });

            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder1.show();


        });

        holder.doneSwitchBtn.setOnClickListener(view -> {
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
                        database = FirebaseFirestore.getInstance();
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("task_status", "Done");
                        database.collection("tasks")
                                .document(currentUser)
                                .collection("myTask")
                                .document(taskId)
                                .update(taskMap);

                    }else {
                        database = FirebaseFirestore.getInstance();
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("task_status", "Pending");
                        database.collection("tasks")
                                .document(currentUser)
                                .collection("myTask")
                                .document(taskId)
                                .update(taskMap);
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

        if (taskMode.equals("Unpin")){
            holder.pinBtn.setBackgroundResource(R.drawable.unpin_back);
        }
        if (taskMode.equals("Pined")){
            holder.pinBtn.setBackgroundResource(R.drawable.oval_back);
        }

        if (status.equals("Pending")){
            holder.doneSwitchBtn.setBackgroundResource(R.drawable.status_back);
        }
        if (status.equals("Done")){
            holder.doneSwitchBtn.setBackgroundResource(R.drawable.btn_back);
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
