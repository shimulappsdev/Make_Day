package com.example.makeday.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.models.Note;
import com.example.makeday.viewholder.NoteDeleteViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteDeleteAdapter extends RecyclerView.Adapter<NoteDeleteViewHolder> {
    private Context context;
    private List<Note> noteList;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser;

    public NoteDeleteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteDeleteViewHolder(LayoutInflater.from(context).inflate(R.layout.delete_note_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteDeleteViewHolder holder, int position) {

        Note note = noteList.get(position);
        holder.noteTitleTV.setText(note.getNote_title());
        holder.noteBodyTV.setText(note.getNote_body());

        String noteId = note.getNote_id();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        holder.deleteNoteLayout.setBackgroundResource(getRandomColor());

        database = FirebaseFirestore.getInstance();
        SharedPreferences preferences = context.getSharedPreferences("notePref", Context.MODE_PRIVATE);
        String selectNote = preferences.getString("noteId", "Note"+System.currentTimeMillis());
        if (noteId.equals(selectNote)){
            holder.deleteNoteLayout.setBackgroundResource(R.color.yellow);
        }

        holder.noteDeleteBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete The Note");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    database.collection("notes")
                            .document(currentUser)
                            .collection("myNote")
                            .document(noteId)
                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(context, "The Note Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        });

    }

    private int getRandomColor() {
        List<Integer> color = new ArrayList<>();
        color.add(R.color.note_back1);
        color.add(R.color.note_back2);
        color.add(R.color.note_back3);
        color.add(R.color.note_back4);
        color.add(R.color.note_back5);
        color.add(R.color.note_back6);
        color.add(R.color.note_back7);
        color.add(R.color.note_back8);
        color.add(R.color.note_back9);
        color.add(R.color.note_back10);

        Random random = new Random();
        int number = random.nextInt(color.size());
        return color.get(number);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
