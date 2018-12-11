package com.adc2018.bpmhw3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Activity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    private static final int IDENTFY_CARD = 1;
    private static final int SHARE_CARD = 2;
    private static final int EXCHANGE_CARD = 3;
    BottomNavigationView bottomNavigationView;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userName = findViewById(R.id.userName);
        userName.setText(BPM3.user.getUser_name());
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:

                        Intent intent = new Intent(HomeActivity.this, GroupManageActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.navigation_notifications:
                        intent = new Intent(HomeActivity.this, ShareManageActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
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
        Intent intent = new Intent(HomeActivity.this, ShowQRCodeActivity.class);
        startActivityForResult(intent, SHARE_CARD);
    }


    /**
     * 检查个人名片
     */
    public void checkPersonalCard() {
        Call<UserCardList> call = api.getUserCardByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserCardList>() {
            @Override
            public void onResponse(Call<UserCardList> call, Response<UserCardList> response) {
                if(response.isSuccessful()) {
                    UserCardList userCardList = response.body();
                    if(userCardList.empty()) {
                        Toast.makeText(HomeActivity.this, "请先完善个人名片信息", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        UserCard userCard = userCardList.getUsercard().get(0);
                        startExchange(userCard);
                    }
                }
                else {
                    Toast.makeText(HomeActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserCardList> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    public void startExchange(UserCard userCard) {
        Intent intent = new Intent(HomeActivity.this, ExchangeCardActivity.class);
        intent.putExtra("userCard", userCard);
        startActivityForResult(intent, EXCHANGE_CARD);
    }
    /**
     * 扫一扫响应
     * @param view
     */
    public void scanClick(View view) {
        checkPersonalCard();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case EXCHANGE_CARD:
//                if(resultCode == RESULT_OK) {
//                    String content = intent.getStringExtra(Constant.CODED_CONTENT);
//                    Toast.makeText(HomeActivity.this, content, Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(HomeActivity.this, "识别未成功", Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }
}
