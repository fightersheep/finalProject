package com.example.finalProjectV1.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DoubleEliminationTeams extends DoubleEliminationTournament {

    public void initializeTournament() {
        // First initialize winners bracket using parent class method
        initializeWinnerTournament();
        initializeLosersRounds();
        initializeFinalRound();
        Log.d("TAG", "initializeTournament: " + this.rounds.size());
    }

    public void initializeWinnerTournament() {

        Log.d("TAG", "onDataChange2: " + this.admin.name);

        // Clear existing rounds
        this.rounds = new ArrayList<>();
        // Create competitors from participants
        List<Competitor> competitors = new ArrayList<>();
        for (ShortUser participant : participants) {
            competitors.add(new Competitor(participant.getName(), participant.getId()));
        }

        // Calculate number of rounds needed
        int totalParticipants = competitors.size();
        int requiredRounds = (int) Math.ceil(Math.log((int) Math.ceil((double) totalParticipants / 2)) / Math.log(2));
        Log.d("TAG", "initializeWinnerTournament6: " + requiredRounds);

        // Create first round matches
        Round firstRound = new Round(0, new ArrayList<>());
        for (int i = 0; i < totalParticipants; i += 4) {
            Match match = new Match();
            match.setMatchId("match_0_" + (i / 4));
            match.setMatchNum(i / 4);

            // Set first competitor

            // Set second competitor if available, otherwise TBD
            if (i + 1 < totalParticipants) {
                match.setCompetitor1(new Team(competitors.get(i), competitors.get(i + 1)));
            } else {
                match.setCompetitor1(new Team(competitors.get(i), new Competitor("TBD")));
            }
            if (i + 2 < totalParticipants) {
                if (i + 3 < totalParticipants) {
                    match.setCompetitor2(new Team(competitors.get(i + 2), competitors.get(i + 3)));
                } else {
                    match.setCompetitor2(new Team(competitors.get(i), new Competitor("TBD")));
                }
            } else {
                match.setCompetitor2(new Team(new Competitor("TBD"), new Competitor("TBD")));
            }
            firstRound.getMatches().add(match);
        }
        rounds.add(firstRound);

        // Create subsequent rounds
        for (int roundIndex = 1; roundIndex < requiredRounds; roundIndex++) {
            Round newRound = new Round(roundIndex, new ArrayList<>());
            int matchesInRound = (int) Math.ceil(((int) Math.ceil((double) totalParticipants / 2)) / Math.pow(2, roundIndex + 1));

            for (int matchIndex = 0; matchIndex < matchesInRound; matchIndex++) {
                Match match = new Match();
                match.setMatchId(String.format("match_%d_%d", roundIndex, matchIndex));
                match.setMatchNum(matchIndex);
                match.setCompetitor1(new Team(new Competitor("TBD"), new Competitor("TBD")));
                match.setCompetitor2(new Team(new Competitor("TBD"), new Competitor("TBD")));
                newRound.getMatches().add(match);
            }

            rounds.add(newRound);
        }

    }

    private void initializeLosersRounds() {
        this.losersRounds = new ArrayList<>();

        int totalParticipants = participants.size();
        Log.d("TAG", "initializeLosersRounds: " + totalParticipants);
        Log.d("TAG", "initializeLosersRounds: " + (int) Math.ceil((double) totalParticipants / 2));

        int requiredRounds = 2 * ((int) Math.ceil(Math.log((int) Math.ceil((double) totalParticipants / 2)) / Math.log(2)) - 1);
        for (int i = 0; i < requiredRounds; i++) {
            Round loserRound = new Round(i - 1, new ArrayList<>());
            int matchesInRound = calculateLosersMatchCount(i, (int) Math.ceil((double) totalParticipants / 2));
            for (int j = 0; j < matchesInRound; j++) {
                Match match = new Match();
                match.setMatchId("losers_" + i + "_" + j);
                match.setMatchNum(j);
                match.setCompetitor1(new Team(new Competitor("TBD"), new Competitor("TBD")));
                match.setCompetitor2(new Team(new Competitor("TBD"), new Competitor("TBD")));
                loserRound.getMatches().add(match);
            }
            losersRounds.add(loserRound);
        }
    }

    protected void initializeFinalRound() {

        Match finalMatch = new Match();
        finalMatch.setMatchId("final_match");
        finalMatch.setMatchNum(0);
        finalMatch.setCompetitor1(new Team(new Competitor("TBD"), new Competitor("TBD")));
        finalMatch.setCompetitor2(new Team(new Competitor("TBD"), new Competitor("TBD")));
        Log.d("TAG", "initializeFinalRound: " + rounds.size());
        Round Round = new Round(rounds.size() - 1, new ArrayList<>());
        Round.getMatches().add(finalMatch);
        this.rounds.add(Round);
    }
}