package com.example.finalProjectV1.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TennisScore {
    private static final List<String> POINT_SCORES = Arrays.asList("0", "15", "30", "40", "AD");
    private int sets;
    private final int setsToWin;
    private int currentSet;
    private int games;
    private int points;

    public TennisScore() {

        this.sets = 0;
        this.currentSet = 0;
        this.games = 0;
        this.points = 0;
        this.setsToWin= 3;
    }
    public TennisScore(int numberOfSets) {

        this.sets = 0;
        this.currentSet = 0;
        this.games = 0;
        this.points = 0;
        this.setsToWin= (numberOfSets / 2) + 1;
    }

    public String getCurrentGameScore() {
        return points >= POINT_SCORES.size() ? "AD" : POINT_SCORES.get(points);
    }

    public void addPoint() {
        points++;
    }

    public void removeAdvantage() {
        if (points > 3) points = 3;
    }

    public void winGame() {
        games++;
        points = 0;
        if (games >= 7) {
            sets++;
            currentSet++;
            games = 0;
        }
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public void setCurrentSet(int currentSet) {
        this.currentSet = currentSet;
    }

    public boolean hasWonMatch() {

        return setsToWin<=sets;
    }
}
