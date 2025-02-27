package com.example.finalProjectV1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalProjectV1.util.CustomAlertDialog;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.firebase.AccountHelper;
import com.example.finalProjectV1.fragments.mainPageActivity;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AccountHelper.OnGoogleSignInListener {

    // UI elements

    private SignInButton googleSignInButton;

    // Helper for Google Sign-In
    private AccountHelper accountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements

        googleSignInButton = findViewById(R.id.googleSignInButton);

        // Set click listeners
        googleSignInButton.setOnClickListener(this);

        // Initialize Google Sign-In helper
        accountHelper = new AccountHelper(this, this);
    }

    // Handle button clicks
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.googleSignInButton) {
            accountHelper.signIn();
        }
    }

    // Handle activity results (including Google Sign-In result)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (AccountHelper.isSignInResultIntent(requestCode)) {
            accountHelper.handleSignInResult(data);
        }
    }

    // Callback for successful Google Sign-In
    @Override
    public void onGoogleSignInSuccess(FirebaseUser user) {
        String email = user.getEmail();
        Intent homeIntent = new Intent(this, mainPageActivity.class);
        startActivity(homeIntent);
        finish();
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
        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.showTextOnlyDialog("Error","fail do loge in ");
    }


}
