package com.adc2018.bpmhw3.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.network.entity.UserList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText registernameEdit;
    private EditText passwdEdit;
    private EditText passwdconfirmEdit;
    private Context ctx;
    private final BPMApi bpmApi = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);
    private User user;
    private List<BroadcastReceiver> broadcastReceiverList = new ArrayList<>();
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ctx = this;
        registernameEdit = findViewById(R.id.registerName);
        passwdEdit = findViewById(R.id.registerPasswd);
        passwdconfirmEdit = findViewById(R.id.registerPasswdConfirm);
        registerLocalBroadcastReceiver(new ValidBroadcastReceiver(), "com.adc2018.bpmhw3.register.VALID");
    }

    /***
     * 注册一个本地广播接收器
     * @param br 接收器
     * @param action 响应广播
     */
    private void registerLocalBroadcastReceiver(BroadcastReceiver br, String action) {
        if(localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        localBroadcastManager.registerReceiver(br, intentFilter);
        broadcastReceiverList.add(br);
    }


    /**
     * 注销所有的本地广播接器
     * 只在 onDestroy() 中调用
     */
    private void unregisterAllLocalReceiver() {
        if(localBroadcastManager != null) {
            for(BroadcastReceiver br : broadcastReceiverList) {
                localBroadcastManager.unregisterReceiver(br);
            }
            broadcastReceiverList.clear();
        }
    }

    /**
     * 注册按钮监听事件
     * @param view
     */
    public void registerClick(View view) {
        String name = registernameEdit.getText().toString();
        String passwd = passwdEdit.getText().toString();
        String passwdConfirm = passwdconfirmEdit.getText().toString();
        if(!Objects.equals(passwd, passwdConfirm)) {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Objects.equals(name, "") || Objects.equals(passwd, "")) {
            Toast.makeText(this, "非法输入", Toast.LENGTH_SHORT).show();
            return;
        }
        user = User.getInstance(name, passwd);
        register(user);
    }

    /**
     * 取消按钮监听事件
     * @param view
     */
    public void cancleClick(View view) {
        Intent intent = new Intent();
        RegisterActivity.this.setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * 注册用户
     * @param user 用户
     */
    public void register(User user) {
        Call<UserList> call = bpmApi.getUserByName(user.getUser_name());
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if(response.isSuccessful()){
                    //首先检查用户是否存在
                    Boolean valid = false;
                    if(response.body().empty()) {
                        valid = true;
                    }
                    Intent intent = new Intent("com.adc2018.bpmhw3.register.VALID");
                    intent.putExtra("valid", valid);
                    localBroadcastManager.sendBroadcast(intent);
                }
                else{
                    try {
                        Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Toast.makeText(ctx, "一些意料之外的事情发生了", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(ctx, "一些意料之外的事情发生了", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onResponse: ", t);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterAllLocalReceiver();
    }

    private class ValidBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            Boolean valid = intent.getBooleanExtra("valid",false);
            if(!valid) {
                Toast.makeText(ctx, "非法用户", Toast.LENGTH_SHORT).show();
            }
            else {
                Call<User> call = bpmApi.registerUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(ctx, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent();
                            RegisterActivity.this.setResult(RESULT_CANCELED, intent1);
                            finish();
                        }
                        else {
                            Toast.makeText(ctx, "注册失败", Toast.LENGTH_SHORT).show();
                            try {
                                Log.i(TAG, "onResponse: "+ new String(response.errorBody().bytes()));
                            } catch (IOException e) {
                                Toast.makeText(ctx, "一些意料之外的事情发生了", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onResponse: ", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(ctx, "一些意料之外的事情发生了", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponse: ", t);
                    }
                });
            }
        }
    }
}
