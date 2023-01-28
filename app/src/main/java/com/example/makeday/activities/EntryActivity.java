package com.example.makeday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.makeday.R;
import com.example.makeday.fragments.LoginFragment;
import com.example.makeday.fragments.RegisterFragment;
import com.example.makeday.fragments.SplashFragment;

public class EntryActivity extends AppCompatActivity {

    SplashFragment splashFragment = new SplashFragment();
    RegisterFragment registerFragment = new RegisterFragment();
    LoginFragment loginFragment = new LoginFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Intent intent = getIntent();

        String login = intent.getStringExtra("login");

        if (login == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.entryActivity, splashFragment).commit();
        }

        if (login != null){
            if (login.equals("login")){
                getSupportFragmentManager().beginTransaction().replace(R.id.entryActivity, loginFragment).commit();
            }
        }

    }
}