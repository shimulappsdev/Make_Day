package com.example.makeday.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.NoteImageAdapter;
import com.example.makeday.adapters.NoteImageShowAdapter;
import com.example.makeday.databinding.FragmentNoteShowBinding;
import com.example.makeday.models.Image;
import com.example.makeday.models.Note;
import com.example.makeday.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NoteShowFragment extends Fragment {

    FragmentNoteShowBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteShowBinding.inflate(getLayoutInflater(), container, false);

        intent = getActivity().getIntent();
        String noteId = intent.getStringExtra("noteId");

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
                            binding.titleTV.setText(data.get(0).getNote_title());
                            binding.noteBodyTV.setText(data.get(0).getNote_body());
                            binding.noteDateTV.setText(data.get(0).getNote_date());
                            binding.noteTimeTV.setText(data.get(0).getNote_time());
                            binding.noteCategoryTV.setText(data.get(0).getNote_category());
                        }
                    }
                });


        binding.showImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("noteId", noteId);
            intent.putExtra("noteImageList", "noteImageList");
            startActivity(intent);
        });

        binding.editNoteBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("noteId", noteId);
            intent.putExtra("editNote", "editNote");
            startActivity(intent);
            getActivity().finish();
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