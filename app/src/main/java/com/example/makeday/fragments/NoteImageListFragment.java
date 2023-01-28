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
import com.example.makeday.adapters.NoteImageShowAdapter;
import com.example.makeday.databinding.FragmentNoteImageListBinding;
import com.example.makeday.models.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NoteImageListFragment extends Fragment {

    FragmentNoteImageListBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    NoteImageShowAdapter adapter;
    List<Image> imageList;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteImageListBinding.inflate(getLayoutInflater(), container, false);

        imageList = new ArrayList<>();

        intent = getActivity().getIntent();
        String noteId = intent.getStringExtra("noteId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        database.collection("notes")
                .document(currentUser)
                .collection("myNote")
                .document(noteId)
                .collection("note_images").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        imageList.clear();
                        List<Image> data = value.toObjects(Image.class);
                        imageList.addAll(data);
                        adapter = new NoteImageShowAdapter(getActivity(), imageList);
                        binding.noteShowImageRecyclerView.setAdapter(adapter);
                        if (imageList.isEmpty()){
                            binding.noImage.setVisibility(View.VISIBLE);
                        }
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
}