package com.example.finalProjectV1.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.interfaces.GetTournamentLisiner;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FirebaseTournamentHelper {
    final DatabaseReference databaseReference = FirebaseManager.getInstance().getDatabase().getReference();


    private static final String TOURNAMENTS_PATH = "tournaments";

    public void saveTournament(Tournament tournament, OnAddToFirebase lisiner) {
        String tournamentId = tournament.getTournamentId();
        tournament.setTournamentId(tournamentId);
        // Save tournament data directly
        DatabaseReference tournamentRef = databaseReference.child(TOURNAMENTS_PATH).child(tournamentId);
        tournamentRef.setValue(tournament)
                .addOnSuccessListener(aVoid -> {lisiner.OnComplete();})
                .addOnFailureListener(e -> lisiner.onSearchError(e.getMessage()));
    }


    private void ensureCompetitorId(Competitor competitor) {
        if (competitor.getId() == null || competitor.getId().isEmpty()) {
            competitor.setId("COMP_" + UUID.randomUUID().toString().substring(0, 8));
        }
    }



    public void getTournamentFromId(String tournamentId, GetTournamentLisiner lisiner) {
        databaseReference.child(TOURNAMENTS_PATH)
                .child(tournamentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot tournamentSnapshot) {
                        String type = tournamentSnapshot.child("type").getValue(String.class);
                        Tournament tournament;
                        switch (type) {
                            case "Single Elimination":
                                tournament = tournamentSnapshot.getValue(SingleEliminationTournament.class);
                                break;
                            // Add more tournament types here
                            default:
                                tournament = tournamentSnapshot.getValue(SingleEliminationTournament.class);
                                break;

                        }
                        lisiner.onTournamentFound(tournament);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        lisiner.onError(error.getMessage());
                    }
                });
    }




    public void updateMatch(String tournamentId, int roundNumber, String matchId, Match match) {
        DatabaseReference matchRef = databaseReference
                .child(TOURNAMENTS_PATH)
                .child(tournamentId)
                .child("rounds")
                .child(String.valueOf(roundNumber))
                .child("matches")
                .child(matchId);

        matchRef.setValue(match);

    }


}


