package com.example.finalProjectV1.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.example.finalProjectV1.firebase.interfaces.OnFriendListListener;
import com.example.finalProjectV1.firebase.interfaces.OnSearchResultListener;
import com.example.finalProjectV1.firebase.interfaces.OnUserResultListener;
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
        if (database == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            database = FirebaseDatabase.getInstance("https://finalprojectv1-762fe-default-rtdb.europe-west1.firebasedatabase.app");
        }
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
                        List<FullUser> usersList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FullUser fullUser = snapshot.getValue(FullUser.class);
                            if (fullUser != null) {
                                usersList.add(fullUser);

                            }
                        }
                        for (int i = 0; i < usersList.size(); i++) {
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
                        List<FullUser> usersList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FullUser fullUser = snapshot.getValue(FullUser.class);
                            if (fullUser != null && (discriminator.isEmpty() || fullUser.getDiscriminator().compareTo(discriminator)==0)) {
                                usersList.add(fullUser);
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
    public static <T> void AddGeneric(DatabaseReference Reference, T ClassToAdd, OnAddToFirebase listener) {
        Reference.setValue(ClassToAdd).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Handle success
                listener.OnComplete();
            } else {
                // Handle failure
                listener.onSearchError("error adding To Firebase");
            }
        });
    }

    public static FullUser searchUserById(String userId, OnUserResultListener listener) {
        DatabaseReference usersRef = getInstance().getDatabase().getReference("users").child(userId);
        usersRef.keepSynced(true);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FullUser fullUser = dataSnapshot.getValue(FullUser.class);
                if (fullUser != null) {
                    listener.onUserFound(fullUser);
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onSearchError(error.getMessage());
            }
        });
        return null;
    }
    public static void getFriendList(DatabaseReference reference, OnFriendListListener listener) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> stringList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    if (value != null) {
                        stringList.add(value);
                    }
                }
                listener.onListReceived(stringList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error getting string list: " + error.getMessage());
                listener.onError(error.getMessage());
            }
        });
    }
    public static void reset() {
        instance = null;
        database = null;
    }
}
