package com.example.makeday.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.activities.EntryActivity;
import com.example.makeday.databinding.FragmentProfileBinding;
import com.example.makeday.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.profileImg.setImageResource(R.drawable.profilehint);

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    binding.profileName.setText(user.getUser_name());
                    binding.profileEmail.setText(user.getUser_email());
                    binding.passwordTV.setText(user.getUser_password());
                    binding.phoneTV.setText(user.getUser_phone());
                    if (user.getUser_profile() != null){
                        Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.profileImg);
                        String profileImageUrl = user.getUser_profile();
                        binding.profileImg.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(), ContainerActivity.class);
                            intent.putExtra("profileImageUrl", profileImageUrl);
                            intent.putExtra("imageView", "imageView");
                            startActivity(intent);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.settingCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("setting", "setting");
            startActivity(intent);
        });

        binding.emailCard.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            startActivity(Intent.createChooser(intent, "Email via..."));
        });
        
        binding.notificationCard.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "The feature will be added soon", Toast.LENGTH_SHORT).show();
        });

        binding.logoutBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("What's in your mind?");
            alertDialog.setIcon(R.drawable.logo);
            alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), EntryActivity.class);
                    intent.putExtra("login", "login");
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("tasksPref", Context.MODE_PRIVATE);
        int pendingTask = preferences.getInt("totalTask",0);
        int allTask = preferences.getInt("allTask",0);
        int doneTask = preferences.getInt("doneTask",0);
        binding.doneTaskTV.setText(""+doneTask);
        binding.pendingTaskTV.setText(""+pendingTask);
        binding.totalTaskTV.setText(""+allTask);

        SharedPreferences preferences1 = getActivity().getSharedPreferences("notePref", Context.MODE_PRIVATE);
        int totalNotes = preferences1.getInt("totalNotes",0);
        binding.totalNoteTV.setText(""+totalNotes);

        SharedPreferences preferences2 = getActivity().getSharedPreferences("listPref", Context.MODE_PRIVATE);
        int totalLists = preferences2.getInt("totalLists",0);
        binding.totalListTV.setText(""+totalLists);

        return binding.getRoot();
    }
}