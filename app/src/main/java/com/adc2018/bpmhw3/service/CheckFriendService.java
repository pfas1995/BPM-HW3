package com.adc2018.bpmhw3.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckFriendService extends Service {

    private static final String TAG = CheckFriendService.class.getSimpleName();
    private Context ctx;





    class CheckBind extends Binder {
        private FriendList friendList;
        private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

        public void startCheck() {
            Call<FriendList> call = api.getFriendByUserId(BPM3.user.getId());
            call.enqueue(new Callback<FriendList>() {
                @Override
                public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                    if(response.isSuccessful()) {
                        friendList = response.body();
                    }
                    else {

                    }
                }

                @Override
                public void onFailure(Call<FriendList> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }


        private void check() {
            try {
                Thread.sleep(1000);
                Call<FriendList> call = api.getFriendByUserId(BPM3.user.getId());
                call.enqueue(new Callback<FriendList>() {
                    @Override
                    public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                        if(response.isSuccessful()) {

                        }
                        else {

                        }
                    }

                    @Override
                    public void onFailure(Call<FriendList> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }


    public CheckFriendService(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
