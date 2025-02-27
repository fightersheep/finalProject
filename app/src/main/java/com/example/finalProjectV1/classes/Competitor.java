package com.example.finalProjectV1.classes;

import java.util.UUID;

public class Competitor extends ShortUser {
    private TennisScore score;
    private boolean isWinner;

    public Competitor(){super();}
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
    public Competitor copy(){
        Competitor copy = new Competitor(this.name,this.userId);
        copy.setScore(this.getScore());
        copy.setWinner(this.isWinner);
        return copy;
    }


    // Getters and setters


    public TennisScore getScore() { return score; }
    public void setScore(TennisScore score) { this.score = score; }
    public boolean isWinner() { return isWinner; }
    public void setWinner(boolean winner) { isWinner = winner; }
}

