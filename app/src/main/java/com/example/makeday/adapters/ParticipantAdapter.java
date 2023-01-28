package com.example.makeday.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.models.Participant;
import com.example.makeday.viewholder.ParticipantViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantViewHolder> {

    private Context context;
    private List<Participant> participantList;

    public ParticipantAdapter(Context context, List<Participant> participantList) {
        this.context = context;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParticipantViewHolder(LayoutInflater.from(context).inflate(R.layout.participant_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {

        Participant participant = participantList.get(position);
        holder.participantNameTV.setText(participant.getParticipant_name());
        holder.participantRoleTV.setText(participant.getParticipant_role());
        Glide.with(context).load(participant.getParticipant_image()).placeholder(R.drawable.profilehint).into(holder.participantImg);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();

        SharedPreferences preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String taskId = preferences.getString("taskId", String.valueOf(System.currentTimeMillis()));

        holder.participantRemove.setOnClickListener(view -> {

            String participantId = participant.getParticipant_id();
            String name = participant.getParticipant_name();
            String role = participant.getParticipant_role();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Remove "+name);
            builder.setMessage(role);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    database.collection("tasks")
                            .document(currentUser)
                            .collection("myTask")
                            .document(taskId)
                            .collection("participants")
                            .document(participantId).delete();
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
        return participantList.size();
    }
}
