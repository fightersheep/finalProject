package com.example.finalProjectV1.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.adapters.TournamentPagerAdapter;
import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.classes.SingleEliminationTeams;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Team;
import com.example.finalProjectV1.classes.TennisScore;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.firebase.interfaces.GetTournamentLisiner;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.example.finalProjectV1.util.CustomAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ventura.bracketslib.BracketsView;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TournamentActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 tournamentPager;
    private ValueEventListener valueEventListener;
    private TournamentPagerAdapter pagerAdapter;
    private FirebaseTournamentHelper tournamentHelper;
    private DatabaseReference databaseReference;
    private Button StartTournamentButton;
    private boolean isFirstLoadComplete = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        tournamentPager = findViewById(R.id.tournamentPager);
        pagerAdapter = new TournamentPagerAdapter(this);
        tournamentPager.setAdapter(pagerAdapter);
        tournamentHelper = new FirebaseTournamentHelper();
        String stringData = getIntent().getStringExtra("tournamentId");
        if(stringData != null){
            databaseReference = FirebaseManager.getInstance().getDatabase().getReference().child("tournaments").child(stringData);
            attachDatabaseListener();
        }


        StartTournamentButton= findViewById(R.id.StartTournamentButton);
        StartTournamentButton.setOnClickListener(this);

    }
    private void attachDatabaseListener(){
        valueEventListener = new ValueEventListener() {
            @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Id =snapshot.child("admin").child("id").getValue(String.class);
                if (Id.compareTo(dataManeger.getUser().getUserId()) == 0 && !snapshot.child("started").getValue(Boolean.class)) {
                    StartTournamentButton.setVisibility(View.VISIBLE);
                }
                boolean doubles = snapshot.child("doubles").getValue(boolean.class);
                Tournament tournament;
                if (!doubles) {
                    tournament = snapshot.getValue(SingleEliminationTournament.class);
                } else {
                    tournament = new SingleEliminationTeams();

                    tournament.setTournamentId(snapshot.child("tournamentId").getValue(String.class));
                    tournament.setName(snapshot.child("name").getValue(String.class));
                    tournament.setStartdate(snapshot.child("startdate").getValue(String.class));
                    tournament.setLocation(snapshot.child("location").getValue(String.class));
                    tournament.setMaxParticipants(snapshot.child("maxParticipants").getValue(Integer.class));
                    tournament.setStarted(snapshot.child("started").getValue(Boolean.class));
                    tournament.setDoubles(snapshot.child("doubles").getValue(Boolean.class));


                    // Get admin
                    DataSnapshot adminSnapshot = snapshot.child("admin");
                    if (adminSnapshot.exists()) {
                        ShortUser admin = new ShortUser();
                        admin.setId(adminSnapshot.child("id").getValue(String.class));
                        admin.setName(adminSnapshot.child("name").getValue(String.class));
                        tournament.setAdmin(admin);
                    }

                    // Get participants
                    tournament.setParticipants(new ArrayList<>());
                    DataSnapshot participantsSnapshot = snapshot.child("participants");
                    for (DataSnapshot participantSnapshot : participantsSnapshot.getChildren()) {
                        ShortUser participant = new ShortUser();
                        participant.setId(participantSnapshot.child("id").getValue(String.class));
                        participant.setName(participantSnapshot.child("name").getValue(String.class));
                        tournament.getParticipants().add(participant);
                    }

                    // Get rounds and matches with teams as competitors
                    tournament.setRounds(new ArrayList<>());
                    DataSnapshot roundsSnapshot = snapshot.child("rounds");
                    for (DataSnapshot roundSnapshot : roundsSnapshot.getChildren()) {
                        Round round = new Round();
                        round.setRoundNumber(roundSnapshot.child("roundNumber").getValue(Integer.class));

                        List<Match> matches = new ArrayList<>();
                        DataSnapshot matchesSnapshot = roundSnapshot.child("matches");
                        for (DataSnapshot matchSnapshot : matchesSnapshot.getChildren()) {
                            Match match = matchSnapshot.getValue(Match.class);
                            // Create Team objects for competitors
                            DataSnapshot comp1Snapshot = matchSnapshot.child("competitor1");
                            match.setCompetitor1(comp1Snapshot.getValue(Team.class));
                            // Do the same for competitor2
                            DataSnapshot comp2Snapshot = matchSnapshot.child("competitor2");
                            match.setCompetitor2(comp2Snapshot.getValue(Team.class));
                            matches.add(match);
                        }
                        round.setMatches(matches);
                        tournament.getRounds().add(round);
                    }
                }
                pagerAdapter.setTournament(tournament);
                if (!isFirstLoadComplete) {
                    isFirstLoadComplete = true;
                    // Do your one-time actions here
                    pagerAdapter.initializeTournament();
                }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("DatabaseError", "Error: " + error.getMessage());
        }
    };

    databaseReference.addValueEventListener(valueEventListener);

    }
    public void onDestroy() {
        super.onDestroy();
        if (valueEventListener  != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
    @Override
    public void onClick(View v) {
        if (v== StartTournamentButton){
            databaseReference.child("started").setValue(true);
            StartTournamentButton.setVisibility(View.GONE);

        }
    }
}
