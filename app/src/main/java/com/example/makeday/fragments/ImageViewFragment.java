package com.example.makeday.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.databinding.FragmentImageViewBinding;

public class ImageViewFragment extends Fragment {

    FragmentImageViewBinding binding;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageViewBinding.inflate(getLayoutInflater(), container, false);

        intent = getActivity().getIntent();
        String noteImage = intent.getStringExtra("noteImageUrl");
        String profileImage = intent.getStringExtra("profileImageUrl");

        if (noteImage != null){
            Glide.with(getActivity()).load(noteImage).placeholder(R.drawable.profilehint).into(binding.myZoomageView);
        }

        if (profileImage != null){
            Glide.with(getActivity()).load(profileImage).placeholder(R.drawable.profilehint).into(binding.myZoomageView);
        }


        return binding.getRoot();
    }
}