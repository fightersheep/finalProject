package com.example.finalProjectV1.classes;

import static com.example.finalProjectV1.TournamentType.SINGLE_ELIMINATION;

import android.util.Log;

import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// SingleEliminationTournament.java
public class SingleEliminationTournament extends Tournament {
    protected List<Round> rounds;
    public SingleEliminationTournament() {
        super();
        this.rounds = new ArrayList<>();
        this.type = "Single Elimination";
    }

    public SingleEliminationTournament(String name, List<Round> rounds) {
        this();
        this.name = name;
        this.rounds = rounds;
        this.type = "Single Elimination";
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public SingleEliminationTournament(String tournamentId, int maxParticipants, String location, String startdate, List<ShortUser> participants, List<Round> rounds, String type, String name) {
        super(tournamentId, maxParticipants, location, startdate, participants, rounds, type, name);
    }




    public void addCompetitor(Competitor competitor) {
        // Ensure competitor has an ID
        if (competitor.getId() == null || competitor.getId().isEmpty()) {
            competitor.setId("COMP_" + UUID.randomUUID().toString().substring(0, 8));
        }

        // Initialize first round if empty
        if (rounds.isEmpty()) {
            rounds.add(new Round(0, new ArrayList<>()));
        }



        Round firstRound = rounds.get(0);
        List<Match> matches = firstRound.getMatches();

        // Create new match or add to existing incomplete match
        if (matches.isEmpty() || matches.get(matches.size() - 1).getCompetitor2() != null) {
            Match newMatch = new Match();
            newMatch.setMatchNum(matches.size());
            newMatch.setMatchId("match_0_" + matches.size());
            newMatch.setCompetitor1(competitor);
            matches.add(newMatch);
        } else {
            matches.get(matches.size() - 1).setCompetitor2(competitor);
        }

        // Update tournament bracket structure
        updateBracket();
    }

    private void updateBracket() {
        int currentRoundSize = rounds.get(0).getMatches().size() * 2;
        int requiredRounds = calculateRequiredRounds(currentRoundSize);

        for (int roundIndex = 1; roundIndex < requiredRounds; roundIndex++) {
            if (rounds.size()-1<roundIndex) {
                Round NewRound = new Round(roundIndex, new ArrayList<>());
                Log.d("createRound", "updateBracket: "+roundIndex);
                rounds.add(NewRound);
            }
            updateRound(roundIndex, currentRoundSize, rounds.get(roundIndex));

        }
    }

    private int calculateRequiredRounds(int competitors) {
        return (int) Math.ceil(Math.log(competitors) / Math.log(2));
    }

    private void updateRound(int roundIndex, int initialCompetitors,Round round) {
        int matchesInRound = (int) Math.ceil(initialCompetitors / Math.pow(2, roundIndex+1));
        Log.d("createRound", "sa:"+ Math.pow(2, roundIndex));
        Log.d("createRound", "oa:"+ roundIndex);

        Log.d("createRound", "sa:"+ initialCompetitors);

        for (int i = round.getMatches().size(); i < matchesInRound; i++) {
            Match match = createMatch(roundIndex, i);
            Log.d("createRound", "sa:"+ roundIndex);
            Log.d("createRound", "createRound:"+ matchesInRound);
            round.getMatches().add(match);
        }

    }

    private Match createMatch(int roundIndex, int matchNumber) {
        Match match = new Match();
        match.setMatchNum(matchNumber);
        match.setMatchId(String.format("match_%d_%d", roundIndex, matchNumber));
        match.setCompetitor1(new Competitor("TBD"));
        match.setCompetitor2(new Competitor("TBD"));
        return match;
    }

    public void MoveParticipantOn(int NewRound, int matchNum, Competitor winner) {
        Log.d("TAG", "1MoveParticipantOn: "+matchNum/2);
        Log.d("TAG", "1MoveParticipantOn: "+matchNum);

        Match next = rounds.get(NewRound).getMatches().get(matchNum/2);
        if (next.getCompetitor1().getName().compareTo("TBD")==0){
            next.setCompetitor1(winner.Pcopy());
        }
        else{
            next.setCompetitor2(winner.Pcopy());
        }

    }

    public void initializeTournament() {
        // Clear existing rounds
        this.rounds = new ArrayList<>();
        Log.d("TAG", "initializeTournament: ");
        // Create competitors from participants
        List<Competitor> competitors = new ArrayList<>();
        for (ShortUser participant : participants) {
            competitors.add(new Competitor(participant.getName(), participant.getId()));
        }

        // Calculate number of rounds needed
        int totalParticipants = competitors.size();
        int requiredRounds = (int) Math.ceil(Math.log(totalParticipants) / Math.log(2));

        // Create first round matches
        Round firstRound = new Round(0, new ArrayList<>());
        for (int i = 0; i < totalParticipants; i += 2) {
            Match match = new Match();
            match.setMatchId("match_0_" + (i / 2));
            match.setMatchNum(i / 2);

            // Set first competitor
            match.setCompetitor1(competitors.get(i));

            // Set second competitor if available, otherwise TBD
            if (i + 1 < totalParticipants) {
                match.setCompetitor2(competitors.get(i + 1));
            } else {
                match.setCompetitor2(new Competitor("TBD"));
            }

            firstRound.getMatches().add(match);
        }
        rounds.add(firstRound);

        // Create subsequent rounds
        for (int roundIndex = 1; roundIndex < requiredRounds; roundIndex++) {
            Round newRound = new Round(roundIndex, new ArrayList<>());
            int matchesInRound = (int) Math.ceil(totalParticipants / Math.pow(2, roundIndex + 1));

            for (int matchIndex = 0; matchIndex < matchesInRound; matchIndex++) {
                Match match = new Match();
                match.setMatchId(String.format("match_%d_%d", roundIndex, matchIndex));
                match.setMatchNum(matchIndex);
                match.setCompetitor1(new Competitor("TBD"));
                match.setCompetitor2(new Competitor("TBD"));
                newRound.getMatches().add(match);
            }

            rounds.add(newRound);
        }

    }




}