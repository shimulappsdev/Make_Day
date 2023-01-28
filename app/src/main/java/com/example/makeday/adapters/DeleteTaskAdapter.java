package com.example.makeday.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.models.Task;
import com.example.makeday.viewholder.DeleteTaskViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DeleteTaskAdapter extends RecyclerView.Adapter<DeleteTaskViewHolder> {

    private Context context;
    private List<Task> taskList;

    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;

    public DeleteTaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public DeleteTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeleteTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.delete_task_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteTaskViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.taskTitleTV.setText(task.getTask_title());
        holder.categoryTV.setText(task.getTask_category());
        holder.priorityTV.setText(task.getTask_priority());
        holder.locationTV.setText(task.getTask_location());
        holder.dateTV.setText(task.getTask_date());
        holder.endTimeTV.setText(task.getTask_endTime());
        holder.startTimeTV.setText(task.getTask_startTime());
        holder.statusTV.setText(task.getTask_status());

        String taskId = task.getTask_id();
        String status = task.getTask_status();

        SharedPreferences deletePre = context.getSharedPreferences("deletePref", Context.MODE_PRIVATE);
        String deleteTaskId = deletePre.getString("taskId", String.valueOf(System.currentTimeMillis()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        if (taskId.equals(deleteTaskId)){
            holder.deleteLayout.setBackgroundResource(R.color.yellow);
        }

        holder.deleteBtn.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete The Task");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        database.collection("tasks")
                                .document(currentUser)
                                .collection("myTask")
                                .document(taskId)
                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "The Task Deleted", Toast.LENGTH_SHORT).show();
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

        if (status.equals("Pending")){
            holder.statusTV.setBackgroundResource(R.drawable.status_back);
        }
        if (status.equals("Done")){
            holder.statusTV.setBackgroundResource(R.drawable.btn_back);
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
