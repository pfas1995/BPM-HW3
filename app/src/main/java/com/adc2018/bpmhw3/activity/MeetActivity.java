package com.adc2018.bpmhw3.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.amap.Position;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.Meet;
import com.adc2018.bpmhw3.entity.rmp.MeetDocument;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.amap.AMapApi;
import com.adc2018.bpmhw3.network.api.amap.AMapUtil;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;
import com.adc2018.bpmhw3.utils.LocationUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetActivity extends Activity {


    private static final String TAG = MeetActivity.class.getSimpleName();

    private TextView meetLocation;
    private TextView meetTime;
    private EditText meetDescription;
    private Location location;

    private Card card;
    private MeetDocument meetDocument;
    private Meet meet;
    private long createTime;
    private Position position;

    private BPMApi bpmApi = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);
    private AMapApi aMapApi = RetrofitTool.getRetrofit(AMapUtil.getBaseUrl()).create(AMapApi.class);

    private int getLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);
        card = (Card) getIntent().getSerializableExtra("card");
        getPermission();
    }

    public void getPermission() {
        AndPermission.with(this)
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        init();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Log.d(TAG, "onAction: denied");
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(MeetActivity.this,"没有权限无法定位哦", Toast.LENGTH_SHORT).show();
                    }
                }).start();
    }


    public void init() {
        location = LocationUtils.getInstance(MeetActivity.this).showLocation();
        if(location == null) {
            Toast.makeText(MeetActivity.this, "获取位置失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        getLocation = 0;
        String position = location.getLongitude() + "," + location.getLatitude();
        Call<ResponseBody> call = aMapApi.getAddress(AMapUtil.getApiKey(), position);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    Position p = BPM3.parserAmap(responseBody);
                    if(p == null) {
                        Toast.makeText(MeetActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    MeetActivity.this.position = p;
                    initWdiget();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        Toast.makeText(MeetActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }



    public void initWdiget() {
        createTime = System.currentTimeMillis();
        meetLocation = findViewById(R.id.meet_location);
        meetTime = findViewById(R.id.meet_time);
        meetDescription = findViewById(R.id.description);
        meetLocation.setText(position.getLocation());
        meetTime.setText(BPM3.timeFormat(createTime));

    }

    @Override
    public void onStop() {
        super.onStop();
        LocationUtils.getInstance(MeetActivity.this).removeLocationUpdatesListener();
    }


    public void confirmClick(View view) {
        addMeetDocument();
    }


    public void addMeetDocument() {
        meetDocument = MeetDocument.Factory(createTime, location.getLongitude(), location.getLatitude(),
                meetDescription.getText().toString());
        Call<MeetDocument> call = bpmApi.addMeetDocument(meetDocument);
        call.enqueue(new Callback<MeetDocument>() {
            @Override
            public void onResponse(Call<MeetDocument> call, Response<MeetDocument> response) {
                if(response.isSuccessful()) {
                    meetDocument = response.body();
                    addMeet();
                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MeetDocument> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void addMeet() {
        meet = Meet.Factory(BPM3.user, card, meetDocument);
        Call<Meet> call = bpmApi.addMeet(meet);
        call.enqueue(new Callback<Meet>() {
            @Override
            public void onResponse(Call<Meet> call, Response<Meet> response) {
                if(response.isSuccessful()) {
                    meet = response.body();
                    Toast.makeText(MeetActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Meet> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    public void cancleClick(View view) {
        finish();
    }
}
