package com.example.finalProjectV1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HomeFragment extends Fragment {

    private TextView tvName, tvEmail, tvLocation, tvDateOfBirth, tvExperience, tvGender, tvCountry;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth);
        tvExperience = view.findViewById(R.id.tvExperience);
        tvGender = view.findViewById(R.id.tvGender);
        tvCountry = view.findViewById(R.id.tvCountry);

        loadUserData();

        return view;
    }
    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String location = dataSnapshot.child("location").getValue(String.class);
                        String dateOfBirth = dataSnapshot.child("dateOfBirth").getValue(String.class);
                        String experience = dataSnapshot.child("experience").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String country = dataSnapshot.child("country").getValue(String.class);

                        tvName.setText(String.format("Name: %s %s", firstName, lastName));
                        tvEmail.setText(String.format("Email: %s", currentUser.getEmail()));
                        tvLocation.setText(String.format("Location: %s", location));
                        tvDateOfBirth.setText(String.format("Date of Birth: %s", dateOfBirth));
                        tvExperience.setText(String.format("Tennis Experience: %s", experience));
                        tvGender.setText(String.format("Gender: %s", gender));
                        tvCountry.setText(String.format("Country: %s", country));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}


