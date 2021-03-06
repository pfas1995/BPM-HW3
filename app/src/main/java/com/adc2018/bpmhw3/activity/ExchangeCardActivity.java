package com.adc2018.bpmhw3.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.entity.rmp.list.UserGroupList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeCardActivity extends AppCompatActivity {

    private static final String TAG = ExchangeCardActivity.class.getSimpleName();

    private static final int SCAN_QRCODE = 0;
    private static final int ADD_MEET = 1;


    private String uid;

    //本人
    private UserCard userCardA;

    //扫描对象
    private UserCard userCardB;

    private Friend friendA;
    private Friend friendB;
    private UserGroup groupA;
    private UserGroup groupB;
//    private int rollBack = 0;

    private static final BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_card);
        userCardA = (UserCard) getIntent().getSerializableExtra("userCard");
        scanQRCode();
    }


    /**
     * 扫描二维码实现
     *
     */
    public void scanQRCode() {
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(ExchangeCardActivity.this, CaptureActivity.class);
                        ZxingConfig config = new ZxingConfig();
                        config.setShowFlashLight(false);
                        config.setShowAlbum(false);
                        /*ZxingConfig是配置类
                         *可以设置是否显示底部布局，闪光灯，相册，
                         * 是否播放提示音  震动
                         * 设置扫描框颜色等
                         * 也可以不传这个参数
                         * */
//                                ZxingConfig config = new ZxingConfig();
//                                config.setPlayBeep(false);//是否播放扫描声音 默认为true
//                                config.setShake(false);//是否震动  默认为true
//                                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
//                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
//                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                        startActivityForResult(intent, SCAN_QRCODE);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(ExchangeCardActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                        ExchangeCardActivity.this.setResult(RESULT_CANCELED);
                        finish();
                    }
                }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SCAN_QRCODE:
                if(resultCode == RESULT_OK) {
                    ExchangeCardActivity.this.uid = intent.getStringExtra(Constant.CODED_CONTENT);
                    confirmExchangeCard();
                }
                else {
                    Toast.makeText(ExchangeCardActivity.this, "识别未成功", Toast.LENGTH_SHORT).show();
                    this.setResult(RESULT_CANCELED);
                    finish();
                }
                break;
            case ADD_MEET:
                finish();
                break;
                default:
        }
    }


    public void confirmExchangeCard() {
        new QMUIDialog.MessageDialogBuilder(ExchangeCardActivity.this) .setTitle("确认")
                .setMessage("确定交换？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        ExchangeCardActivity.this.setResult(RESULT_CANCELED);
                        ExchangeCardActivity.this.finish();
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        initExChange();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void initExChange() {
        getUserCardB(uid);
    }

    public void getUserCardB(String uid) {
        Call<UserCardList> call =  api.getUserCardByUserId(uid);
        call.enqueue(new Callback<UserCardList>() {
            @Override
            public void onResponse(Call<UserCardList> call, Response<UserCardList> response) {
                if(response.isSuccessful()) {
                    userCardB = response.body().getUsercard().get(0);
                    getUserGroupA();
                }
                else {
                    Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserCardList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    public void getUserGroupA() {
        Call<UserGroupList> call = api.getUserGroupByUserIdAndGroupName(userCardA.getUser().getId(),
                userCardA.getUser().getUser_name() + "默认分组");
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    groupA = response.body().getFirstUserGroup();
                    getUserGroupB();
                }
                else {
                    Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void getUserGroupB() {
        Call<UserGroupList> call = api.getUserGroupByUserIdAndGroupName(userCardB.getUser().getId(),
                userCardB.getUser().getUser_name() + "默认分组");
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    groupB = response.body().getFirstUserGroup();
                    checkExchangeCard();
                }
                else {
                    Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    public void checkExchangeCard() {
        friendA = Friend.Factory(userCardA.getUser(), userCardB);
        friendB = Friend.Factory(userCardB.getUser(), userCardA);
        Call<FriendList> call = api.getUserABRelation(friendA.getUser().getId(), friendB.getUser().getId());
        call.enqueue(new Callback<FriendList>() {
            @Override
            public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                if(response.isSuccessful()) {
                    FriendList friendList = response.body();
                    if(friendList.empty()) {
                        exchangeCard();
                    }
                    else {
                        Toast.makeText(ExchangeCardActivity.this, "已交换过", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendList> call, Throwable t) {

            }
        });
    }

    public void exchangeCard() {
        addFriendA();
    }

    public void addFriendA() {
        Call<Friend> call = api.addFriend(friendA);
        call.enqueue(new Callback<Friend>() {
            @Override
            public void onResponse(Call<Friend> call, Response<Friend> response) {
                if(response.isSuccessful()) {
                    addFirendB();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Friend> call, Throwable t) {
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void addFirendB() {
        Call<Friend> call = api.addFriend(friendB);
        call.enqueue(new Callback<Friend>() {
            @Override
            public void onResponse(Call<Friend> call, Response<Friend> response) {
                if(response.isSuccessful()) {
                    addCardGroupA();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Friend> call, Throwable t) {
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
                finish();
            }
        });
    }

    public void addCardGroupA() {
        CardGroup cardGroup = CardGroup.Factory(groupA.getGroup(), userCardB.getCard());
        Call<CardGroup> call = api.addCardGroup(cardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    addCardGroupB();
                }
                else {

                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void addCardGroupB() {
        CardGroup cardGroup = CardGroup.Factory(groupB.getGroup(), userCardA.getCard());
        Call<CardGroup> call = api.addCardGroup(cardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ExchangeCardActivity.this, "交换成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ExchangeCardActivity.this, MeetActivity.class);
                    intent.putExtra("card", userCardB.getCard());
                    startActivityForResult(intent, ADD_MEET);
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Toast.makeText(ExchangeCardActivity.this, "交换失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



//    public void rollBack() {
//
//        friendA.getFriends().remove(userCardB);
//        friendB.getFriends().remove(userCardA);
//        groupA.removeCard(userCardB.getCard());
//        groupB.removeCard(userCardA.getCard());
//        putFriendA();
//    }

}
