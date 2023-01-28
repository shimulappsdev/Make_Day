package com.example.makeday.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.models.Image;
import com.example.makeday.viewholder.NoteImageShowViewHolder;

import java.util.List;

public class NoteImageShowAdapter extends RecyclerView.Adapter<NoteImageShowViewHolder> {
    private Context context;
    private List<Image> imageList;

    public NoteImageShowAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public NoteImageShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteImageShowViewHolder(LayoutInflater.from(context).inflate(R.layout.note_img_show_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteImageShowViewHolder holder, int position) {
        Image image = imageList.get(position);
        Glide.with(context).load(image.getNote_Image()).placeholder(R.drawable.profilehint).into(holder.noteImageView);

        String noteImageUrl = image.getNote_Image();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("noteImageUrl", noteImageUrl);
            intent.putExtra("imageView", "imageView");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
