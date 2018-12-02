package com.adc2018.bpmhw3.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.adapter.content.GroupExpandableListAdapter;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.entity.rmp.list.UserGroupList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupManageActivity extends AppCompatActivity {

    private static final String TAG = GroupManageActivity.class.getSimpleName();

    private QMUIDialog addGroupDialog;
    private QMUIDialog moveCardDialog;
    private ExpandableListView eView;
    private GroupExpandableListAdapter adapter;
    private PopupMenu groupPopMenu;
    private PopupMenu cardPopMenu;

    private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    private UserGroup userGroup;
    private CardGroup cCardGroup;
    private Card moveCard;


    private void init() {
        Call<UserGroupList> call =  api.getUserGroup(BPM3.user.getId());
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    UserGroupList userGroupList = response.body();
                    if(!userGroupList.empty()) {
                         userGroup = userGroupList.getUserGgroup().get(0);
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

    private void initWidget() {
        setContentView(R.layout.activity_group_manage);
        adapter = GroupExpandableListAdapter.Factory(GroupManageActivity.this,
                userGroup);
        eView = findViewById(R.id.groupList);
        eView.setAdapter(adapter);
    }

    public ExpandableListView getExpandableListView() {
        return eView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar_layout);
        init();
    }

    public void addGroupClick(View view) {
        addGroupDialog();
    }


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

    public void moveCardDialog(final int groupPosition, final int childPosition) {
        final List<String> items = new ArrayList<>();
        List<CardGroup> cardGroups = userGroup.getGroup();
        for(int i = 0; i < cardGroups.size(); i++) {
            items.add(cardGroups.get(i).getGroup_name());
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
//                            moveCard(groupPosition, childPosition);
                        }
                    }
                });
        moveCardDialog = builder.create();
        moveCardDialog.show();
    }


    /**
     * 回滚
     * @param groupPosition
     * @param childPosition
     * @param targetGroup
     */
    public void rollBackMove(int groupPosition, int childPosition, int targetGroup) {
        CardGroup source =  userGroup.getGroup().get(groupPosition);
        CardGroup target = userGroup.getGroup().get(targetGroup);
        target.getCards().remove(moveCard);
        source.getCards().add(childPosition, moveCard);
        Call<CardGroup> call = api.updateCardGroup(source.getId(), source);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {

            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
        call = api.updateCardGroup(target.getId(), target);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {

            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }


    public void moveCard(final int groupPosition, final int childPosition, final int targetGroup) {
        CardGroup source =  userGroup.getGroup().get(groupPosition);
        CardGroup target = userGroup.getGroup().get(targetGroup);
        moveCard = source.getCards().remove(childPosition);
        target.addCard(moveCard);
        final Call<CardGroup> call = api.updateCardGroup(source.getId(), source);
        final Call<CardGroup> call2 = api.updateCardGroup(target.getId(), target);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    call2.enqueue(new Callback<CardGroup>() {
                        @Override
                        public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                            if(response.isSuccessful()) {
                                viewRefresh(groupPosition, targetGroup);
                            }
                            else {
                                rollBackMove(groupPosition, childPosition, targetGroup);
                            }
                        }

                        @Override
                        public void onFailure(Call<CardGroup> call, Throwable t) {
                            rollBackMove(groupPosition, childPosition, targetGroup);
                            Log.e(TAG, "onFailure: ", t);
                        }
                    });
                }
                else {
                    rollBackMove(groupPosition, childPosition, targetGroup);
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                rollBackMove(groupPosition, childPosition, targetGroup);
                Log.e(TAG, "onFailure: ", t);
            }
        });
//        Log.d(TAG, "moveCard: " + String.valueOf(targetGroup) + " " + userGroup.toString());
//        Call<UserGroup> call = api.updateUserGroup(userGroup.getId(), userGroup);
//        call.enqueue(new Callback<UserGroup>() {
//            @Override
//            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
//                if(response.isSuccessful()) {
//                    userGroup = response.body();
//                    Toast.makeText(GroupManageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
//                    viewRefresh();
//                }
//                else{
//                    rollBackMove(groupPosition, childPosition, targetGroup);
//                    Toast.makeText(GroupManageActivity.this, "移动失败", Toast.LENGTH_SHORT).show();
//                    try {
//                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserGroup> call, Throwable t) {
//                rollBackMove(groupPosition, childPosition, targetGroup);
//                Log.e(TAG, "onFailure: ", t);
//            }
//        });
    }


    public void checkGroupNameValid(final String name) {
        Call<UserGroupList> call =  api.getUserGroup(BPM3.user.getId());
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    UserGroupList userGroupList = response.body();
                    if(!userGroupList.empty()) {
                        userGroup = userGroupList.getUserGgroup().get(0);
                        List<CardGroup> groups = userGroup.getGroup();
                        Boolean valid = true;
                        for(CardGroup cardGroup: groups) {
                            if(Objects.equals(cardGroup.getGroup_name(), name)) {
                                valid = false;
                                break;
                            }
                        }
                        if(valid) {
                            addCardGroup(name);
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

    public void addCardGroup(String name) {
        CardGroup cardGroup = CardGroup.Factory(name);
        Call<CardGroup> call = api.addCardGroup(cardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    CardGroup cardGroup = response.body();
                    addToUserGroup(cardGroup);
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
            public void onFailure(Call<CardGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    public void addToUserGroup(CardGroup cardGroup) {
        userGroup.addCardGroup(cardGroup);
        Call<UserGroup> call = api.updateUserGroup(userGroup.getId(), userGroup);
        call.enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                if(response.isSuccessful()) {
                    userGroup = response.body();
                    Toast.makeText(GroupManageActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
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

    public void viewRefresh() {
        adapter.setGroups(userGroup);
        adapter.notifyDataSetChanged();
    }

    public void viewRefresh(int groupPosition, int targetPosition) {
        adapter.setGroups(userGroup);
        adapter.notifyDataSetChanged();
        adapter.onGroupExpanded(groupPosition);
        adapter.onGroupCollapsed(targetPosition);
    }

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
}
