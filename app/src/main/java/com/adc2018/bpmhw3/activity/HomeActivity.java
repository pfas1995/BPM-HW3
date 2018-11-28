package com.adc2018.bpmhw3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;

import com.adc2018.bpmhw3.R;

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int IDENTFY_CARD = 1;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.home_bottom_nav);
//        bottomNavigationView.setOnNavigationItemSelectedListener();
    }

    /**
     *
     * @param view
     */
    public void identifyCardClick(View view) {
        Intent intent = new Intent(HomeActivity.this, CardIdentifyActivity.class);
        startActivityForResult(intent, IDENTFY_CARD);
    }

}
