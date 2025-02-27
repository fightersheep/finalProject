package com.example.finalProjectV1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

import java.util.ArrayList;
import java.util.List;

public class TournamentPagerAdapter extends RecyclerView.Adapter<TournamentPagerAdapter.StageViewHolder> {
    private Context context;
    private Tournament tournament;

    public TournamentPagerAdapter(Context context) {
        this.context = context;
        this.tournament = new SingleEliminationTournament();
    }

    public void setTournament(Tournament Set) {


            this.tournament = Set;
            Log.e("TAG", "setTournament: ");
            notifyDataSetChanged();

    }

    public void addCompetitor(Competitor competitor) {
        tournament.addCompetitor(competitor);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                // Center content vertically
                int verticalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
                int totalHeight = 0;
                for (int i = 0; i < getItemCount(); i++) {
                    View view = recycler.getViewForPosition(i);
                    measureChild(view, 0, 0);
                    totalHeight += getDecoratedMeasuredHeight(view);
                }
                int topOffset = (verticalSpace - totalHeight) / 2;
                if (topOffset > 0) {
                    offsetChildrenVertical(topOffset);
                }
            }
        };
        recyclerView.setLayoutManager(layoutManager);

        return new StageViewHolder(recyclerView);
    }


    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {
        holder.recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT));
        holder.recyclerView.setAdapter(new MatchAdapter(tournament, position));
    }

    @Override
    public int getItemCount() {
        return tournament.getRounds().size();
    }
    public void initializeTournament(){
        if (!tournament.isStarted()) {
            tournament.initializeTournament(new OnAddToFirebase() {
                @Override
                public void OnComplete() {

                }

                @Override
                public void onSearchError(String error) {

                }
            });
        }
    }

    public Tournament getTournament() {
        return tournament;
    }

    static class StageViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public StageViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView;
        }
    }
}
