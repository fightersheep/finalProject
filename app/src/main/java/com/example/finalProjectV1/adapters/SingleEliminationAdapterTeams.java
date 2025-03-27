package com.example.finalProjectV1.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Team;
import com.example.finalProjectV1.classes.Tournament;

public class SingleEliminationAdapterTeams extends SingleEliminationAdapter{


    public  SingleEliminationAdapterTeams(Tournament tournament, int currentStage) {
        super(tournament, currentStage);
    }

    static class SingleEliminationViewHolderTeams extends SingleEliminationAdapter.MatchViewHolder {

        TextView TeamCompetitor2Name;
        TextView TeamCompetitor1Name;

        public SingleEliminationViewHolderTeams(@NonNull View itemView) {
            super(itemView);
            TeamCompetitor2Name = itemView.findViewById(R.id.TeamCompetitor2Name);
            TeamCompetitor1Name = itemView.findViewById(R.id.TeamCompetitor1Name);
        }
    }

    public SingleEliminationViewHolderTeams onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new SingleEliminationViewHolderTeams(view);
    }

    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Match match = this.getMatchesForStage(currentStage).get(position);
        SingleEliminationViewHolderTeams SingleElimHolder = (SingleEliminationViewHolderTeams) holder;

        SingleElimHolder.TeamCompetitor2Name.setVisibility(View.VISIBLE);
        SingleElimHolder.TeamCompetitor1Name.setVisibility(View.VISIBLE);

        if (match.getCompetitor2() instanceof Team) {
            Team team2 = (Team) match.getCompetitor2();
            SingleElimHolder.TeamCompetitor2Name.setText(team2.getCompetitor2().getName());
        }
        if (match.getCompetitor1() instanceof Team) {
            Team team1 = (Team) match.getCompetitor1();
            SingleElimHolder.TeamCompetitor1Name.setText(team1.getCompetitor2().getName());
        }

    }
}

