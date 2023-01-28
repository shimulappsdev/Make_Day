package com.example.makeday.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.activities.MainActivity;
import com.example.makeday.adapters.ListModelAdapter;
import com.example.makeday.databinding.FragmentListMainBinding;
import com.example.makeday.models.ListModel;
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

public class ListMainFragment extends Fragment {

   FragmentListMainBinding binding;
    DatabaseReference databaseReference;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;
    ListModelAdapter adapter;
    List<ListModel> listModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListMainBinding.inflate(getLayoutInflater(), container, false);

        listModelList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences2 = getActivity().getSharedPreferences("listPref", Context.MODE_PRIVATE);

        database = FirebaseFirestore.getInstance();
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

        database.collection("lists")
                .document(currentUser)
                .collection("myLists").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listModelList.clear();
                        List<ListModel> data = value.toObjects(ListModel.class);
                        listModelList.addAll(data);
                        adapter = new ListModelAdapter(getActivity(), listModelList);
                        binding.listRecyclerView.setAdapter(adapter);
                        binding.listCount.setText("You have to total "+listModelList.size()+" lists");
                        binding.progressBar.setVisibility(View.GONE);

                        SharedPreferences.Editor editor = preferences2.edit();
                        editor.putInt("totalLists", listModelList.size());
                        editor.apply();
                    }
                });

        binding.profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profile", "profile");
            startActivity(intent);
        });

        binding.addListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("addList", "addList");
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