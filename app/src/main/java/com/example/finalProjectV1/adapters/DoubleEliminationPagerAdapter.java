package com.example.finalProjectV1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.classes.DoubleEliminationTournament;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

public class DoubleEliminationPagerAdapter extends TournamentPagerAdapter {
    private final boolean isWinnersBracket;

    public DoubleEliminationPagerAdapter(Context context, boolean isWinnersBracket) {
        super(context);
        this.isWinnersBracket = isWinnersBracket;
    }


    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {
        holder.recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT));

        DoubleEliminationAdapter matchAdapter = null;
        if (!tournament.isDoubles()) {
             matchAdapter = new DoubleEliminationAdapter(
                    (DoubleEliminationTournament) getTournament(),
                    position,
                    isWinnersBracket);
        }
        else {
             matchAdapter = new DoubleEliminationAdapterTeams(
                    (DoubleEliminationTournament) getTournament(),
                    position,
                    isWinnersBracket);
            Log.d("TAG", "onBindViewHolder: wellldonnnn");
        }
        holder.recyclerView.setAdapter(matchAdapter);

    }

    @Override
    public int getItemCount() {
        if (!(getTournament() instanceof DoubleEliminationTournament)) return 0;

        DoubleEliminationTournament tournament = (DoubleEliminationTournament) getTournament();

        return isWinnersBracket ?
                tournament.getRounds().size() :
                tournament.getLosersRounds().size();
    }

}
