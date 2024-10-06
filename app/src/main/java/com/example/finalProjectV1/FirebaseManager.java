package com.example.finalProjectV1;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    private static FirebaseManager instance;
    private FirebaseDatabase database;

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

    public FirebaseDatabase getDatabase() {
        return database;
    }
}
