package com.example.finalProjectV1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseManager.getInstance(); // This initializes the FirebaseManager
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        FirebaseApp.initializeApp(this);
        FirebaseManager.getInstance();
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginButton.getId()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == registerButton.getId()) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        }

    }






}