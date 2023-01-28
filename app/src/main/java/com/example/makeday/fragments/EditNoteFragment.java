package com.example.makeday.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.databinding.FragmentEditNoteBinding;
import com.example.makeday.models.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditNoteFragment extends Fragment {

    FragmentEditNoteBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser, categoryRadioString, cate, noteId;
    private int addYear, addMonth, addDay, addHour, addMinute;
    Calendar calendar;
    Intent intent;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditNoteBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating Note");
        dialog.setCancelable(false);

        intent = getActivity().getIntent();
        noteId = intent.getStringExtra("noteId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        database.collection("notes")
                .document(currentUser)
                .collection("myNote").whereEqualTo("note_id", noteId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Note> data = new ArrayList<>();
                        data =  value.toObjects(Note.class);
                        if (value!= null){
                            binding.editTitle.setText(data.get(0).getNote_title());
                            binding.noteBodyInput.setText(data.get(0).getNote_body());
                            binding.noteDateInput.setText(data.get(0).getNote_date());
                            binding.noteTimeInput.setText(data.get(0).getNote_time());
                            cate = data.get(0).getNote_category();
                        }
                    }
                });

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

        binding.updateNoteBtn.setOnClickListener(view -> {
            dialog.show();
            String title = binding.editTitle.getText().toString().trim();
            String category = cate;
            String time = binding.noteTimeInput.getText().toString().trim();
            String date = binding.noteDateInput.getText().toString().trim();
            String body = binding.noteBodyInput.getText().toString().trim();

            if (title.equals("")){
                binding.editTitle.setError("");
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
                        .update(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            intent.putExtra("showNote", "showNote");
            intent.putExtra("noteId", noteId);
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