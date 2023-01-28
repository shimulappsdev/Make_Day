package com.example.makeday.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.NoteImageAdapter;
import com.example.makeday.adapters.ParticipantAdapter;
import com.example.makeday.databinding.FragmentNoteImageBinding;
import com.example.makeday.models.Image;
import com.example.makeday.models.Participant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NoteImageFragment extends Fragment {

    FragmentNoteImageBinding binding;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    StorageReference storage;
    Uri noteImgUri,blank;
    String currentUser, noteImgUrl;
    NoteImageAdapter adapter;
    Intent intent;
    ProgressDialog dialog;
    List<Image> imageList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteImageBinding.inflate(getLayoutInflater(), container, false);

        imageList = new ArrayList<>();

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Image");
        dialog.setCancelable(false);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        intent = getActivity().getIntent();
        String noteId = intent.getStringExtra("noteId");

        binding.addNewImageBtn.setOnClickListener(view -> {
            dialog.show();
            String imageId = UUID.randomUUID().toString();
            if (noteImgUri != null){
                storage = FirebaseStorage.getInstance().getReference("user_Img")
                        .child(currentUser).child("note").child(noteId)
                        .child(""+System.currentTimeMillis());
                storage.putFile(noteImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    noteImgUrl = String.valueOf(uri);
                                    Map<String, Object> noteMap = new HashMap<>();
                                    noteMap.put("note_Id", noteId);
                                    noteMap.put("image_Id", imageId);
                                    noteMap.put("note_Image", noteImgUrl);
                                    database.collection("notes")
                                            .document(currentUser)
                                            .collection("myNote")
                                            .document(noteId)
                                            .collection("note_images")
                                            .document(imageId)
                                            .set(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        binding.noteImage.setImageResource(R.drawable.profilehint);
                                                        binding.noteImage.setImageURI(blank);
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Image Added", Toast.LENGTH_SHORT).show();
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
                        }
                    }
                });
            }else {
                dialog.dismiss();
            }
        });

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
                        adapter = new NoteImageAdapter(getActivity(), imageList);
                        binding.noteImageRecyclerView.setAdapter(adapter);
                    }
                });

        binding.noteSkipBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("startNote", "startNote");
            startActivity(intent);
        });

        binding.addImageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,201);
        });

        binding.completeNoteBtn.setOnClickListener(view -> {
            if (imageList.isEmpty()){
                Toast.makeText(getActivity(), "You don't added any image yet", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getActivity(), ContainerActivity.class);
                intent.putExtra("startNote", "startNote");
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    noteImgUri = data.getData();
                    binding.noteImage.setImageURI(noteImgUri);
                }
            }
        }
    }
}