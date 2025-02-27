package com.example.finalProjectV1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.firebase.AccountHelper;
import com.example.finalProjectV1.firebase.interfaces.OnLogoutListener;

public class settingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView LogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LogOut = findViewById(R.id.logoutOption);
        LogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == LogOut.getId()){
            AccountHelper.logout(new OnLogoutListener() {
                @Override
                public void onLogoutSuccess() {
                    Intent intent = new Intent(settingActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            });
        }
    }
}