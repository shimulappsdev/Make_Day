package com.example.makeday.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.databinding.FragmentAddNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNoteFragment extends Fragment {

    FragmentAddNoteBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser, categoryRadioString, cate;
    private int addYear, addMonth, addDay, addHour, addMinute;
    Calendar calendar;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Creating Note");
        dialog.setCancelable(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();

        binding.dateCard.setOnClickListener(view -> {
            addYear = calendar.get(Calendar.YEAR);
            addMonth = calendar.get(Calendar.MONTH);
            addDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    binding.noteDateInput.setText(dayOfMonth+ " / "+(monthOfYear+1)+" / "+year);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
            },addYear, addMonth, addDay);
            datePickerDialog.show();
        });

        binding.timeCard.setOnClickListener(view -> {
            addHour = calendar.get(Calendar.HOUR_OF_DAY);
            addMinute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                    binding.noteTimeInput.setText(getTime(hourOfDay, minuteOfHour));
                }
            },addHour, addMinute, false);
            timePickerDialog.show();
        });

        binding.categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.everyDayBtn:
                        categoryRadioString = binding.everyDayBtn.getText().toString().trim();
                        break;
                    case R.id.personalBtn:
                        categoryRadioString = binding.personalBtn.getText().toString().trim();
                        break;
                    case R.id.professionalBtn:
                        categoryRadioString = binding.professionalBtn.getText().toString().trim();
                        break;
                    case R.id.travelBtn:
                        categoryRadioString = binding.travelBtn.getText().toString().trim();
                        break;
                }
                cate = categoryRadioString;
            }
        });


        binding.createNoteBtn.setOnClickListener(view -> {
            dialog.show();

            String noteId = UUID.randomUUID().toString();

            String title = binding.titleInput.getText().toString().trim();
            String category = cate;
            String time = binding.noteTimeInput.getText().toString().trim();
            String date = binding.noteDateInput.getText().toString().trim();
            String body = binding.noteBodyInput.getText().toString().trim();

            if (title.equals("")){
                binding.titleInput.setError("");
                dialog.dismiss();
            }else if (category == null){
                Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }else if (time.equals("")){
                binding.noteTimeInput.setError("");
                dialog.dismiss();
            }else if (date.equals("")){
                binding.noteDateInput.setError("");
                dialog.dismiss();
            }else if (body.equals("")){
                binding.noteBodyInput.setError("");
                dialog.dismiss();
            }else {

                Map<String, Object> noteMap = new HashMap<>();
                noteMap.put("note_id", noteId);
                noteMap.put("note_title", title);
                noteMap.put("note_category", category);
                noteMap.put("note_time", time);
                noteMap.put("note_date", date);
                noteMap.put("note_body", body);

                database.collection("notes")
                        .document(currentUser)
                        .collection("myNote")
                        .document(noteId)
                        .set(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Intent intent = new Intent(getActivity(), ContainerActivity.class);
                                    intent.putExtra("noteId", noteId);
                                    intent.putExtra("noteImage", "noteImage");
                                    startActivity(intent);
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
            intent.putExtra("startNote", "startNote");
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