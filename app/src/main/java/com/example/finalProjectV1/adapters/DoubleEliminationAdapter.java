package com.example.finalProjectV1.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.DoubleEliminationTournament;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.Team;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

import java.util.ArrayList;
import java.util.List;
public class DoubleEliminationAdapter extends BaseTournamentAdapter {
    private final boolean isWinnersBracket;

    public DoubleEliminationAdapter(DoubleEliminationTournament tournament, int currentStage, boolean isWinnersBracket) {
        super(tournament, currentStage);
        this.isWinnersBracket = isWinnersBracket;
    }

    @Override
    protected List<Match> getMatchesForStage(int stage) {
        DoubleEliminationTournament tournament = (DoubleEliminationTournament) this.tournament;
        return isWinnersBracket ?
                tournament.getRounds().get(stage).getMatches() :
                tournament.getLosersRounds().get(stage).getMatches();
    }



    @Override
    protected void handleMatchWinner(Match match, Competitor winner) {
        super.handleMatchWinner(match, winner);

        if (isWinnersBracket) {
            if (match.getMatchId().compareTo("final_match")==0){
                if(winner.getId()==match.getCompetitor1().getId() || tournament.getRounds().get(tournament.getRounds().size()-2).getMatches().get(0).getMatchId().compareTo("final_match")==0 ){
                    return;
                }else{

                    Match finalMatch = new Match();
                    finalMatch.setMatchId("final_match");
                    finalMatch.setMatchNum(0);
                    finalMatch.setCompetitor1(new Competitor(match.getCompetitor1().getName()+("(L)"),match.getCompetitor1().getId()));
                    finalMatch.setCompetitor2(new Competitor(match.getCompetitor2().getName(),match.getCompetitor2().getId()));
                    Round Round = new Round(tournament.getRounds().size()-1, new ArrayList<>());
                    Round.getMatches().add(finalMatch);
                    tournament.getRounds().add(Round);

                }
            }
            else {
                Competitor loser = match.getCompetitor1().getId().equals(winner.getId()) ?
                        match.getCompetitor2() : match.getCompetitor1();
                tournament.moveToLosersBracket(currentStage, match.getMatchNum(), loser);
            }
        }
        else {
            tournament.moveToNextLosersRound(currentStage,match.getMatchNum(),winner);
        }
        notifyDataSetChanged();

        tournamentHelper.saveTournament(tournament, new OnAddToFirebase() {
            @Override
            public void OnComplete() {
                Log.d("TAG", "updated match: ");
            }
            @Override
            public void onSearchError(String error) {

            }
        });

    }

    static class DoubleEliminationViewHolder extends BaseTournamentAdapter.MatchViewHolder {
        TextView bracketIndicator;

        public DoubleEliminationViewHolder(@NonNull View itemView) {
             super(itemView);
            bracketIndicator = itemView.findViewById(R.id.bracketIndicator);
        }
    }


    @Override
    public DoubleEliminationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_double_elim, parent, false);

        return new DoubleEliminationViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Match match = getMatchesForStage(currentStage).get(position);
        if (match.getMatchId().compareTo("final_match")==0){
            Log.d("TAG", "onBindViewHolder: "+match.getCompetitor2().getName()); ;
            DoubleEliminationViewHolder doubleElimHolder = (DoubleEliminationViewHolder) holder;
            doubleElimHolder.bracketIndicator.setText("Final match");
            doubleElimHolder.bracketIndicator.setTextColor(Color.BLACK);

        }
        else {
            DoubleEliminationViewHolder doubleElimHolder = (DoubleEliminationViewHolder) holder;
            doubleElimHolder.bracketIndicator.setText(isWinnersBracket ? "Winners Bracket" : "Losers Bracket");
            doubleElimHolder.bracketIndicator.setTextColor(isWinnersBracket ? Color.BLUE : Color.RED);
        }
    }
}