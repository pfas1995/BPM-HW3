package com.adc2018.bpmhw3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Main2Activity extends AppCompatActivity {


    private static final String TAG = Main2Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void startActivity(View view) {
        Intent intent = new Intent(Main2Activity.this, CardIdentifyActivity.class);
        startActivity(intent);
    }
}
