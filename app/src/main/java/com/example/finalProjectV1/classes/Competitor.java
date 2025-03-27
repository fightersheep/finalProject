package com.example.finalProjectV1.classes;

import java.util.UUID;

public class Competitor extends ShortUser {
    private TennisScore score;
    private boolean isWinner;

    public Competitor(){super();
        this.score = new TennisScore();}
    public Competitor(String name) {
        super("COMP_" + UUID.randomUUID().toString().substring(0, 8),name);
        this.score = new TennisScore();
        this.isWinner = false;

    }
    public Competitor(String name,String id) {
        super(id,name);
        this.score = new TennisScore();
        this.isWinner = false;
    }

    public Competitor(Competitor competitor) {
        this.userId= competitor.userId;
        this.name = competitor.name;
        this.score = competitor.getScore();
        this.isWinner = competitor.isWinner;
    }

    public Competitor Pcopy(){
        Competitor copy = new Competitor(this.name,this.userId);

        return copy;
    }


    // Getters and setters


    public TennisScore getScore() { return score; }
    public void setScore(TennisScore score) { this.score = score; }
    public boolean isWinner() { return isWinner; }
    public void setWinner(boolean winner) { isWinner = winner; }
}

