package com.example.finalProjectV1.classes;

import android.util.Log;

import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import java.util.ArrayList;
import java.util.List;

public class DoubleEliminationTournament extends SingleEliminationTournament {
    protected List<Round> losersRounds;

    public DoubleEliminationTournament() {
        super();
        this.type = "Double Elimination";
        this.losersRounds = new ArrayList<>();
    }

    public DoubleEliminationTournament(String tournamentId, int maxParticipants, String location,
                                       String startdate, List<ShortUser> participants, List<Round> rounds, String type, String name) {
        super(tournamentId, maxParticipants, location, startdate, participants, rounds, type, name);
        this.losersRounds = new ArrayList<>();

    }

    public void initializeTournament() {
        // First initialize winners bracket using parent class method
        super.initializeTournament();
        initializeLosersRounds();
        initializeFinalRound();
        Log.d("TAG", "initializeTournament: "+ this.rounds.size());
    }




    private void initializeLosersRounds() {
        this.losersRounds = new ArrayList<>();

        int totalParticipants = participants.size();
        int requiredRounds =2*((int) Math.ceil(Math.log(totalParticipants) / Math.log(2)) - 1);
        for (int i = 0; i < requiredRounds; i++) {
            Round loserRound = new Round(i-1, new ArrayList<>());
            int matchesInRound = calculateLosersMatchCount(i, totalParticipants);

            for (int j = 0; j < matchesInRound; j++) {
                Match match = new Match();
                match.setMatchId("losers_" + i + "_" + j);
                match.setMatchNum(j);
                match.setCompetitor1(new Competitor("TBD"));
                match.setCompetitor2(new Competitor("TBD"));
                loserRound.getMatches().add(match);
            }
            losersRounds.add(loserRound);
        }
    }
    // Add methods to handle moving to final match


    // Getter for final match

    // Setter for final match
    protected void initializeFinalRound(){

        Match finalMatch = new Match();
        finalMatch.setMatchId("final_match");
        finalMatch.setMatchNum(0);
        finalMatch.setCompetitor1(new Competitor("TBD"));
        finalMatch.setCompetitor2(new Competitor("TBD"));
        Log.d("TAG", "initializeFinalRound: "+rounds.size());
        Round Round = new Round(rounds.size() -1, new ArrayList<>());
        Round.getMatches().add(finalMatch);
        this.rounds.add(Round);
    }

    protected int calculateLosersMatchCount(int roundIndex, int totalParticipants) {
        if (roundIndex == 0) {
            Log.d("TAG", "1calculateLosersMatchCount-1: " +totalParticipants);
            Log.d("TAG", "1calculateLosersMatchCount: " +(int) Math.ceil(totalParticipants/4));
            return (int) Math.ceil((double) totalParticipants/4);
        } else if (roundIndex % 2 == 0) {

            return losersRounds.get(roundIndex - 1).getMatches().size() / 2;
        } else {
            Log.d("TAG", "3calculateLosersMatchCount: "+losersRounds.get(roundIndex - 1).getMatches().size());

            return losersRounds.get(roundIndex-1).getMatches().size();
        }
    }

    public void moveToNextLosersRound(int currentStage, int matchNum, Competitor winner) {
        // If this is the last match in losers bracket
        if (currentStage == losersRounds.size() - 1) {
            rounds.get(rounds.size()-1).getMatches().get(0).setCompetitor2(winner.Pcopy());
        } else {
            Round nextRound = losersRounds.get(currentStage + 1);
            Match nextMatch = nextRound.getMatches().get(matchNum / 2);

            if (nextMatch.getCompetitor1().getName().equals("TBD")) {
                nextMatch.setCompetitor1(winner.Pcopy());
            } else {
                nextMatch.setCompetitor2(winner.Pcopy());
            }
        }
    }


    public List<Round> getLosersRounds() {
        return losersRounds;
    }
    public List<Round> getRounds() {
        return super.getRounds();
        // Create a special round for the final match if needed
    }
    public void setLosersRounds(List<Round> losersRounds) {
        this.losersRounds = losersRounds;
    }
    public void moveToLosersBracket(int currentStage, int matchNum, Competitor loser) {
        loser.setName(loser.getName()+"(L)");
        loser.setScore(new TennisScore());
        if (currentStage == 0) {
            Match match = losersRounds.get(0).getMatches().get(matchNum / 2);
            Log.d("TAG", "moveToLosersBracket: "+match.getMatchNum());

            if (match.getCompetitor1().getName().equals("TBD")) {
                match.setCompetitor1(loser);
            } else {
                match.setCompetitor2(loser);
            }
        } else {
            Round round = losersRounds.get(2 * currentStage - 1);
            Match match;
            if (round.getMatches().size() / 2 == matchNum || round.getMatches().size() / 2 - 1 == matchNum) {
                match = round.getMatches().get((round.getMatches().size() - (matchNum+1)));
                Log.d("TAG", "moveToLosersBracket1: "+ match.getMatchNum());


            } else {
                match = round.getMatches().get((round.getMatches().size() - matchNum)-1);
                Log.d("TAG", "moveToLosersBracket2: "+ match.getMatchNum());
            }
            Log.d("TAG", "moveToLosersBracket: "+ match.getMatchNum());
            if (match.getCompetitor1().getName().equals("TBD")) {
                match.setCompetitor1(loser);
            } else {
                match.setCompetitor2(loser);
            }
        }
    }
}