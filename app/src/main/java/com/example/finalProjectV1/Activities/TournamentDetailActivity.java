package com.example.finalProjectV1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.example.finalProjectV1.util.CustomAlertDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TournamentDetailActivity extends AppCompatActivity {
    private TextView tournamentName, tournamentType, tournamentDate;
    private TextView tournamentLocation, participantsCount;
    private Button joinButton, observeButton;
    private String Startdate;
    private String location;
    private int maxParticipants;
    private String tournamentId;
    private String name;
    private String type;
    private int currentParticipants;
    private CustomAlertDialog dialog;
    private boolean Started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_detail);
        Intent get = getIntent();

        Startdate =get.getStringExtra("tournamentStartdate");
        location =get.getStringExtra("tournamentLocation");
        maxParticipants =get.getIntExtra("tournamentMax",0);
        tournamentId =get.getStringExtra("tournamentId");
        name =get.getStringExtra("tournamentName");
        type =get.getStringExtra("tournamenType");
        currentParticipants =get.getIntExtra("tournamentSize",0);
        Started =get.getBooleanExtra("Started",false);

        dialog = new CustomAlertDialog(this);

        initializeViews();
        setupTournamentData();
        setupButtons();
    }

    private void initializeViews() {
        tournamentName = findViewById(R.id.tournamentName);
        tournamentType = findViewById(R.id.tournamentType);
        tournamentDate = findViewById(R.id.tournamentDate);
        tournamentLocation = findViewById(R.id.tournamentLocation);
        participantsCount = findViewById(R.id.participantsCount);
        joinButton = findViewById(R.id.joinButton);
        observeButton = findViewById(R.id.observeButton);
    }

    private void setupTournamentData() {
        tournamentName.setText(name);
        tournamentType.setText("Type: " + type);
        tournamentDate.setText("Start Date: " + Startdate);
        tournamentLocation.setText("Location: " + location);
        participantsCount.setText(String.format("Participants: %d/%d",
                currentParticipants,  maxParticipants));
    }

    private void setupButtons() {
        joinButton.setOnClickListener(v -> {
            if (currentParticipants < maxParticipants) {
                joinTournament();
            } else {
                Toast.makeText(this, "Tournament is full!", Toast.LENGTH_SHORT).show();
            }
        });

        observeButton.setOnClickListener(v -> {
            observeTournament();
        });
    }

    private void joinTournament() {
        DatabaseReference tournamentRef = FirebaseManager.getInstance()
                .getDatabase()
                .getReference()
                .child("tournaments")
                .child(tournamentId)
                .child("participants");

        tournamentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ShortUser> participants = new ArrayList<>();

                // Convert existing participants to list if they exist
                if (task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        participants.add(snapshot.getValue(ShortUser.class));
                        if (snapshot.getValue(ShortUser.class).getId().compareTo(dataManeger.getUser().getId())==0){
                            dialog.showTextOnlyDialog("already in", "you already joined the tournament");
                            return;
                        }
                    }
                }

                // Create and add new participant
                ShortUser newParticipant = new ShortUser(
                        dataManeger.getUser().getUserId(),
                        dataManeger.getUser().getName()
                );
                participants.add(newParticipant);

                // Update Firebase with new list
                FirebaseManager.AddGeneric(tournamentRef, participants, new OnAddToFirebase() {
                    @Override
                    public void OnComplete() {
                        dialog.showTextOnlyDialog("Joined Tournament", "Thank you for joining the tournament");
                        participantsCount.setText(String.format("Participants: %d/%d",
                                currentParticipants+1,  maxParticipants));
                    }

                    @Override
                    public void onSearchError(String error) {
                        dialog.showTextOnlyDialog("Error", error);
                    }
                });
            }
        });
    }


    private void observeTournament() {
        Intent intent = new Intent(this,TournamentActivity.class);
        intent.putExtra("tournamentId",tournamentId);
        startActivity(intent);

    }
}
