package com.example.finalProjectV1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;

public class mainPageActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        viewPager = findViewById(R.id.pager);
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);

        ImageView homeArrow = findViewById(R.id.homeArrow);
        ImageView chatBubble = findViewById(R.id.chatBubble);
        ImageView copyright = findViewById(R.id.copyright);

        homeArrow.setOnClickListener(v -> viewPager.setCurrentItem(0));
        chatBubble.setOnClickListener(v -> viewPager.setCurrentItem(1));
        copyright.setOnClickListener(v -> viewPager.setCurrentItem(2));
    }
}