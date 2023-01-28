package com.example.makeday.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.databinding.FragmentAddTaskBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTaskFragment extends Fragment {

    FragmentAddTaskBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser, categoryRadioString, priorityRadioSting, taskCategory;;
    private int addYear, addMonth, addDay, addHour, addMinute;
    Calendar calendar;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Creating Task");
        dialog.setCancelable(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

//        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        calendar = Calendar.getInstance();

        binding.dateSection.setOnClickListener(view -> {
            addYear = calendar.get(Calendar.YEAR);
            addMonth = calendar.get(Calendar.MONTH);
            addDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    binding.dateInput.setText(dayOfMonth+ " / "+(monthOfYear+1)+" / "+year);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
            },addYear, addMonth, addDay);
            datePickerDialog.show();
        });

        binding.startTimeCard.setOnClickListener(view -> {
            addHour = calendar.get(Calendar.HOUR_OF_DAY);
            addMinute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                    binding.startTimeInput.setText(getTime(hourOfDay, minuteOfHour));
                }
            },addHour, addMinute, false);
            timePickerDialog.show();
        });

        binding.endTimeCard.setOnClickListener(view -> {
            addHour = calendar.get(Calendar.HOUR_OF_DAY);
            addMinute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                    binding.endTimeInput.setText(getTime(hourOfDay, minuteOfHour));
                }
            },addHour, addMinute, false);
            timePickerDialog.show();
        });

        binding.categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.workBtn:
                        categoryRadioString = binding.workBtn.getText().toString().trim();
                        binding.otherCateInput.setVisibility(View.GONE);
                        break;
                    case R.id.personalBtn:
                        categoryRadioString = binding.personalBtn.getText().toString().trim();
                        binding.otherCateInput.setVisibility(View.GONE);
                        break;
                    case R.id.shoppingBtn:
                        categoryRadioString = binding.shoppingBtn.getText().toString().trim();
                        binding.otherCateInput.setVisibility(View.GONE);
                        break;
                    case R.id.healthBtn:
                        categoryRadioString = binding.healthBtn.getText().toString().trim();
                        binding.otherCateInput.setVisibility(View.GONE);
                        break;
                    case R.id.otherBtn:
                        binding.otherCateInput.setVisibility(View.VISIBLE);
                        categoryRadioString = binding.otherBtn.getText().toString().trim();
                        break;
                }
            }
        });


        binding.priorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.highBtn:
                        priorityRadioSting = binding.highBtn.getText().toString().trim();
                        break;
                    case R.id.mediumBtn:
                        priorityRadioSting = binding.mediumBtn.getText().toString().trim();
                        break;
                    case R.id.lowBtn:
                        priorityRadioSting = binding.lowBtn.getText().toString().trim();
                        break;
                }
            }
        });


        binding.insertTaskBtn.setOnClickListener(view -> {
            dialog.show();

            String taskId = UUID.randomUUID().toString();

            if (categoryRadioString != null){
                if (categoryRadioString.equals("Other")){
                    taskCategory = binding.otherCateInput.getText().toString().trim();
                }else {
                    taskCategory = categoryRadioString;
                }
            }

            String title = binding.addTitle.getText().toString().trim();
            String category = taskCategory;
            String priority = priorityRadioSting;
            String startTime = binding.startTimeInput.getText().toString().trim();
            String endTime = binding.endTimeInput.getText().toString().trim();
            String date = binding.dateInput.getText().toString().trim();
            String location = binding.addLocation.getText().toString().trim();
            String shortDes = binding.addShortDes.getText().toString().trim();


            if (title.equals("")){
                binding.addTitle.setError("Required");
                dialog.dismiss();
            }else if (startTime.equals("")){
                binding.startTimeInput.setError("Required");
                dialog.dismiss();
            }else if (endTime.equals("")){
                binding.endTimeInput.setError("Required");
                dialog.dismiss();
            }else if (date.equals("")){
                binding.dateInput.setError("Required");
                dialog.dismiss();
            }else if (location.equals("")){
                binding.addLocation.setError("Required");
                dialog.dismiss();
            }else if (category.equals("")){
                Toast.makeText(getActivity(), "Category Not Selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }else if (priority.equals("")){
                Toast.makeText(getActivity(), "Priority Not Selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }else {

                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("task_id", taskId);
                taskMap.put("task_title", title);
                taskMap.put("task_category", category);
                taskMap.put("category_status", "");
                taskMap.put("task_priority", priority);
                taskMap.put("task_startTime", startTime);
                taskMap.put("task_endTime", endTime);
                taskMap.put("task_date", date);
                taskMap.put("task_location", location);
                taskMap.put("task_status", "Pending");
                taskMap.put("Task_mode", "Unpin");
                taskMap.put("task_shortDes", shortDes);

                database.collection("tasks")
                        .document(currentUser)
                        .collection("myTask")
                        .document(taskId)
                        .set(taskMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), ContainerActivity.class);
                            intent.putExtra("participant", "participant");
                            intent.putExtra("taskId", taskId);
                            startActivity(intent);

                            if (categoryRadioString.equals("Other")){
                                Map<String, Object> taskMap = new HashMap<>();
                                taskMap.put("category_status", "Other");
                                database.collection("tasks")
                                        .document(currentUser)
                                        .collection("myTask")
                                        .document(taskId)
                                        .update(taskMap);
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Oops..Failed", Toast.LENGTH_SHORT).show();
                    }
                });


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

    private String getTime(int hour, int minute){
        Time time = new Time(hour, minute,0);
        Format formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(time);
    }
}