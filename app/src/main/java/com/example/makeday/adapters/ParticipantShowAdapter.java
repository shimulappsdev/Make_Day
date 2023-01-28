package com.example.makeday.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeday.R;
import com.example.makeday.models.Participant;
import com.example.makeday.viewholder.ParticipantShowViewHolder;

import java.util.List;

public class ParticipantShowAdapter extends RecyclerView.Adapter<ParticipantShowViewHolder> {

    private Context context;
    private List<Participant> participantList;

    public ParticipantShowAdapter(Context context, List<Participant> participantList) {
        this.context = context;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParticipantShowViewHolder(LayoutInflater.from(context).inflate(R.layout.show_participint_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantShowViewHolder holder, int position) {

        Participant participant = participantList.get(position);
        holder.participantNameTV.setText(participant.getParticipant_name());
        holder.participantRoleTV.setText(participant.getParticipant_role());
        Glide.with(context).load(participant.getParticipant_image()).placeholder(R.drawable.profilehint).into(holder.participantImg);

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }
}
