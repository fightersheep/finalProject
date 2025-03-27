package com.example.finalProjectV1.classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SingleEliminationTeams extends SingleEliminationTournament {


    @Override
    public void initializeTournament() {
            Log.d("TAG", "onDataChange2: "+ this.admin.name);

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

            // Create first round matches
            Round firstRound = new Round(0, new ArrayList<>());
            for (int i = 0; i < totalParticipants; i += 4) {
                Match match = new Match();
                match.setMatchId("match_0_" + (i / 2));
                match.setMatchNum(i / 2);

                // Set first competitor

                // Set second competitor if available, otherwise TBD
                if (i + 1 < totalParticipants) {
                    match.setCompetitor1(new Team(competitors.get(i),competitors.get(i+1)));
                } else{
                    match.setCompetitor1(new Team(competitors.get(i),new Competitor("TBD")));
                }
                if (i + 2 < totalParticipants) {
                    if (i + 3 < totalParticipants) {
                        match.setCompetitor2(new Team(competitors.get(i + 2), competitors.get(i + 3)));
                    } else {
                        match.setCompetitor2(new Team(competitors.get(i), new Competitor("TBD")));
                    }
                }
                else{
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
                    match.setCompetitor1(new Team(new Competitor("TBD"),new Competitor("TBD")));
                    match.setCompetitor2(new Team(new Competitor("TBD"),new Competitor("TBD")));
                    newRound.getMatches().add(match);
                }

                rounds.add(newRound);
            }

        Log.d("TAG", "onDataChange3: "+ this.type);


    }
    public void MoveParticipantOn(int NewRound, int matchNum, Competitor winner) {
        Log.d("TAG", "MoveParticipantOn: "+matchNum);
        Match next = rounds.get(NewRound).getMatches().get(matchNum/2);
        Team Winner2 = new Team();
        Winner2.name=winner.name;
        Winner2.userId = winner.userId;
        Winner2.competitor2 = ((Team)winner).competitor2;
        if (next.getCompetitor1().getName().compareTo("TBD")==0){
            next.setCompetitor1(Winner2);
        }
        else{
            next.setCompetitor2(Winner2);
        }

    }
}
