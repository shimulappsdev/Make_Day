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
import com.example.makeday.adapters.NoteAdapter;
import com.example.makeday.adapters.NoteDeleteAdapter;
import com.example.makeday.adapters.TaskAdapter;
import com.example.makeday.databinding.FragmentDeleteNoteBinding;
import com.example.makeday.models.Note;
import com.example.makeday.models.Task;
import com.example.makeday.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoteFragment extends Fragment {

    FragmentDeleteNoteBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    NoteDeleteAdapter adapter;
    List<Note> noteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteNoteBinding.inflate(getLayoutInflater(), container, false);

        noteList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);
        database.collection("notes")
                .document(currentUser)
                .collection("myNote").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        noteList.clear();
                        List<Note> data = value.toObjects(Note.class);
                        noteList.addAll(data);
                        adapter = new NoteDeleteAdapter(getActivity(), noteList);
                        binding.deleteNoteRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
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
}