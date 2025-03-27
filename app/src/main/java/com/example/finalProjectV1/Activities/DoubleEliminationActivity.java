package com.example.finalProjectV1.Activities;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.adapters.DoubleEliminationPagerAdapter;
import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.DoubleEliminationTeams;
import com.example.finalProjectV1.classes.DoubleEliminationTournament;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.classes.SingleEliminationTeams;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Team;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.FirebaseTournamentHelper;
import com.example.finalProjectV1.firebase.dataManeger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoubleEliminationActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 winnersViewPager;
    private ViewPager2 losersViewPager;
    private DoubleEliminationPagerAdapter winnersAdapter;
    private DoubleEliminationPagerAdapter losersAdapter;
    private ValueEventListener valueEventListener;
    private FirebaseTournamentHelper tournamentHelper;
    private DatabaseReference databaseReference;
    private Button StartTournamentButton;
    private boolean isFirstLoadComplete = false;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_elimination);
        isFirstLoadComplete = false;
        initializeViews();
        setupAdapters();
        setupFirebase();
    }



    private void initializeViews() {
        winnersViewPager = findViewById(R.id.winners_view_pager);
        losersViewPager = findViewById(R.id.losers_view_pager);
        StartTournamentButton = findViewById(R.id.StartTournamentButton);
        StartTournamentButton.setOnClickListener(this);
    }

    private void setupAdapters() {
        winnersAdapter = new DoubleEliminationPagerAdapter(this, true);
        losersAdapter = new DoubleEliminationPagerAdapter(this, false);
        winnersViewPager.setAdapter(winnersAdapter);
        losersViewPager.setAdapter(losersAdapter);
    }

    private void setupFirebase() {
        tournamentHelper = new FirebaseTournamentHelper();
        String tournamentId = getIntent().getStringExtra("tournamentId");
        if (tournamentId != null) {
            databaseReference = FirebaseManager.getInstance().getDatabase()
                    .getReference().child("tournaments").child(tournamentId);
            attachDatabaseListener();
        }
    }

    private void attachDatabaseListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "onDataChange: ");
                handleTournamentUpdate(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error: " + error.getMessage());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void handleTournamentUpdate(DataSnapshot snapshot) {
        String adminId = snapshot.child("admin").child("id").getValue(String.class);
        boolean isStarted = snapshot.child("started").getValue(Boolean.class);

        if (adminId.equals(dataManeger.getUser().getUserId()) && !isStarted) {
            StartTournamentButton.setVisibility(View.VISIBLE);
        }


        boolean doubles = snapshot.child("doubles").getValue(boolean.class);
        Tournament tournament;
        if (!doubles) {
            tournament = snapshot.getValue(DoubleEliminationTournament.class);
        } else {
            tournament = new DoubleEliminationTeams();

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
                ((DoubleEliminationTournament) tournament).setLosersRounds(new ArrayList<>());
                DataSnapshot LoserRoundsSnapshot = snapshot.child("losersRounds");
                for (DataSnapshot LoserRoundSnapshot : LoserRoundsSnapshot.getChildren()) {
                    Round LoserRound = new Round();
                    LoserRound.setRoundNumber(LoserRoundSnapshot.child("roundNumber").getValue(Integer.class));

                    List<Match> Losermatches = new ArrayList<>();
                    DataSnapshot LosermatchesSnapshot = LoserRoundSnapshot.child("matches");
                    for (DataSnapshot LosermatchSnapshot : LosermatchesSnapshot.getChildren()) {
                        Match match = LosermatchSnapshot.getValue(Match.class);
                        // Create Team objects for competitors
                        DataSnapshot comp1Snapshot = LosermatchSnapshot.child("competitor1");
                        match.setCompetitor1(comp1Snapshot.getValue(Team.class));
                        // Do the same for competitor2
                        DataSnapshot comp2Snapshot = LosermatchSnapshot.child("competitor2");
                        match.setCompetitor2(comp2Snapshot.getValue(Team.class));
                        Losermatches.add(match);
                    }

                    LoserRound.setMatches(Losermatches);
                    ((DoubleEliminationTournament) tournament).getLosersRounds().add(LoserRound);
                }
            }
            winnersAdapter.setTournament(tournament);
            losersAdapter.setTournament(tournament);

            if (!isFirstLoadComplete) {
                Log.d("TAG", "handleTournamentUpdate: ");
                isFirstLoadComplete = true;
                winnersAdapter.initializeTournament();
            }
        }


    @Override
    public void onClick(View v) {
        if (v == StartTournamentButton) {
            databaseReference.child("started").setValue(true);
            StartTournamentButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}