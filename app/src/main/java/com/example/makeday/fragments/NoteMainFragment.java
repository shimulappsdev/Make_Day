package com.example.makeday.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.activities.MainActivity;
import com.example.makeday.adapters.NoteAdapter;
import com.example.makeday.databinding.FragmentNoteMainBinding;
import com.example.makeday.models.Image;
import com.example.makeday.models.Note;
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
import java.util.List;

public class NoteMainFragment extends Fragment {

    FragmentNoteMainBinding binding;
    DatabaseReference databaseReference;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    NoteAdapter adapter;
    List<Note> noteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteMainBinding.inflate(getLayoutInflater(), container, false);

        noteList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getActivity().getSharedPreferences("notePref", Context.MODE_PRIVATE);

        SharedPreferences preferences1 = getActivity().getSharedPreferences("notePref", Context.MODE_PRIVATE);

        int everyDayCount = preferences.getInt("everyDayCount",0);
        int personalCount = preferences.getInt("personalCount",0);
        int professionalCount = preferences.getInt("professionalCount",0);
        int TravelCount = preferences.getInt("TravelCount",0);

        binding.everyDayCount.setText(everyDayCount+" Notes");
        binding.personalCount.setText(personalCount+" Notes");
        binding.professionalCount.setText(professionalCount+" Notes");
        binding.travelCount.setText(TravelCount+" Notes");

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                binding.profileName.setText("Hi "+user.getUser_name());
                if (user.getUser_profile() != null){
                    Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.profileImage);
                }else {
                    binding.profileImage.setImageResource(R.drawable.profilehint);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.progressBar.setVisibility(View.VISIBLE);

        database.collection("notes")
                .document(currentUser)
                .collection("myNote").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        noteList.clear();
                        List<Note> data = value.toObjects(Note.class);
                        noteList.addAll(data);
                        adapter = new NoteAdapter(getActivity(), noteList);
                        binding.allNotesRecyclerView.setAdapter(adapter);
                        binding.totalNotesCount.setText("You have total "+ noteList.size() +" notes");
                        binding.progressBar.setVisibility(View.GONE);

                        SharedPreferences.Editor editor = preferences1.edit();
                        editor.putInt("totalNotes", noteList.size());
                        editor.apply();

                    }
                });

        binding.profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profile", "profile");
            startActivity(intent);
        });

        binding.everyDayCateCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("everyDayNote", "everyDayNote");
            startActivity(intent);
        });

        binding.personalCateCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("personalNote", "personalNote");
            startActivity(intent);
        });

        binding.professionalCateCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("professionalNote", "professionalNote");
            startActivity(intent);
        });

        binding.travelCateCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("travelNote", "travelNote");
            startActivity(intent);
        });


        binding.addNoteBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("addNote", "addNote");
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
}