package com.example.makeday.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeday.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView participantImg;
    public TextView participantNameTV, participantRoleTV;
    public ImageView participantRemove;

    public ParticipantViewHolder(@NonNull View itemView) {
        super(itemView);

        participantImg = itemView.findViewById(R.id.participantImg);
        participantNameTV = itemView.findViewById(R.id.participantNameTV);
        participantRoleTV = itemView.findViewById(R.id.participantRoleTV);
        participantRemove = itemView.findViewById(R.id.participantRemove);

    }
}
