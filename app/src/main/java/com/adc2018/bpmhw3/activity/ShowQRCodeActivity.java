package com.adc2018.bpmhw3.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;
import com.adc2018.bpmhw3.utils.QRCodeUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowQRCodeActivity extends AppCompatActivity {

    private static final String TAG = ShowQRCodeActivity.class.getSimpleName();

    private static final int DESIGN_CARD = 0;
    private static final int ADD_MEET = 1;

    private ImageView qrCode;
    private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    private UserCard userCard;


    private void init() {
        Call<UserCardList> call = api.getUserCardByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserCardList>() {
            @Override
            public void onResponse(Call<UserCardList> call, Response<UserCardList> response) {
                if(response.isSuccessful()) {
                    UserCardList userCardList = response.body();
                    if(userCardList.empty()) {
                       designCardTipDialog();
                    }
                    else {
                        userCard = userCardList.getUsercard().get(0);
                        initWidegt();
                    }
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        ShowQRCodeActivity.this.setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserCardList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                ShowQRCodeActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });
    }


    private  void initWidegt() {
        setContentView(R.layout.activity_show_qrcode_card);
        qrCode = findViewById(R.id.QRCode);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(BPM3.user.getId(), 480, 480);
        qrCode.setImageBitmap(bitmap);
        new CheckTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case DESIGN_CARD:
                init();
                break;
            case ADD_MEET:
                finish();
                break;
            default:
        }
    }

    public void retClick(View view) {
        Intent intent = new Intent();
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    public void designCardTipDialog() {
        new QMUIDialog.MessageDialogBuilder(ShowQRCodeActivity.this) .setTitle("提示")
                .setMessage("您还未完善个人信息，完善个人信息？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        ShowQRCodeActivity.this.setResult(RESULT_CANCELED);
                        ShowQRCodeActivity.this.finish();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Intent intent = new Intent(ShowQRCodeActivity.this, DesignCardActivity.class);
                        startActivityForResult(intent, DESIGN_CARD);
                    }
                })
                .show();
    }


    class CheckTask extends AsyncTask<Void, Integer, Boolean> {
        private FriendList friendList;
        private FriendList checkFriendList;
        private UserCard userCard;
        private Long startTime;

        private Boolean result = false;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Call<FriendList> call = api.getFriendByUserId(BPM3.user.getId());
            call.enqueue(new Callback<FriendList>() {
                @Override
                public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                    if(response.isSuccessful()) {
                        friendList = response.body();
                        check();
                    }
                    else {
                        try {
                            Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        }
                        catch (Exception e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendList> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
            while(System.currentTimeMillis() - startTime < 30000);
            return result;
        }


        public void check() {
            long beg = System.currentTimeMillis();
            while(System.currentTimeMillis() - beg < 10000);
            if(System.currentTimeMillis() - startTime < 30000) {
                Call<FriendList> call = api.getFriendByUserId(BPM3.user.getId());
                call.enqueue(new Callback<FriendList>() {
                    @Override
                    public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                        if(response.isSuccessful()) {
                            checkFriendList = response.body();
                            if(checkFriendList.getFriends().size() == friendList.getFriends().size()) {
                                check();
                            }
                            else {
                                getNewCard();
                            }
                        }
                        else {
                            try {
                                Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                            }
                            catch (Exception e) {
                                Log.e(TAG, "onResponse: ", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FriendList> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            }
            else {

            }
        }

        public void getNewCard() {
            List<Friend> old = friendList.getFriends();
            List<Friend> add = checkFriendList.getFriends();
            for(Friend friend: add) {
                if(!old.contains(friend)) {
                    userCard = friend.getUserCard();
                }
            }
            result = true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG, "onPostExecute: checkTask finished");
            if(result) {
                Intent intent = new Intent(ShowQRCodeActivity.this, MeetActivity.class);
                intent.putExtra("card", userCard.getCard());
                startActivityForResult(intent, ADD_MEET);
            }
            else {
                try{
                    Toast.makeText(ShowQRCodeActivity.this, "二维码失效", Toast.LENGTH_SHORT).show();
                    ShowQRCodeActivity.this.finish();
                }
                catch (Exception e) {
                    Log.e(TAG, "onPostExecute: ", e);
                }
            }
        }
    }
}
