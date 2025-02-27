package com.example.finalProjectV1.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.SimpleTextWatcher;
import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.TennisScore;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.example.finalProjectV1.util.CustomAlertDialog;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private Tournament tournament;
    private int currentStage;
    private FirebaseTournamentHelper tournamentHelper=  new FirebaseTournamentHelper();



    MatchAdapter(Tournament tournament, int currentStage) {
        this.tournament = tournament;
        this.currentStage = currentStage;

    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = tournament.getRounds().get(currentStage).getMatches().get(position);
        bindMatch(holder, match);
    }

    private void bindMatch(@NonNull MatchViewHolder holder, Match match) {
        Competitor competitor1 = match.getCompetitor1();
        Competitor competitor2 = match.getCompetitor2();
        if(tournament.getAdmin().getId().compareTo(
                dataManeger.getUser().getUserId())!=0|| match.isComplete() || !tournament.isStarted())  {
            Log.d("Tag", "bindMatch: "+(tournament.getAdmin().getId().compareTo(
                    dataManeger.getUser().getUserId())==0));
            holder.competitor2PointButton.setVisibility(View.GONE);
            holder.competitor1PointButton.setVisibility(View.GONE);
        }

        holder.competitor1Name.setText(competitor1.getName());
        holder.competitor2Name.setText(competitor2.getName());
        setupTennisMatch(holder,match);

        if (match.isComplete()) {
            highlightWinner(holder, match.getWinnerId(), competitor1.getId(),
                    competitor2 != null ? competitor2.getId() : null);
        }
    }

    private void setupTennisMatch(MatchViewHolder holder, Match match) {

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
            }

        });
        if (match.isComplete()){
            holder.competitor2PointButton.setVisibility(View.GONE);
            holder.competitor1PointButton.setVisibility(View.GONE);
        }
    }

    private void handlePoint(TennisScore scorer, TennisScore opponent) {
        Log.d("Tennis", "Current points - Scorer: " + scorer.getPoints() + ", Opponent: " + opponent.getPoints());

        if (scorer.getPoints() == 3 && opponent.getPoints() == 3) {
            // Deuce situation
            scorer.addPoint(); // Move to advantage
            Log.d("Tennis", "Deuce - Moving to advantage");
        } else if (scorer.getPoints() >= 4 && opponent.getPoints() >= 4) {
            // Both at or past advantage
            if (scorer.getPoints() == opponent.getPoints()) {
                // Back to deuce
                scorer.setPoints(3);
                opponent.setPoints(3);
                Log.d("Tennis", "Back to deuce");
            }
        } else if (scorer.getPoints() >= 3 && scorer.getPoints() > opponent.getPoints() + 1) {
            // Win game condition
            scorer.winGame();
            opponent.setPoints(0);
            Log.d("Tennis", "Game won - New game starting");
        } else {
            // Normal point scoring
            scorer.addPoint();
            Log.d("Tennis", "Normal point added");
        }
        tournamentHelper.saveTournament(tournament, new OnAddToFirebase() {
            @Override
            public void OnComplete() {
                notifyDataSetChanged();

            }

            @Override
            public void onSearchError(String error) {


            }
        });

    }


    private void updateScoreDisplay(MatchViewHolder holder, TennisScore score1, TennisScore score2) {

        holder.competitor1Sets.setText(String.valueOf(score1.getSets()));
        holder.competitor1games.setText(String.valueOf(score1.getGames()));
        holder.competitor1points.setText(score1.getCurrentGameScore());

        holder.competitor2Sets.setText(String.valueOf(score2.getSets()));
        holder.competitor2games.setText(String.valueOf(score2.getGames()));
        holder.competitor2points.setText(score2.getCurrentGameScore());
    }


    private boolean checkMatchWinner(Match match, TennisScore score1, TennisScore score2) {
        if (score1.hasWonMatch() || score2.hasWonMatch()) {
            Competitor winner;
            if (score1.hasWonMatch()){
                 winner =match.getCompetitor1();
            }
            else {
                 winner =match.getCompetitor2();
            }
            match.setComplete(true);
            match.setWinnerId(winner.getId());
            if (tournament.getRounds().size()>currentStage+1) {
                tournament.MoveParticipantOn(currentStage + 1, match.getMatchNum(), winner);
                notifyDataSetChanged();
                Log.d("TA", "checkMatchWinner: dksl ");
            }
            tournamentHelper.updateMatch(tournament.getTournamentId(), currentStage, match.getMatchId(), match);
            return true;
        }
        return false;
    }





    private void setupCompetitor(TextView nameView, Competitor competitor) {
        if (competitor != null) {
            nameView.setText(competitor.getName());
        } else {
            nameView.setText("-");
        }
    }



    private void showError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void highlightWinner(MatchViewHolder holder, String winnerId, String id1, String id2) {
        holder.competitor1Name.setTextColor(winnerId.equals(id1) ? Color.GREEN : Color.BLACK);
        holder.competitor2Name.setTextColor(winnerId.equals(id2) ? Color.GREEN : Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return tournament.getRounds().get(currentStage).getMatches().size();
    }
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
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

