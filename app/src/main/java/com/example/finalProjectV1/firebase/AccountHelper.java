package com.example.finalProjectV1.firebase;

import android.app.Activity;
import android.content.Intent;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.firebase.interfaces.OnLogoutListener;
import com.example.finalProjectV1.firebase.interfaces.OnUserResultListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class AccountHelper {

    // Request code for Google Sign-In
    private static final int RC_SIGN_IN = 9001;

    // Client for interacting with the Google Sign-In API
    private final GoogleSignInClient mGoogleSignInClient;

    // Firebase Authentication instance
    private final FirebaseAuth mAuth;

    // Firebase Realtime Database instance
    private final DatabaseReference mDatabase;

    // Reference to the activity using this helper
    private final Activity mActivity;

    // Listener to communicate sign-in results back to the activity
    private final OnGoogleSignInListener mListener;

    // Interface for callback methods
    public interface OnGoogleSignInListener {
        void onGoogleSignInSuccess(FirebaseUser user);
        void onGoogleSignInFailure(String errorMessage);
        void onFirstTimeSignIn(FirebaseUser user);
    }

    // Constructor
    public AccountHelper(Activity activity, OnGoogleSignInListener listener) {
        mActivity = activity;
        mListener = listener;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseManager.getInstance().getDatabase().getReference();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    // Initiates the Google Sign-In process
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Handles the result of the Google Sign-In process
    public void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            mListener.onGoogleSignInFailure("Google sign in failed: " + e.getMessage());
        }
    }

    // Authenticates with Firebase using the Google ID token
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkIfFirstTimeUser(user);
                    } else {
                        mListener.onGoogleSignInFailure("Authentication failed.");
                    }
                });
    }

    // Checks if it's the user's first time signing in
    private   void checkIfFirstTimeUser( FirebaseUser user) {
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User already exists in the database
                    mListener.onGoogleSignInSuccess(user);
                } else {
                    // First time user, redirect to registration

                    mListener.onFirstTimeSignIn(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mListener.onGoogleSignInFailure("Failed to check user status: " + databaseError.getMessage());
            }
        });
    }
    public static boolean isUserLoggedIn() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null;
    }
    public static void getUser(OnUserResultListener listener) {
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseManager.searchUserById(UserID,listener);
    }
    public static void logout(OnLogoutListener listener) {
        FirebaseAuth.getInstance().signOut();
        listener.onLogoutSuccess();
    }


    // Checks if the given request code is for Google Sign-In
    public static boolean isSignInResultIntent(int requestCode) {
        return requestCode == RC_SIGN_IN;
    }
}