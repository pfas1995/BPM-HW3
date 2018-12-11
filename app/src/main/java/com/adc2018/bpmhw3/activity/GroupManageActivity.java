package com.adc2018.bpmhw3.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.adapter.content.GroupExpandableListAdapter;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.adc2018.bpmhw3.entity.rmp.Group;
import com.adc2018.bpmhw3.entity.rmp.ShareCard;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.entity.rmp.list.CardGroupList;
import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.entity.rmp.list.ShareCardList;
import com.adc2018.bpmhw3.entity.rmp.list.UserGroupList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupManageActivity extends AppCompatActivity {

    private static final String TAG = GroupManageActivity.class.getSimpleName();

    private static final int CALL_PHONE = 0;
    private static final int SEND_MESSAGE = 1;
    private static final int SHOW_MEET = 2;

    BottomNavigationView bottomNavigationView;

    private QMUIDialog addGroupDialog;
    private QMUIDialog moveCardDialog;
    private ExpandableListView eView;
    private GroupExpandableListAdapter adapter;
    private PopupMenu groupPopMenu;
    private PopupMenu cardPopMenu;

    private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    private List<UserGroup> userGroups;
    private List<List<CardGroup>> cardGroups;
    int groupPosition = 0;
    int childsPosition = 0;
    private CardGroup cCardGroup;
    private Card moveCard;

    private Friend friendA;
    private Friend friendB;

    /**
     * 初始化用户分组,获取用户的所有分组
     */
    private void initUserGroups() {
        userGroups = new ArrayList<>();
        cardGroups = new ArrayList<>();
        Call<UserGroupList> call =  api.getUserGroupByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    UserGroupList userGroupList = response.body();
                    if(!userGroupList.empty()) {
                         userGroups = userGroupList.getUserGgroup();
                         initCardGroups();
                         initWidget();
                    }
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                    finally {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 初始化分组卡片，获取所有分组的名片
     */
    public void initCardGroups() {
        if(groupPosition < userGroups.size()) {
            Call<CardGroupList> call = api.getCardGroupByGroupId(userGroups.get(groupPosition).getGroup().getId());
            call.enqueue(new Callback<CardGroupList>() {
                @Override
                public void onResponse(Call<CardGroupList> call, Response<CardGroupList> response) {
                    if(response.isSuccessful()) {
                        cardGroups.add(response.body().getCardGroups());
                        groupPosition++;
                        initCardGroups();
                    }
                    else {
                        Toast.makeText(GroupManageActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                        finally {
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CardGroupList> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }
        else {
            initWidget();
        }
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        setContentView(R.layout.activity_group_manage);
        adapter = GroupExpandableListAdapter.Factory(GroupManageActivity.this,
                userGroups, cardGroups);
        eView = findViewById(R.id.groupList);
        eView.setAdapter(adapter);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(GroupManageActivity.this, HomeActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.navigation_dashboard:
                        break;
                    case R.id.navigation_notifications:
                        intent = new Intent(GroupManageActivity.this, ShareManageActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    public ExpandableListView getExpandableListView() {
        return eView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        initUserGroups();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        }
    }


    /**
     * 响应添加分组
     * @param view
     */
    public void addGroupClick(View view) {
        addGroupDialog();
    }


    /**
     * 添加分组对话框，输出新的分组名
     */
    public void addGroupDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(
                GroupManageActivity.this);
        builder.setTitle("添加分组").setPlaceholder("在此输入分组名").setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick( QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText().toString();
                        if (text !=null && text.length() > 0) {
                            checkGroupNameValid(text.toString());
                        }
                        else {
                            Toast.makeText(GroupManageActivity.this, "请填入分组名", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addGroupDialog = builder.create();
        addGroupDialog.show();
    }

    /**
     * 移动名片对话框，选择移动到分组
     * @param groupPosition
     * @param childPosition
     */
    public void moveCardDialog(final int groupPosition, final int childPosition) {
        final List<String> items = new ArrayList<>();
        for(int i = 0; i < userGroups.size(); i++) {
            items.add(userGroups.get(i).getGroup().getName());
        }
        final QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(GroupManageActivity.this)
                .addItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == groupPosition) {
                            Toast.makeText(GroupManageActivity.this, "已在组中" , Toast.LENGTH_SHORT).show();
                        }
                        else {
                            confirmMoveCard(groupPosition, childPosition, which);
                            dialog.dismiss();
                        }
                    }
                });
        moveCardDialog = builder.create();
        moveCardDialog.show();
    }

    /**
     * 移动名片到另一分组
     * @param groupPosition
     * @param childPosition
     * @param targetGroupPosition
     */
    public void moveCard(final int groupPosition, final int childPosition, final int targetGroupPosition) {
        CardGroup source = (CardGroup) adapter.getChild(groupPosition, childPosition);
        UserGroup targetGroup = (UserGroup) adapter.getGroup(targetGroupPosition);
        CardGroup target = CardGroup.Factory(targetGroup.getGroup(), source.getCard());
        target.setId(source.getId());
        final Call<CardGroup> call = api.updateCardGroup(target.getId(), target);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    CardGroup cardGroup = response.body();
                    adapter.removeChild(groupPosition, childPosition);
                    adapter.addChild(targetGroupPosition, cardGroup);
                    viewRefresh();
                    Toast.makeText(GroupManageActivity.this, "移动成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "移动失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    /**
     * 检查分组名是否存在
     * @param name
     */
    public void checkGroupNameValid(final String name) {
        Call<UserGroupList> call =  api.getUserGroupByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    UserGroupList userGroupList = response.body();
                    if(!userGroupList.empty()) {
                        userGroups = userGroupList.getUserGgroup();
                        Boolean valid = true;
                        for(UserGroup userGroup: userGroups) {
                            if(Objects.equals(userGroup.getGroup().getName(), name)) {
                                valid = false;
                                break;
                            }
                        }
                        if(valid) {
                            addGroup(name);
                        }
                        else {
                            Log.d(TAG, "onResponse: " + "分组名重复");
                            Toast.makeText(addGroupDialog.getContext(), "分组名重复", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    /**
     * 新增分组
     * @param name
     */
    public void addGroup(String name) {
        Group group = Group.Factory(name);
        Call<Group> call = api.addGroup(group);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.isSuccessful()) {
                    Group group = response.body();
                    addToUserGroup(group);
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
     * 将新增分组与用户绑定形成新的用户分组
     * @param group
     */
    public void addToUserGroup(Group group) {
        UserGroup userGroup = UserGroup.Factory(BPM3.user, group);
        Call<UserGroup> call = api.addUserGroup(userGroup);
        call.enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                if(response.isSuccessful()) {
                    UserGroup userGroup = response.body();
                    Toast.makeText(GroupManageActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    adapter.addGroup(userGroup);
                    addGroupDialog.dismiss();
                    viewRefresh();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                        Toast.makeText(GroupManageActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }


    /**
     * 刷新界面
     */
    public void viewRefresh() {
        adapter.notifyDataSetChanged();
    }


    /**
     * 确认移动分组
     * @param groupPosition
     * @param childPosition
     * @param targetGroup
     */
    public void confirmMoveCard(final int groupPosition, final int childPosition, final int targetGroup) {
        new QMUIDialog.MessageDialogBuilder(GroupManageActivity.this) .setTitle("确认")
                .setMessage("确定要移动？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        moveCard(groupPosition, childPosition, targetGroup);
                        dialog.dismiss();
                    }
                })
                .show();
    }


    /**
     * 分享名片，先获取用户好友
     * @param card
     */
    public void shareCard(final Card card) {
        Call<FriendList> call = api.getFriendByUserId(BPM3.user.getId());
        call.enqueue(new Callback<FriendList>() {
            @Override
            public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                if(response.isSuccessful()) {
                    List<Friend> friends = response.body().getFriends();
                    shareDialog(card, friends);
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendList> call, Throwable t) {
                Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 分享框，选择分享对象
     * @param card
     * @param friends
     */
    public void shareDialog(final Card card, final List<Friend> friends) {
        final List<String> items = new ArrayList<>();
        for(int i = 0; i < friends.size(); i++) {
            items.add(friends.get(i).getUserCard().getUser().getUser_name());
        }
        final QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(GroupManageActivity.this)
                .addItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(GroupManageActivity.this,
//                                friend.getFriends().get(which).getUser().getUser_name(), Toast.LENGTH_SHORT).show();
                        shareCardToUser(card, friends.get(which).getUserCard().getUser());
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


    /**
     * 分享名片给目标对象，先检查是否已经分享，再分享
     * @param card
     * @param toUser
     */
    public void shareCardToUser(final Card card, final User toUser) {
        Call<ShareCardList> call = api.getSharecardByUserId(toUser.getId());
        call.enqueue(new Callback<ShareCardList>() {
            @Override
            public void onResponse(Call<ShareCardList> call, Response<ShareCardList> response) {
                if(response.isSuccessful()) {
                    ShareCard shareCard = response.body().getFirstSharecard();
                    if(shareCard == null) {
                        shareCard = ShareCard.Factory(toUser, card);
                        addShareCard(shareCard);
                    }
                    else {
                        putShareCard(shareCard);
                    }
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareCardList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
     }


    /**
     * 将分享的名片与目标对象绑定
     * @param shareCard
     */
    public void addShareCard(ShareCard shareCard) {
        Call<ShareCard> call = api.addShareCard(shareCard);
        call.enqueue(new Callback<ShareCard>() {
            @Override
            public void onResponse(Call<ShareCard> call, Response<ShareCard> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(GroupManageActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareCard> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
     }

    /**
     *
     * @param shareCard
     */
     public void putShareCard(ShareCard shareCard) {
        Call<ShareCard> call = api.updateSharecard(shareCard.getId(), shareCard);
        call.enqueue(new Callback<ShareCard>() {
            @Override
            public void onResponse(Call<ShareCard> call, Response<ShareCard> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(GroupManageActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                    try{
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    }
                    catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShareCard> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(GroupManageActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
     }


    /**
     * 拨打电话
     * @param card
     */
    public void callCard(final Card card) {
        AndPermission.with(this)
                .permission(Permission.CALL_PHONE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri phone = Uri.parse("tel:" + card.getPhone_number());
                        intent.setData(phone);
                        startActivityForResult(intent, CALL_PHONE);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(GroupManageActivity.this, "没有权限发起联系", Toast.LENGTH_LONG).show();
                        GroupManageActivity.this.setResult(RESULT_CANCELED);
                        finish();
                    }
                }).start();
    }


    /**
     * 发送消息
     * @param card
     */
    public void messageCard(Card card) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + card.getPhone_number()));
        startActivityForResult(intent, SEND_MESSAGE);
    }


    /**
     * 移除名片：移除组关系，若存在好友关系，移除好友关系，并移除目标好友中自己的名片
     * @param groupPosition
     * @param childsPosition
     */

    public void removeCardPre(final int groupPosition, final int childsPosition) {
        CardGroup target = (CardGroup) adapter.getChild(groupPosition, childsPosition);
        Call<FriendList> call = api.getCardFriend(BPM3.user.getId(), target.getCard().getId());
        call.enqueue(new Callback<FriendList>() {
            @Override
            public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                if(response.isSuccessful()) {
                    List<Friend>  friends = response.body().getFriends();
                    if(friends.size() != 0) {
                        friendA = friends.get(0);
                        removeFriend(friendA);
                        removeFriendBPre(friendA.getUserCard().getUser());

                    }
                    removeCard(groupPosition, childsPosition);
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

    /**
     * 移除目标好友的好友关系
     * @param user
     */
    public void removeFriendBPre(User user) {
        Call<FriendList> call = api.getUserABRelation(user.getId(), BPM3.user.getId());
        call.enqueue(new Callback<FriendList>() {
            @Override
            public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                if(response.isSuccessful()) {
                    List<Friend> friends = response.body().getFriends();
                    if(friends.size() != 0) {
                        friendB = friends.get(0);
                        removeFriend(friendB);
                        removeFriendBCardPre(friendB);
                    }
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


    /**
     * 移除自己的好友关系
     * @param friend
     */
    public void removeFriend(final Friend friend) {
        Friend update = new Friend();
        Call<Friend> call = api.updateFriend(friend.getId(), update);
        call.enqueue(new Callback<Friend>() {
            @Override
            public void onResponse(Call<Friend> call, Response<Friend> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "onResponse: remove" + friend.toString());
                }
                else {
                    Log.d(TAG, "onResponse: remove failure " + friend.toString());
                }
            }

            @Override
            public void onFailure(Call<Friend> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    /**
     * 移除目标对象中自己的名片，先获取目标对象的用户分组
     * @param friendB
     */
    public void removeFriendBCardPre(final Friend friendB) {
        UserCard userCardA = friendB.getUserCard();
        Call<CardGroupList> call = api.getCardGroupByCard(userCardA.getId());
        call.enqueue(new Callback<CardGroupList>() {
            @Override
            public void onResponse(Call<CardGroupList> call, Response<CardGroupList> response) {
                if(response.isSuccessful()) {
                    getUserGroup(friendB.getUser(), response.body().getCardGroups());
                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + response.errorBody().bytes());
                    }
                    catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    /**
     * 获取用户分组
     * @param user
     * @param cardGroups
     */
    public void getUserGroup(User user, final List<CardGroup> cardGroups) {
        Call<UserGroupList> call = api.getUserGroupByUserId(user.getId());
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    removeFriendBCard(response.body().getUserGgroup(), cardGroups);
                }
                else {
                    try{
                        Log.d(TAG, "onResponse: " + response.errorBody().bytes());
                    }
                    catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 移除目标对象中自己的名片，获取名片的 CardGroup, 然后移除组关系
     * @param userGroups
     * @param cardGroups
     */
    public void removeFriendBCard(List<UserGroup> userGroups, List<CardGroup> cardGroups) {
        for(UserGroup userGroup : userGroups) {
            for(CardGroup cardGroup : cardGroups) {
                if(cardGroup.getGroup().getId().equals(userGroup.getGroup().getId())) {
                    CardGroup update = new CardGroup();
                    Call<CardGroup> call = api.updateCardGroup(cardGroup.getId(), cardGroup);
                    call.enqueue(new Callback<CardGroup>() {
                        @Override
                        public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                            if(response.isSuccessful()) {

                            }
                            else {

                            }
                        }

                        @Override
                        public void onFailure(Call<CardGroup> call, Throwable t) {
                            Log.e(TAG, "onFailure: ", t);
                        }
                    });
                }
            }
        }
    }

    public void removeCard(final int groupPosition, final int childsPosition) {
        CardGroup target = (CardGroup) adapter.getChild(groupPosition, childsPosition);
        CardGroup update = new CardGroup();
        Call<CardGroup> call = api.updateCardGroup(target.getId(), update);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    adapter.removeChild(groupPosition, childsPosition);
                    viewRefresh();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    public void showMeet(Card card) {
        Intent intent = new Intent(GroupManageActivity.this, LookMeetActivity.class);
        intent.putExtra("card", card);
        startActivityForResult(intent, SHOW_MEET);
    }
}
