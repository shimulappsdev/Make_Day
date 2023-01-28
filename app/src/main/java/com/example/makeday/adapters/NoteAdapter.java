package com.example.makeday.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.models.Note;
import com.example.makeday.viewholder.NoteViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = noteList.get(position);
        holder.noteTitleTV.setText(note.getNote_title());
        holder.noteBodyTV.setText(note.getNote_body());

        String noteId = note.getNote_id();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("showNote", "showNote");
            intent.putExtra("noteId", noteId);
            context.startActivity(intent);
        });

        holder.noteLayout.setBackgroundResource(getRandomColor());

        holder.itemView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("deleteNote", "deleteNote");
            intent.putExtra("noteId", noteId);
            context.startActivity(intent);

            SharedPreferences preferences = context.getSharedPreferences("notePref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("noteId", noteId);
            editor.apply();

            return true;
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
