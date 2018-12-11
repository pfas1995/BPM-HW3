package com.adc2018.bpmhw3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.adapter.content.ShareMessageRecycleViewAdapter;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.adc2018.bpmhw3.entity.rmp.ShareCard;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.entity.rmp.list.ShareCardList;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.entity.rmp.list.UserGroupList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareManageActivity extends AppCompatActivity {

    private  static final String TAG = ShareManageActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private TextView textView;
    BottomNavigationView bottomNavigationView;

    private ShareMessageRecycleViewAdapter adapter;
    private List<ShareCard> shareCards;


    private ShareCard shareCard;
    private UserCard userCardA;
    private UserCard userCardB;
    private Friend friendA;
    private Friend friendB;
    private UserGroup groupA;
    private UserGroup groupB;
    private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        init();
    }


    public void init() {
        Call<ShareCardList> call = api.getSharecardByUserId(BPM3.user.getId());
        call.enqueue(new Callback<ShareCardList>() {
            @Override
            public void onResponse(Call<ShareCardList> call, Response<ShareCardList> response) {
                if(response.isSuccessful()) {
                    shareCards = response.body().getShareCards();
                    initWidget();
                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareCardList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(ShareManageActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void initWidget() {
        setContentView(R.layout.activity_share_manage);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.shareCardRecyclerView);
        textView = findViewById(R.id.messageNum);
        textView.setText("您有 " + shareCards.size() + " 张分享名片待处理");
        manager = new LinearLayoutManager(ShareManageActivity.this);
        recyclerView.setLayoutManager(manager);
        adapter = new ShareMessageRecycleViewAdapter(ShareManageActivity.this, shareCards);
        recyclerView.setAdapter(adapter);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(ShareManageActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.navigation_dashboard:
                         intent = new Intent(ShareManageActivity.this, GroupManageActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.navigation_notifications:
                        break;
                }
                return true;
            }
        });

    }


    public void refreshView() {
        textView.setText("您有 " + adapter.getItemCount() + " 张分享名片待处理");
        adapter.notifyDataSetChanged();
    }

    public void acceptCard(final ShareCard shareCard) {
        this.shareCard = shareCard;
        Call<UserCardList> call = api.getUserCardByCardId(shareCard.getCard().getId());
        call.enqueue(new Callback<UserCardList>() {
            @Override
            public void onResponse(Call<UserCardList> call, Response<UserCardList> response) {
                if(response.isSuccessful()) {
                    UserCardList userCardList = response.body();
                    List<UserCard> userCards = userCardList.getUsercard();
                    if(userCards.size() == 0) {
                        getUserDefaultGroup();
                    }
                    else {
                        userCardB = userCards.get(0);
                        getUserCardA();
                    }
                }
                else {
                    try{

                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserCardList> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void rejectCard(final ShareCard shareCard) {
        this.shareCard = shareCard;
        ShareCard target = new ShareCard();
        target.setId(shareCard.getId());
        Call<ShareCard> call = api.updateSharecard(target.getId(), target);
        call.enqueue(new Callback<ShareCard>() {
            @Override
            public void onResponse(Call<ShareCard> call, Response<ShareCard> response) {
                if(response.isSuccessful()) {
                    adapter.removeShareCard(shareCard);
                    refreshView();
                    Toast.makeText(ShareManageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    Call<ResponseBody> call1 = api.deleteShareCard(shareCard.getId());
                    call1.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
                else {
                    try{

                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareCard> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void getUserDefaultGroup() {
        Call<UserGroupList> call = api.getUserGroupByUserIdAndGroupName(BPM3.user.getId(),
                BPM3.user.getUser_name() + "默认分组");
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    groupA = response.body().getFirstUserGroup();
                    addCardToDefaultGroup();

                }
                else {
                    Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addCardToDefaultGroup() {
        CardGroup cardGroup = CardGroup.Factory(groupA.getGroup(), shareCard.getCard());
        Call<CardGroup> call = api.addCardGroup(cardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    rejectCard(shareCard);
                }
                else {

                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserCardA()  {
        Call<UserCardList> call =  api.getUserCardByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserCardList>() {
            @Override
            public void onResponse(Call<UserCardList> call, Response<UserCardList> response) {
                if(response.isSuccessful()) {
                    userCardA = response.body().getUsercard().get(0);
                    getUserGroupA();
                }
                else {
                    Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserCardList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
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
                    checkAccept();
                }
                else {
                    Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkAccept() {
        friendA = Friend.Factory(userCardA.getUser(), userCardB);
        friendB = Friend.Factory(userCardB.getUser(), userCardA);
        Call<FriendList> call = api.getUserABRelation(friendA.getUser().getId(), friendB.getUser().getId());
        call.enqueue(new Callback<FriendList>() {
            @Override
            public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                if(response.isSuccessful()) {
                    FriendList friendList = response.body();
                    if(friendList.empty()) {
                        accept();
                    }
                    else {
                        rejectCard(shareCard);
                        Toast.makeText(ShareManageActivity.this, "已存在", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendList> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void accept() {
        addFriendA();
    }

    public void addFriendA() {
        friendA = Friend.Factory(userCardA.getUser(), userCardB);
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
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Friend> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFirendB() {
        Call<Friend> call = api.addFriend(friendB);
        call.enqueue(new Callback<Friend>() {
            @Override
            public void onResponse(Call<Friend> call, Response<Friend> response) {
                if(response.isSuccessful()) {
                    updateGroupA();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Friend> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateGroupA() {
        CardGroup cardGroup = CardGroup.Factory(groupA.getGroup(), userCardB.getCard());
        Call<CardGroup> call = api.addCardGroup(cardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    updateGroupB();
                }
                else {

                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateGroupB() {
        CardGroup cardGroup = CardGroup.Factory(groupB.getGroup(), userCardA.getCard());
        Call<CardGroup> call = api.addCardGroup(cardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ShareManageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    rejectCard(shareCard);
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new  String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Toast.makeText(ShareManageActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
