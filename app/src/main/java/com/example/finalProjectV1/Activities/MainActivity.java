package com.example.finalProjectV1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.AccountHelper;
import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.firebase.interfaces.OnLogoutListener;
import com.example.finalProjectV1.firebase.interfaces.OnUserResultListener;
import com.example.finalProjectV1.fragments.mainPageActivity;
import com.example.finalProjectV1.util.CustomAlertDialog;
import com.google.firebase.FirebaseApp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnUserResultListener {
    private CustomAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new CustomAlertDialog(this);
        FirebaseManager.getInstance(); // This initializes the FirebaseManager
        FirebaseApp.initializeApp(this);
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    @Override
    public void onClick(View v) {
        if(!AccountHelper.isUserLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(
                    MainActivity.this,
                    R.anim.slide_up_in,
                    R.anim.slide_up_out
            );
            startActivity(intent, options.toBundle());
        }
        else {

            AccountHelper.getUser(this);

        }



    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
    }


    @Override
    public FullUser onUserFound(FullUser fullUser) {
        dataManeger.setUser(fullUser);
        Intent intent = new Intent(MainActivity.this, mainPageActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeCustomAnimation(
                    MainActivity.this,
                    R.anim.slide_up_in,
                    R.anim.slide_up_out
            );

            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        Log.d("taf", "onUserFound: ");
        return fullUser;

    }

    @Override
    public void onUserNotFound() {
        AccountHelper.logout(new OnLogoutListener() {
            @Override
            public void onLogoutSuccess() {
                dialog.showTextOnlyDialog("user not found","user not found please log in again");
            }
        });

    }

    @Override
    public void onSearchError(String error) {
        dialog.showTextOnlyDialog("error",error);
        Log.d("taf", "error: ");

    }
}