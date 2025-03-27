package com.example.finalProjectV1.classes;


import java.util.ArrayList;
import java.util.List;

// Round.java
public class Round {
    private int roundNumber;
    private List<Match> matches;

    public Round() {
        this.matches = new ArrayList<>();
        // Required empty constructor for Firebase
    }

    public Round(int roundNumber, List<Match> matches) {
        this.roundNumber = roundNumber;
        this.matches = matches;
    }

    // Getters and setters
    public int getRoundNumber() { return roundNumber; }
    public void setRoundNumber(int roundNumber) { this.roundNumber = roundNumber; }
    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }
}


