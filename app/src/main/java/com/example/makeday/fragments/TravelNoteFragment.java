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

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.NoteAdapter;
import com.example.makeday.databinding.FragmentTravelNoteBinding;
import com.example.makeday.models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TravelNoteFragment extends Fragment {

    FragmentTravelNoteBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    NoteAdapter adapter;
    List<Note> noteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTravelNoteBinding.inflate(getLayoutInflater(), container, false);

        noteList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        SharedPreferences preferences = getActivity().getSharedPreferences("notePref", Context.MODE_PRIVATE);

        database = FirebaseFirestore.getInstance();

        binding.progressBar.setVisibility(View.VISIBLE);

        database.collection("notes")
                .document(currentUser)
                .collection("myNote").whereEqualTo("note_category","Travel").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        noteList.clear();
                        List<Note> data = value.toObjects(Note.class);
                        noteList.addAll(data);
                        adapter = new NoteAdapter(getActivity(), noteList);
                        binding.travelNoteRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("TravelCount", noteList.size());
                        editor.apply();

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