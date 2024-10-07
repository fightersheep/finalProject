package com.example.finalProjectV1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button notiButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseManager.getInstance(); // This initializes the FirebaseManager
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        notiButton = findViewById(R.id.notiButton);
        notiButton.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginButton.getId()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == registerButton.getId()) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == notiButton.getId()) {
            Intent intent = new Intent(this, mainPageActivity.class);
            long delayInMillis = 5000; // 5 seconds
            String title = "My Notification";
            String message = "This is a scheduled notification";
            NotificationHelper.scheduleNotification(this, delayInMillis, title, message, intent);
        }

    }






}
