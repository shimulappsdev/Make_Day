package com.example.makeday.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.databinding.ActivityMainBinding;
import com.example.makeday.fragments.ListMainFragment;
import com.example.makeday.fragments.NoteMainFragment;
import com.example.makeday.fragments.TodoMainFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.todoCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("todo", "startTodo");
            startActivity(intent);
        });

        binding.noteCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("startNote", "startNote");
            startActivity(intent);
        });

        binding.listCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("list", "startList");
            startActivity(intent);
        });

        binding.timePassCard.setOnClickListener(view -> {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("What's in your mind ?");
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}