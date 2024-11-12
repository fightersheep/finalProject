package com.example.finalProjectV1;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {
    private static FirebaseManager instance;
    private static FirebaseDatabase database;

    private FirebaseManager() {
        // Initialize the database with your specific URL
        database = FirebaseDatabase.getInstance("https://finalprojectv1-762fe-default-rtdb.europe-west1.firebasedatabase.app");
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }
    public static void searchUsers(String searchQuery, OnSearchResultListener listener) {
        DatabaseReference usersRef = getInstance().getDatabase().getReference("users");
        usersRef.keepSynced(true);

        usersRef.orderByChild("name")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff")
                .limitToFirst(20)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<User> usersList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                usersList.add(user);
                            }
                        }
                        listener.onSearchComplete(usersList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onSearchError(error.getMessage());
                    }
                });
    }
    public static void searchUserWithDiscriminator(String username, String discriminator, OnSearchResultListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.keepSynced(true);

        usersRef.orderByChild("name")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<User> usersList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null && (discriminator.isEmpty() || user.getDiscriminator().compareTo(discriminator)==0)) {
                                usersList.add(user);
                            }
                        }
                        listener.onSearchComplete(usersList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onSearchError(error.getMessage());
                    }
                });
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }
}
