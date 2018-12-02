package com.adc2018.bpmhw3.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.utils.BPM3;
import com.adc2018.bpmhw3.utils.QRCodeUtil;

public class ExchangeCardActivity extends AppCompatActivity {

    private ImageView qrCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_card);
        qrCode = findViewById(R.id.QRCode);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(BPM3.user.getId(), 480, 480);
        qrCode.setImageBitmap(bitmap);
    }

    public void retClick(View view) {
        Intent intent = new Intent();
        this.setResult(RESULT_OK, intent);
        this.finish();
    }
}
