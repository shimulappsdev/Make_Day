package com.example.makeday.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.models.Image;
import com.example.makeday.viewholder.NoteImageViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteImageAdapter extends RecyclerView.Adapter<NoteImageViewHolder> {
    private Context context;
    private List<Image> imageList;

    String currentUser;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;

    public NoteImageAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public NoteImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteImageViewHolder(LayoutInflater.from(context).inflate(R.layout.note_img_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteImageViewHolder holder, int position) {

        Image image = imageList.get(position);
        Glide.with(context).load(image.getNote_Image()).placeholder(R.drawable.profilehint).into(holder.noteImageView);

        String imageId = image.getImage_Id();
        String noteId = image.getNote_Id();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();
        holder.removeImage.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Remove");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.collection("notes")
                            .document(currentUser)
                            .collection("myNote")
                            .document(noteId)
                            .collection("note_images").document(imageId).delete();
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

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
