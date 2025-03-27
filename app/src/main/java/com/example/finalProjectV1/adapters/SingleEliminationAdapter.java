package com.example.finalProjectV1.adapters;

import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Tournament;


import java.util.List;

public class SingleEliminationAdapter extends BaseTournamentAdapter {

    public SingleEliminationAdapter(Tournament tournament, int currentStage) {
        super(tournament, currentStage);
    }

    @Override
    protected List<Match> getMatchesForStage(int stage) {
        return tournament.getRounds().get(stage).getMatches();
    }
}