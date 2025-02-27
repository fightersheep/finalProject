package com.example.finalProjectV1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.Activities.TournamentActivity;
import com.example.finalProjectV1.Activities.TournamentDetailActivity;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.Tournament;

import java.util.List;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {
    private List<Tournament> tournamentList;

    public TournamentAdapter(List<Tournament> tournamentList) {
        this.tournamentList = tournamentList;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tournament, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        Tournament tournament = tournamentList.get(position);
        String displayText = tournament.getName() + " (" + tournament.getType() + ")";
        holder.textTournamentName.setText(displayText);

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, TournamentDetailActivity.class);
            intent.putExtra("tournamentId", tournament.getTournamentId());
            intent.putExtra("tournamentName", tournament.getName());
            intent.putExtra("tournamenType", tournament.getType());
            intent.putExtra("tournamentStartdate", tournament.getStartdate());
            intent.putExtra("tournamentLocation", tournament.getLocation());
            intent.putExtra("tournamentMax", tournament.getMaxParticipants());
            intent.putExtra("tournamentSize", tournament.getParticipants().size());


            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TextView textTournamentName;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            textTournamentName = itemView.findViewById(R.id.textTournamentName);
        }
    }
}

