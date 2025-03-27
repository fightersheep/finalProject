package com.example.finalProjectV1.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.TennisScore;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

import java.util.List;

public abstract class BaseTournamentAdapter extends RecyclerView.Adapter<BaseTournamentAdapter.MatchViewHolder> {
    protected Tournament tournament;
    protected int currentStage;
    protected FirebaseTournamentHelper tournamentHelper = new FirebaseTournamentHelper();

    public BaseTournamentAdapter(Tournament tournament, int currentStage) {
        this.tournament = tournament;
        this.currentStage = currentStage;
    }

    protected abstract List<Match> getMatchesForStage(int stage);

    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = getMatchesForStage(currentStage).get(position);
        bindMatch(holder, match);
    }

    protected void bindMatch(@NonNull MatchViewHolder holder, Match match) {
        Competitor competitor1 = match.getCompetitor1();
        Competitor competitor2 = match.getCompetitor2();
        tournament.getName();
        if(tournament.getAdmin().getId().compareTo(dataManeger.getUser().getUserId())!=0
                || match.isComplete()
                || !tournament.isStarted()) {
            holder.competitor2PointButton.setVisibility(View.GONE);
            holder.competitor1PointButton.setVisibility(View.GONE);
        }

        holder.competitor1Name.setText(competitor1.getName());
        holder.competitor2Name.setText(competitor2.getName());
        setupTennisMatch(holder, match);
        if (match.isComplete()) {
            highlightWinner(holder, match.getWinnerId(), competitor1.getId(),
                    competitor2 != null ? competitor2.getId() : null);
        }
    }

    protected void setupTennisMatch(MatchViewHolder holder, Match match) {
        TennisScore score1 = match.getCompetitor1().getScore();
        TennisScore score2 = match.getCompetitor2().getScore();

        updateScoreDisplay(holder, score1, score2);


        holder.competitor1PointButton.setOnClickListener(v -> {
            if (tournament.isStarted()) {

                handlePoint(score1, score2);

                updateScoreDisplay(holder, score1, score2);
                if (checkMatchWinner(match, score1, score2)) {
                    holder.competitor2PointButton.setVisibility(View.GONE);
                    holder.competitor1PointButton.setVisibility(View.GONE);
                }
                else {
                    tournamentHelper.saveTournament(tournament, new OnAddToFirebase() {
                        @Override
                        public void OnComplete() {
                            Log.d("TAG", "OnComplete: save1 ");
                        }

                        @Override
                        public void onSearchError(String error) {
                        }
                    });
                }
            }
        });

        holder.competitor2PointButton.setOnClickListener(v -> {
            if (tournament.isStarted()) {
                handlePoint(score2, score1);
                updateScoreDisplay(holder, score1, score2);
                if (checkMatchWinner(match, score1, score2)) {
                    holder.competitor2PointButton.setVisibility(View.GONE);
                    holder.competitor1PointButton.setVisibility(View.GONE);
                }
                else {
                    tournamentHelper.saveTournament(tournament, new OnAddToFirebase() {
                        @Override
                        public void OnComplete() {
                            Log.d("TAG", "OnComplete: save2");

                        }

                        @Override
                        public void onSearchError(String error) {
                        }
                    });
                }
            }
        });

        if (match.isComplete()) {
            holder.competitor2PointButton.setVisibility(View.GONE);
            holder.competitor1PointButton.setVisibility(View.GONE);
        }
    }

    protected void handlePoint(TennisScore scorer, TennisScore opponent) {
        if (scorer.getPoints() == 3 && opponent.getPoints() == 3) {
            scorer.addPoint();
        } else if (scorer.getPoints() >= 4 && opponent.getPoints() >= 4) {
            if (scorer.getPoints() == opponent.getPoints()) {
                scorer.setPoints(3);
                opponent.setPoints(3);
            }
        } else if (scorer.getPoints() >= 3 && scorer.getPoints() > opponent.getPoints() + 1) {
            scorer.winGame();
            opponent.setPoints(0);
        } else {
            scorer.addPoint();
        }

        notifyDataSetChanged();

    }

    protected void updateScoreDisplay(MatchViewHolder holder, TennisScore score1, TennisScore score2) {
        holder.competitor1Sets.setText(String.valueOf(score1.getSets()));
        holder.competitor1games.setText(String.valueOf(score1.getGames()));
        holder.competitor1points.setText(score1.getCurrentGameScore());

        holder.competitor2Sets.setText(String.valueOf(score2.getSets()));
        holder.competitor2games.setText(String.valueOf(score2.getGames()));
        holder.competitor2points.setText(score2.getCurrentGameScore());
    }

    protected boolean checkMatchWinner(Match match, TennisScore score1, TennisScore score2) {
        if (score1.hasWonMatch() || score2.hasWonMatch()) {
            Competitor winner = score1.hasWonMatch() ? match.getCompetitor1() : match.getCompetitor2();
            handleMatchWinner(match, winner);
            return true;
        }
        return false;
    }

    protected void handleMatchWinner(Match match, Competitor winner) {
        match.setComplete(true);
        match.setWinnerId(winner.getId());

        if (tournament.getRounds().size() > currentStage + 1) {
            Log.d("TAG", "handleMatchWinner:size "+tournament.getRounds().size());
            Log.d("TAG", "handleMatchWinner:curre "+currentStage + 1);

            tournament.MoveParticipantOn(currentStage + 1, match.getMatchNum(), winner);
            notifyDataSetChanged();
        }

        tournamentHelper.saveTournament(tournament, new OnAddToFirebase() {
            @Override
            public void OnComplete() {
                Log.d("TAG", "OnComplete: save3");

            }

            @Override
            public void onSearchError(String error) {
            }
        });
    }

    protected void highlightWinner(MatchViewHolder holder, String winnerId, String id1, String id2) {
        holder.competitor1Name.setTextColor(winnerId.equals(id1) ? Color.GREEN : Color.BLACK);
        holder.competitor2Name.setTextColor(winnerId.equals(id2) ? Color.GREEN : Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return getMatchesForStage(currentStage).size();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView competitor1Name, competitor2Name;
        TextView competitor1Sets, competitor1games, competitor1points;
        TextView competitor2Sets, competitor2games, competitor2points;
        Button competitor1PointButton, competitor2PointButton;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            competitor1Name = itemView.findViewById(R.id.competitor1Name);
            competitor2Name = itemView.findViewById(R.id.competitor2Name);
            competitor1Sets = itemView.findViewById(R.id.competitor1Set1);
            competitor1games = itemView.findViewById(R.id.competitor1Set2);
            competitor2Sets = itemView.findViewById(R.id.competitor2Set1);
            competitor2games = itemView.findViewById(R.id.competitor2Set2);
            competitor1points = itemView.findViewById(R.id.competitor1CurrentGame);
            competitor2points = itemView.findViewById(R.id.competitor2CurrentGame);
            competitor2PointButton = itemView.findViewById(R.id.competitor2PointButton);
            competitor1PointButton = itemView.findViewById(R.id.competitor1PointButton);
        }
    }
}