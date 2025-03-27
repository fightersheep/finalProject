package com.example.finalProjectV1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.DoubleEliminationTournament;
import com.example.finalProjectV1.classes.SingleEliminationTeams;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

import java.util.ArrayList;
import java.util.List;

public class TournamentPagerAdapter extends RecyclerView.Adapter<TournamentPagerAdapter.StageViewHolder> {
    protected Context context;
    protected Tournament tournament;
    private final List<Integer> scrollPositions = new ArrayList<>();

    public TournamentPagerAdapter(Context context) {
        this.context = context;
        this.tournament = new SingleEliminationTournament();
    }

    public void setTournament(Tournament Set) {

        this.tournament = Set;
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
        SingleEliminationAdapter matchAdapter = null;
        if (!tournament.isDoubles()) {
            matchAdapter = new SingleEliminationAdapter(tournament, position);
        }
        else {
            matchAdapter = new SingleEliminationAdapterTeams(tournament, position);
            Log.d("TAG", "onBindViewHolder: wellldonnnn");
        }
        holder.recyclerView.setAdapter(matchAdapter);
    }

    @Override
    public int getItemCount() {
        return tournament.getRounds().size();
    }

    public void initializeTournament() {
        if (!tournament.isStarted()) {
            Log.d("TAG", "onDataChange1: " +tournament.getAdmin().getName());

            tournament.initializeTournament();
            Log.d("TAG", "onDataChange3: " +tournament.getAdmin().getName());

            FirebaseTournamentHelper helper = new FirebaseTournamentHelper();
            helper.saveTournament(tournament, new OnAddToFirebase() {
                @Override
                public void OnComplete() {
                    Log.d("TAG", "OnComplete: saved");
                }

                @Override
                public void onSearchError(String error) {
                    Log.d("TAG", "onDataChange: error" +error);

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