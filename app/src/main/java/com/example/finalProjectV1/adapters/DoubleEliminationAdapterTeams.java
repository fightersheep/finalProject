package com.example.finalProjectV1.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.DoubleEliminationTournament;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Team;

public class DoubleEliminationAdapterTeams extends DoubleEliminationAdapter {
    public DoubleEliminationAdapterTeams(DoubleEliminationTournament tournament, int currentStage, boolean isWinnersBracket) {
        super(tournament, currentStage, isWinnersBracket);
    }

    static class DoubleEliminationViewHolderTeams extends DoubleEliminationAdapter.DoubleEliminationViewHolder {

        TextView TeamCompetitor2Name;
        TextView TeamCompetitor1Name;

        public DoubleEliminationViewHolderTeams(@NonNull View itemView) {
            super(itemView);
            TeamCompetitor2Name = itemView.findViewById(R.id.TeamCompetitor2Name);
            TeamCompetitor1Name = itemView.findViewById(R.id.TeamCompetitor1Name);
        }
    }

    @Override
    public DoubleEliminationViewHolderTeams onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_double_elim, parent, false);
        return new DoubleEliminationViewHolderTeams(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Match match = this.getMatchesForStage(currentStage).get(position);
        DoubleEliminationViewHolderTeams doubleElimHolder = (DoubleEliminationViewHolderTeams) holder;

        doubleElimHolder.TeamCompetitor2Name.setVisibility(View.VISIBLE);
        doubleElimHolder.TeamCompetitor1Name.setVisibility(View.VISIBLE);

        if (match.getCompetitor2() != null && match.getCompetitor2() instanceof Team) {
            Team team2 = (Team) match.getCompetitor2();
            doubleElimHolder.TeamCompetitor2Name.setText(team2.getCompetitor2().getName());
        }

        if (match.getCompetitor1() != null && match.getCompetitor1() instanceof Team) {
            Team team1 = (Team) match.getCompetitor1();
            doubleElimHolder.TeamCompetitor1Name.setText(team1.getCompetitor2().getName());
        }
    }
}