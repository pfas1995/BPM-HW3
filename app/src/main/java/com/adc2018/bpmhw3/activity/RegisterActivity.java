package com.adc2018.bpmhw3.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.Group;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.network.entity.UserList;

import java.io.IOException;
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

    private UserGroup userGroup;
    private Group defaultGroup;
    private CardGroup defaultCardGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ctx = this;
        registernameEdit = findViewById(R.id.registerName);
        passwdEdit = findViewById(R.id.registerPasswd);
        passwdconfirmEdit = findViewById(R.id.registerPasswdConfirm);
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
        checkValid();
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
     * 检测用户是否合法
     *
     */
    public void checkValid() {
        Call<UserList> call = bpmApi.getUserByName(user.getUser_name());
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if(response.isSuccessful()){
                    //首先检查用户是否存在
                    if(response.body().empty()) {
                        register();
                    }
                    else {
                        Toast.makeText(ctx, "非法用户名", Toast.LENGTH_SHORT).show();
                    }
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

    /**
     * 注册用户
     */
    private void register() {
        Call<User> call = bpmApi.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                    addDefaultCardGroup();
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

    /**
     * 创建用户默认分组
     */
    public void addDefaultCardGroup() {
        defaultGroup = Group.Factory(user.getUser_name() + "默认分组");
        Call<Group> call = bpmApi.addGroup(defaultGroup);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.isSuccessful()) {
                    defaultGroup = response.body();
                    addUserGroup(defaultGroup);
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 创建用户分组
     * @param group 新增分组
     */
    public void addUserGroup(final Group group) {
        userGroup = UserGroup.Factory(user, group);
        Call<UserGroup> call = bpmApi.addUserGroup(userGroup);
        call.enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                if(response.isSuccessful()) {
                    userGroup = response.body();
                    Log.d(TAG, "onResponse: " + userGroup.toString());
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

}
