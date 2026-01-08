package com.example.assistli;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TabLayoutMediator mediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWelcome = findViewById(R.id.tvWelcome);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("USERNAME", "אורח");
        tvWelcome.setText("ברוכים הבאים, " + savedUsername);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new MainPagerAdapter(this));
        viewPager.setOffscreenPageLimit(4);
        mediator = new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0: tab.setText(""); break;
//                            case 1: tab.setText(""); break;
//                            case 2: tab.setText(""); break;
//                            case 3: tab.setText(""); break;
                        }
                    }
                });
        mediator.attach();
    }

    @Override
    protected void onDestroy() {
        if (mediator != null) mediator.detach();
        super.onDestroy();
    }
}