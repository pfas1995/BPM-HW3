package com.adc2018.bpmhw3.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.adc2018.bpmhw3.R;

public class AdjustActivity extends AppCompatActivity {

    private static final String TAG = AdjustActivity.class.getSimpleName();

    private EditText editName;
    private EditText editCompany;
    private EditText editPosition;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust);
        editName = findViewById(R.id.editName);
        editCompany = findViewById(R.id.editCompany);
        editPosition = findViewById(R.id.editPosition);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editEmail = findViewById(R.id.editEmail);
    }



}
