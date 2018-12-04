package com.adc2018.bpmhw3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.network.entity.UserList;
import com.adc2018.bpmhw3.utils.BPM3;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();


    private EditText nameEdit;
    private EditText passwdEdit;
    private User user;

    private static final int REGISTER = 1;
    private BPMApi bpmApi = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameEdit = findViewById(R.id.loginName);
        passwdEdit = findViewById(R.id.loginPasswd);
    }

    /**
     * 响应登录按钮
     * @param view
     */
    public void loginClick(View view) {
        user = User.getInstance(nameEdit.getText().toString(), passwdEdit.getText().toString());
        Log.d(TAG, "loginClick: " + user.toString());
        Call<UserList> call = bpmApi.login(user.getUser_name(), user.getUser_pwd());
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if(response.isSuccessful() && !response.body().empty()) {
                    List<User> userList = response.body().getUser();
                    user = BPM3.user = userList.get(0);
                    checkUserIntegrity();
                    Log.d(TAG, "onResponse: " + response.body().toString());
                }
                else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    if(!response.isSuccessful()) {
                        try {
                            Log.i(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "一些意料之外的情况出现了", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     *
     * @param view
     */
    public void registerClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER);
    }

    public void checkFriend() {
        Call<FriendList> call = bpmApi.getFriendByUserId(user.getId());
        call.enqueue(new Callback<FriendList>() {
            @Override
            public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                if(response.isSuccessful()) {
                    Friend friend = response.body().getFirstFriend();
                    if(friend == null) {
                        addFirend();
                    }
                    else {
                        checkSucess();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    if(!response.isSuccessful()) {
                        try {
                            Log.i(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<FriendList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    public void addFirend() {
        Friend friend = Friend.Factory(user);
        Call<Friend> call = bpmApi.addFriend(friend);
        call.enqueue(new Callback<Friend>() {
            @Override
            public void onResponse(Call<Friend> call, Response<Friend> response) {
                if(response.isSuccessful()) {
                    checkSucess();
                }
                else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    if(!response.isSuccessful()) {
                        try {
                            Log.i(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Friend> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    private void checkUserIntegrity() {
        checkFriend();
    }

    public void checkSucess() {
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


//    public void startActivity(View view) {
//        Intent intent = new Intent(LoginActivity.this, CardIdentifyActivity.class);
//        startActivity(intent);
//    }
}
