package com.example.makeday.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeday.R;
import com.example.makeday.activities.MainActivity;
import com.example.makeday.databinding.FragmentSplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashFragment extends Fragment {

    FragmentSplashBinding binding;
    RegisterFragment registerFragment = new RegisterFragment();
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(getLayoutInflater(), container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (currentUser != null){
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }else {
                        AppCompatActivity activity = (AppCompatActivity) getActivity();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.entryActivity, registerFragment).commit();
                    }
                }
            }
        };thread.start();

        return binding.getRoot();
    }
}