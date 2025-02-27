package com.example.finalProjectV1.classes;

import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;

import java.util.ArrayList;
import java.util.List;

// Tournament.java
    public abstract class Tournament {
    protected String tournamentId;
    protected String name;
    protected String type;
    protected List<Round> rounds;
    protected List<ShortUser> participants;
    protected String Startdate;
    protected String location;
    protected int maxParticipants;
    protected boolean started;
    protected ShortUser admin;

    public ShortUser getAdmin() {
        return admin;
    }

    public void setAdmin(ShortUser admin) {
        this.admin = admin;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Tournament() {
        this.rounds = new ArrayList<>();
        this.participants = new ArrayList<>();


    }


    public Tournament(String tournamentId, int maxParticipants, String location, String startdate, List<ShortUser> participants, List<Round> rounds, String type, String name) {
        this.tournamentId = tournamentId;
        this.maxParticipants = maxParticipants;
        this.location = location;
        Startdate = startdate;
        this.participants = participants;
        this.rounds = rounds;
        this.type = type;
        this.name = name;
        this.started =false;
    }

    public Tournament(String id, String name, String type, String date, String location, int maxParticipants) {
        this.tournamentId = id;
        this.name = name;
        this.type = type;
        this.Startdate = date;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.started =false;

    }

    // Getters and setters
    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<ShortUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ShortUser> participants) {
        this.participants = participants;
    }

    public String getStartdate() {
        return Startdate;
    }

    public void setStartdate(String startdate) {
        Startdate = startdate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addCompetitor(Competitor competitor) {
    }


    public void MoveParticipantOn(int NewRound, int matchNum, Competitor winner) {

    }

    public void initializeTournament(OnAddToFirebase lisiner){};

    public void SetNewTournament(Tournament tournament){
        this.tournamentId = tournament.tournamentId;
        this.maxParticipants = tournament.maxParticipants;
        this.location = tournament.getLocation();
        this.Startdate = tournament.Startdate;
        this.participants = tournament.getParticipants();
        this.rounds = tournament.rounds;
        this.type = tournament.type;
        this.name = tournament.name;
        this.started =tournament.isStarted();
    }
}
    // Getters and setters
