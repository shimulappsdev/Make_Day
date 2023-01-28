package com.example.makeday.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.databinding.FragmentSettingBinding;
import com.example.makeday.models.User;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SettingFragment extends Fragment {

    FragmentSettingBinding binding;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    Uri profileImageUri;
    String currentUser, profileImageUrl, userName;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating...");
        dialog.setMessage("Please Wait!");
        dialog.setCancelable(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.profilePic.setImageResource(R.drawable.profilehint);

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    userName = user.getUser_name();
                    binding.updateName.setText(user.getUser_name());
                    binding.updatePhone.setText(user.getUser_phone());
                    if (user.getUser_profile() != null){
                        Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.profilePic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,100);
        });

        binding.saveChangeBtn.setOnClickListener(view -> {
            dialog.show();
            String name = binding.updateName.getText().toString().trim();
            String phone = binding.updatePhone.getText().toString().trim();

            if (name.equals("")){
                binding.updateName.setError("");
            }else if (phone.equals("")){
                binding.updatePhone.setError("");
            }else {
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("user_name",name);
                userMap.put("user_phone",phone);
                databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                        }
                    }
                });

                if (profileImageUri != null){
                    storageReference = FirebaseStorage.getInstance().getReference("user_Img").child(currentUser).child("profile").child(userName+System.currentTimeMillis());
                    storageReference.putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        profileImageUrl = String.valueOf(uri);
                                        Map<String,Object> userMap = new HashMap<>();
                                        userMap.put("user_profile",profileImageUrl);
                                        databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    dialog.dismiss();
                                                    binding.profilePic.setImageResource(R.drawable.profilehint);
                                                    Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }

            }

        });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profile", "profile");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    profileImageUri = data.getData();
                    binding.profilePic.setImageURI(profileImageUri);
                }
            }
        }
    }
}