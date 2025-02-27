package com.example.finalProjectV1.classes;



// Match.java
public class Match {
    private String matchId;
    private int MatchNum;
    private Competitor competitor1;
    private Competitor competitor2;
    private boolean isComplete;
    private String winnerId;

    public Match() {
        // Required empty constructor for Firebase
    }


    public int getMatchNum() {
        return MatchNum;
    }

    public void setMatchNum(int matchNum) {
        MatchNum = matchNum;
    }

    public Match(String matchId, Competitor competitor1, Competitor competitor2, int MatchNum) {
        this.matchId = matchId;
        this.competitor1 = competitor1;
        this.competitor2 = competitor2;
        this.isComplete = false;
        this.MatchNum =MatchNum;
    }

    // Getters and setters
    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
    public Competitor getCompetitor1() { return competitor1; }
    public void setCompetitor1(Competitor competitor1) { this.competitor1 = competitor1; }
    public Competitor getCompetitor2() { return competitor2; }
    public void setCompetitor2(Competitor competitor2) { this.competitor2 = competitor2; }
    public boolean isComplete() { return isComplete; }
    public void setComplete(boolean complete) { isComplete = complete; }
    public String getWinnerId() { return winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }
}





    // Constructor, getters, setters
