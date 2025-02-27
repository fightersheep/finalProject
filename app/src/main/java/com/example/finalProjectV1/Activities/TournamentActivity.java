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
import com.example.finalProjectV1.classes.SingleEliminationTournament;
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
            Log.e("TAG", "onCreate: String data" );
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
            if (snapshot.child("admin").child("id").getValue(String.class).compareTo(dataManeger.getUser().getUserId())==0 && !snapshot.child("started").getValue(Boolean.class)){
                StartTournamentButton.setVisibility(View.VISIBLE);
            }
            String type = snapshot.child("type").getValue(String.class);
            Tournament tournament;
            switch (type) {
                case "Single Elimination":
                    tournament = snapshot.getValue(SingleEliminationTournament.class);
                    break;
                default:
                    tournament = snapshot.getValue(SingleEliminationTournament.class);
                    break;
            }

            Log.d("DatabaseUpdate", "Tournament updated: " + type);
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
