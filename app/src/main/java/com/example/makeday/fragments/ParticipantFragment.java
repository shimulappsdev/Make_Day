package com.example.makeday.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.ParticipantAdapter;
import com.example.makeday.adapters.TaskAdapter;
import com.example.makeday.databinding.FragmentParticipantBinding;
import com.example.makeday.models.Participant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
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

public class ParticipantFragment extends Fragment {

    FragmentParticipantBinding binding;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    StorageReference storage;
    Uri participantImgUri;
    String currentUser, participantImgUrl;
    ParticipantAdapter adapter;
    List<Participant> participantList;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParticipantBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Adding Participant");
        dialog.setCancelable(false);

        participantList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        Intent intent1 = getActivity().getIntent();
        String taskId = intent1.getStringExtra("taskId");

        database = FirebaseFirestore.getInstance();

        binding.skipParticipantBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("todo", "startTodo");
            startActivity(intent);
        });

        binding.addParticipantImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,200);
        });

        binding.addNewBtn.setOnClickListener(view -> {
            dialog.show();
            String participantId = UUID.randomUUID().toString();

            String name = binding.participantNameInput.getText().toString().trim();
            String role = binding.participantRoleInput.getText().toString().trim();

            if (name.equals("")){
                binding.participantNameInput.setError("");
                dialog.dismiss();
            }else if (role.equals("")){
                binding.participantRoleInput.setError("");
                dialog.dismiss();
            }else {
                Map<String, Object> participantMap = new HashMap<>();
                participantMap.put("participant_id", participantId);
                participantMap.put("participant_name", name);
                participantMap.put("participant_role", role);
                participantMap.put("participant_image", "");

                database.collection("tasks")
                        .document(currentUser)
                        .collection("myTask")
                        .document(taskId)
                        .collection("participants")
                        .document(participantId)
                        .set(participantMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    binding.participantNameInput.setText("");
                                    binding.participantRoleInput.setText("");
                                    binding.participantImg.setImageResource(R.drawable.profilehint);
                                }
                            }
                        });

                if (participantImgUri != null){
                    storage = FirebaseStorage.getInstance().getReference("user_Img")
                            .child(currentUser).child("participant").child(participantId)
                            .child(name+System.currentTimeMillis());
                    storage.putFile(participantImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        participantImgUrl = String.valueOf(uri);
                                        Map<String, Object> participantMap = new HashMap<>();
                                        participantMap.put("participant_image", participantImgUrl);
                                        database.collection("tasks")
                                                .document(currentUser)
                                                .collection("myTask")
                                                .document(taskId)
                                                .collection("participants")
                                                .document(participantId)
                                                .update(participantMap);
                                    }
                                });
                            }
                        }
                    });

                }
            }

        });

        database.collection("tasks")
                .document(currentUser)
                .collection("myTask")
                .document(taskId)
                .collection("participants")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        participantList.clear();
                        List<Participant> data = value.toObjects(Participant.class);
                        participantList.addAll(data);
                        adapter = new ParticipantAdapter(getActivity(), participantList);
                        binding.participantRecyclerView.setAdapter(adapter);
                    }
                });


        binding.completeTaskBtn.setOnClickListener(view -> {
            if (participantList.isEmpty()){
                Toast.makeText(getActivity(), "You don't added any participant yet", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getActivity(), ContainerActivity.class);
                intent.putExtra("todo", "startTodo");
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    participantImgUri = data.getData();
                    binding.participantImg.setImageURI(participantImgUri);
                }
            }
        }
    }
}