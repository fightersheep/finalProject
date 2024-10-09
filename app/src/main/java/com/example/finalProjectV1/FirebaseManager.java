package com.example.finalProjectV1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public static void getUserIdFromEmail(String email, OnUserIdFetchedListener listener) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (!isNewUser) {
                            // User exists, now get the user ID
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null && user.getEmail().equals(email)) {
                                String userId = user.getUid();
                                listener.onUserIdFetched(userId);
                            } else {
                                listener.onError("User not found or not logged in");
                            }
                        } else {
                            listener.onError("User does not exist");
                        }
                    } else {
                        listener.onError("Error fetching sign-in methods: " + task.getException().getMessage());
                    }
                });
    }

    public interface OnUserIdFetchedListener {
        void onUserIdFetched(String userId);
        void onError(String errorMessage);
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }
}
