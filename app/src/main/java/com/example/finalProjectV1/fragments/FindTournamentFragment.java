package com.example.finalProjectV1.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalProjectV1.Activities.CreateTournamentActivity;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.adapters.TournamentAdapter;
import com.example.finalProjectV1.classes.Competitor;
import com.example.finalProjectV1.classes.Match;
import com.example.finalProjectV1.classes.Round;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.classes.SingleEliminationTournament;
import com.example.finalProjectV1.classes.Tournament;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindTournamentFragment extends Fragment {
    private RecyclerView recyclerView;
    private TournamentAdapter adapter;
    private List<Tournament> tournamentList;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabCreateTournament;
    private ChildEventListener childEventListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_tournament, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTournaments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fabCreateTournament = view.findViewById(R.id.fabCreateTournament);

        tournamentList = new ArrayList<>();
        adapter = new TournamentAdapter(tournamentList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getInstance().getReference("tournaments");
        attachDatabaseListener();

         setupCreateButton();

        return view;
    }

    private void setupCreateButton() {
        fabCreateTournament.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateTournamentActivity.class);
            startActivity(intent);
        });
    }
    private void attachDatabaseListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot snapshot, String previousChildName) {
                String type = snapshot.child("type").getValue(String.class);
                Tournament tournament;
                switch (type) {
                    case "Single Elimination":
                        tournament = snapshot.getValue(SingleEliminationTournament.class);

                        break;
                    // Add more tournament types here
                    default:
                        tournament = snapshot.getValue(SingleEliminationTournament.class);
                        break;
                }


                Log.d("onChildAdded", "onChildAdded: ");
                tournamentList.add(tournament);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, String previousChildName) {
                String key = snapshot.child("tournamentId").getValue(String.class);
                Tournament newValue = snapshot.getValue(SingleEliminationTournament.class);
                for (int i = 0; i < tournamentList.size(); i++) {
                    if (tournamentList.get(i).getTournamentId().equals(key)) {
                        tournamentList.get(i).SetNewTournament(newValue);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();
                for (int i = 0; i < tournamentList.size(); i++) {
                    if (tournamentList.get(i).getTournamentId().equals(key)) {
                        tournamentList.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }
    public void onDestroy() {
        super.onDestroy();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
    }

}
