package com.adc2018.bpmhw3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.adc2018.bpmhw3.R;

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int IDENTFY_CARD = 1;
    private static final int SHARE_CARD = 2;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.home_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent = new Intent(HomeActivity.this, GroupManageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_notifications:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 名片识别按钮
     * @param view
     */
    public void identifyCardClick(View view) {
        Intent intent = new Intent(HomeActivity.this, CardIdentifyActivity.class);
        startActivityForResult(intent, IDENTFY_CARD);
    }

    /**
     * 设计名片按钮
     * @param view
     */
    public void designCardClick(View view) {
        Intent intent = new Intent(HomeActivity.this, DesignCardActivity.class);
        startActivity(intent);
    }

    public void exchangeCardClick(View view) {
        Intent intent = new Intent(HomeActivity.this, ExchangeCardActivity.class);
        startActivityForResult(intent, SHARE_CARD);
    }

}
