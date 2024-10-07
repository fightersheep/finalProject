package com.example.finalProjectV1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleSignInHelper.OnGoogleSignInListener {

    // UI elements
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private SignInButton googleSignInButton;

    // Helper for Google Sign-In
    private GoogleSignInHelper googleSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);

        // Set click listeners
        loginButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);

        // Initialize Google Sign-In helper
        googleSignInHelper = new GoogleSignInHelper(this, this);
    }

    // Handle button clicks
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            // TODO: Handle regular login here
        } else if (v.getId() == R.id.googleSignInButton) {
            googleSignInHelper.signIn();
        }
    }

    // Handle activity results (including Google Sign-In result)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GoogleSignInHelper.isSignInResultIntent(requestCode)) {
            googleSignInHelper.handleSignInResult(data);
        }
    }

    // Callback for successful Google Sign-In
    @Override
    public void onGoogleSignInSuccess(FirebaseUser user) {
        String email = user.getEmail();
        Toast.makeText(this, "Signed in as: " + email, Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(this, mainPageActivity.class);
        startActivity(homeIntent);
    }
    public void onFirstTimeSignIn(FirebaseUser user) {
        Intent registrationIntent = new Intent(this, RegisterActivity.class);
        registrationIntent.putExtra("user_id", user.getUid());
        registrationIntent.putExtra("email", user.getEmail());
        startActivity(registrationIntent);
        finish(); // Optional: finish the current activity if you don't want the user to come back to it
    }

    // Callback for failed Google Sign-In
    @Override
    public void onGoogleSignInFailure(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
