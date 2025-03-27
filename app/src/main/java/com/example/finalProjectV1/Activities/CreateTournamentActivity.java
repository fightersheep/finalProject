package com.example.finalProjectV1.Activities;

import static com.google.common.math.IntMath.isPowerOfTwo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.TournamentType;
import com.example.finalProjectV1.classes.DoubleEliminationTournament;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Tournament;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.dataManeger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateTournamentActivity extends AppCompatActivity {
    private EditText editTournamentName;
    private Spinner spinnerTournamentType;
    private EditText editTournamentDateTime;
    private EditText editTournamentLocation;
    private EditText editMaxParticipants;
    private Button buttonCreate;
    private DatabaseReference databaseReference;
    private EditText editMinPlayers;
    private EditText editRounds;
    private Tournament tournament;
    private Calendar selectedDateTime;
    private Switch isDoubles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        editTournamentName = findViewById(R.id.editTournamentName);
        spinnerTournamentType = findViewById(R.id.spinnerTournamentType);
        editTournamentDateTime = findViewById(R.id.editTournamentDateTime);
        editTournamentLocation = findViewById(R.id.editTournamentLocation);
        editMaxParticipants = findViewById(R.id.editMaxParticipants);
        buttonCreate = findViewById(R.id.buttonCreate);
        editMinPlayers = findViewById(R.id.editMinPlayers);
        editRounds = findViewById(R.id.editRounds);
        selectedDateTime = Calendar.getInstance();
        isDoubles = findViewById(R.id.isDoubles);
        editTournamentDateTime.setFocusable(false);
        editTournamentDateTime.setClickable(true);
        editTournamentDateTime.setOnClickListener(v -> showDateTimePicker());
        spinnerTournamentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                configureTournamentTypeFields(TournamentType.fromString(selectedType));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        setupTournamentTypeSpinner();

        FirebaseManager.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("tournaments");
        buttonCreate.setOnClickListener(v -> createTournament());
    }

    private void setupTournamentTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tournament_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTournamentType.setAdapter(adapter);
    }
    private void configureTournamentTypeFields(TournamentType type) {
        switch (type) {
            case SINGLE_ELIMINATION:
                editMinPlayers.setVisibility(View.GONE);
                editRounds.setVisibility(View.GONE);
                editMinPlayers.setHint("Minimum Players (Must be power of 2)");
                tournament = new SingleEliminationTournament();
                break;

            case DOUBLE_ELIMINATION:
                editMinPlayers.setVisibility(View.VISIBLE);
                editRounds.setVisibility(View.GONE);
                editMinPlayers.setHint("Minimum Players (Must be power of 2)");
                tournament = new DoubleEliminationTournament();
                break;

            case ROUND_ROBIN:
                editMinPlayers.setVisibility(View.VISIBLE);
                editRounds.setVisibility(View.GONE);
                editMinPlayers.setHint("Minimum Players (At least 3)");
                break;

            case SWISS_SYSTEM:
                editMinPlayers.setVisibility(View.VISIBLE);
                editRounds.setVisibility(View.VISIBLE);
                editMinPlayers.setHint("Minimum Players (At least 4)");
                editRounds.setHint("Number of Rounds");
                break;

            case LEAGUE:
                editMinPlayers.setVisibility(View.VISIBLE);
                editRounds.setVisibility(View.VISIBLE);
                editMinPlayers.setHint("Number of Teams");
                editRounds.setHint("Rounds per Team");
                break;
        }
    }
    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // After date is selected, show time picker
                    showTimePicker();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        editTournamentDateTime.setText(sdf.format(selectedDateTime.getTime()));
    }

    private boolean validateTournamentSettings(TournamentType type, int minPlayers) {
        switch (type) {
            case SINGLE_ELIMINATION:
            case DOUBLE_ELIMINATION:

            case ROUND_ROBIN:
                return minPlayers >= 3;

            case SWISS_SYSTEM:
                return minPlayers >= 4;

            case LEAGUE:
                return minPlayers >= 2;

            default:
                return true;
        }
    }
    private void createTournament() {
        String name = editTournamentName.getText().toString().trim();
        TournamentType type = TournamentType.fromString(spinnerTournamentType.getSelectedItem().toString());
        //int minPlayers = Integer.parseInt(editMinPlayers.getText().toString().trim());

       // if (!validateTournamentSettings(type, minPlayers)) {
         //   Toast.makeText(this, "Invalid settings for selected tournament type", Toast.LENGTH_SHORT).show();
         //   return;
       // }

        // Create tournament object with additional parameters based on type
        tournament.setTournamentId(databaseReference.push().getKey());
        tournament.setName(name);
        tournament.setType(type.getDisplayName());
        tournament.setLocation(editTournamentLocation.getText().toString());
        tournament.setMaxParticipants(Integer.parseInt(editMaxParticipants.getText().toString()));
        tournament.setStartdate(editTournamentDateTime.getText().toString());
        tournament.setAdmin(new ShortUser(dataManeger.getUser()));
        tournament.setStarted(false);
        tournament.setDoubles(isDoubles.isChecked());


        // Save to Firebase
        databaseReference.child(tournament.getTournamentId()).setValue(tournament)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Tournament created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create tournament", Toast.LENGTH_SHORT).show());
    }


}
