package com.example.finalProjectV1.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finalProjectV1.adapters.PageAdapter;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.Activities.settingActivity;
import com.example.finalProjectV1.notification.FriendRequestNotification;

public class mainPageActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 viewPager;
    private ImageView gearIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        FriendRequestNotification note = new FriendRequestNotification(this);

        viewPager = findViewById(R.id.pager);
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);

        ImageView homeArrow = findViewById(R.id.homeArrow);
        ImageView chatBubble = findViewById(R.id.chatBubble);
        ImageView copyright = findViewById(R.id.copyright);
        gearIcon = findViewById(R.id.gearIcon);

        homeArrow.setOnClickListener(v -> viewPager.setCurrentItem(0));
        chatBubble.setOnClickListener(v -> viewPager.setCurrentItem(1));
        copyright.setOnClickListener(v -> viewPager.setCurrentItem(2));
        gearIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==gearIcon.getId()){
            Intent intent = new Intent(mainPageActivity.this, settingActivity.class);
            startActivity(intent);
        }
    }
}