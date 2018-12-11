package com.adc2018.bpmhw3.activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.amap.Position;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.Meet;
import com.adc2018.bpmhw3.entity.rmp.list.MeetList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.amap.AMapApi;
import com.adc2018.bpmhw3.network.api.amap.AMapUtil;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookMeetActivity extends AppCompatActivity {


    private static  final String  TAG = LookMeetActivity.class.getSimpleName();

    private TextView meetLocation;
    private TextView meetTime;
    private TextView meetDescription;
    private BPMApi bpmApi = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);
    private AMapApi aMapApi = RetrofitTool.getRetrofit(AMapUtil.getBaseUrl()).create(AMapApi.class);

    private Card card;
    private Meet meet;
    private Position position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        card = (Card) getIntent().getSerializableExtra("card");
        init();
    }


    public void init() {
        Call<MeetList> call = bpmApi.findMeetByUserAndCard(BPM3.user.getId(), card.getId());
        call.enqueue(new Callback<MeetList>() {
            @Override
            public void onResponse(Call<MeetList> call, Response<MeetList> response) {
                if(response.isSuccessful()) {
                    List<Meet> meets = response.body().getMeetList();
                    if(!meets.isEmpty()) {
                        meet = meets.get(0);
                    }
                    else {
                        meet = null;
                    }
                    getLocation();
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
            public void onFailure(Call<MeetList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void getLocation() {
        if(meet != null) {
            String p = meet.getMeetDocument().getMeet_longitide() + "," + meet.getMeetDocument().getMeet_latitude();
            Call<ResponseBody> call = aMapApi.getAddress(AMapUtil.getApiKey(), p);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        position = BPM3.parserAmap(response.body());
                    }
                    else {
                        try{
                            Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        }
                        catch (Exception e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                    initWdiget();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }
        else {
            initWdiget();
        }
    }

    public void initWdiget() {
        setContentView(R.layout.activity_look_meet);
        if(meet == null) return;
        meetLocation = findViewById(R.id.meet_location);
        meetTime = findViewById(R.id.meet_time);
        meetDescription = findViewById(R.id.description);
        if(position != null) meetLocation.setText(position.getLocation());
        meetTime.setText(BPM3.timeFormat(meet.getMeetDocument().getMeet_time()));
        meetDescription.setText(meet.getMeetDocument().getDescription());
    }


    public void retClick(View view) {
        finish();
    }
}
