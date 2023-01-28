package com.example.makeday.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.adapters.FragmentAdapter;
import com.example.makeday.databinding.FragmentCategoryContainerBinding;

public class CategoryContainerFragment extends Fragment {


    FragmentCategoryContainerBinding binding;
    FragmentManager fragmentManager;
    FragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryContainerBinding.inflate(getLayoutInflater(), container, false);

        fragmentManager = getFragmentManager();
        adapter = new FragmentAdapter(fragmentManager,100);
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}