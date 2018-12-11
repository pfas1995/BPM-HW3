package com.adc2018.bpmhw3.adapter.content;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.activity.GroupManageActivity;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;

import java.util.ArrayList;
import java.util.List;


public class GroupExpandableListAdapter extends BaseExpandableListAdapter {

    private Context ctx;
    private GroupManageActivity activity;
    private List<UserGroup> groups;
    private List<List<CardGroup>> childs;
    private PopupMenu cardPopupMenu;
    private PopupMenu groupPopupMenu;

    public static GroupExpandableListAdapter Factory(Context ctx, List<UserGroup> groups, List<List<CardGroup>> childs) {
        GroupExpandableListAdapter adapter = new GroupExpandableListAdapter();
        adapter.ctx = ctx;
        adapter.groups = groups;
        adapter.childs = childs;
        adapter.activity = (GroupManageActivity) ctx;
        return adapter;
    }

    public void setGroups(List<UserGroup> groups) {
        this.groups = groups;
    }

    public void setChilds(List<List<CardGroup>> childs) {
        this.childs = childs;
    }

    public CardGroup removeChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).remove(childPosition);
    }

    public void addGroup(UserGroup userGroup) {
        groups.add(userGroup);
        childs.add(new ArrayList<CardGroup>());
    }

    public void addChild(int groupPosition, CardGroup cardGroup) {
       childs.get(groupPosition).add(cardGroup);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    /**
     * 获取 GroupView
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.group_layout, parent, false);
        }
        else {
            view = convertView;
        }
        UserGroup userGroup = groups.get(groupPosition);
        GroupViewHolder viewHolder = GroupViewHolder.Factory(view);
        viewHolder.setContent(userGroup);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupManageActivity groupManageActivity = (GroupManageActivity) ctx;
                ExpandableListView eView = groupManageActivity.getExpandableListView();
                if(isExpanded) {
                    eView.collapseGroup(groupPosition);
                }
                else {
                    eView.expandGroup(groupPosition);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ctx, "long click" , Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }


    /**
     * 获取某个 GroupView 的 childView 并设置长按监听
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.group_member_layout, parent, false);
        }
        else {
            view = convertView;
        }
        CardGroup cardGroup = childs.get(groupPosition).get(childPosition);
        GroupMemberViewHolder viewHolder = GroupMemberViewHolder.Factory(view);
        viewHolder.setContent(cardGroup);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCardPopupMenu(v, groupPosition, childPosition);
                return true;
            }
        });
        return view;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }



    private static class GroupViewHolder {
        private TextView textView;

        public static GroupViewHolder Factory(View view) {
            GroupViewHolder viewHolder = new GroupViewHolder();
            viewHolder.textView = view.findViewById(R.id.groupName);
            return viewHolder;
        }
        public void setContent(UserGroup userGroup) {
            textView.setText(userGroup.getGroup().getName());
        }

    }

    private static class GroupMemberViewHolder {
        private TextView cardName;
        private TextView cardCompany;
        private TextView cardPosition;
        private TextView cardPhone;
        private TextView cardAddress;
        private TextView cardEmail;

        public static GroupMemberViewHolder Factory(View view){
            GroupMemberViewHolder viewHolder = new GroupMemberViewHolder();
            viewHolder.cardName = view.findViewById(R.id.cardName);
            viewHolder.cardCompany = view.findViewById(R.id.cardCompany);
            viewHolder.cardPosition = view.findViewById(R.id.cardPosition);
            viewHolder.cardPhone = view.findViewById(R.id.cardPhone);
            viewHolder.cardAddress = view.findViewById(R.id.cardAddress);
            viewHolder.cardEmail = view.findViewById(R.id.cardEmail);
            return viewHolder;
        }

        public void setContent(CardGroup cardGroup) {
            Card card = cardGroup.getCard();
            cardName.setText(card.getName());
            cardCompany.setText(card.getCompany());
            cardPosition.setText(card.getPosition());
            cardPhone.setText(card.getPhone_number());
            cardAddress.setText(card.getAddress());
            cardEmail.setText(card.getEmail());
        }
    }


    /**
     * 某个名片长按弹出选项
     * @param view
     */
    private void showCardPopupMenu(View view, final int cgroupPosition, final int cchildPosition) {
        cardPopupMenu = new PopupMenu(activity, view);
        cardPopupMenu.getMenuInflater().inflate(R.menu.card_menu, cardPopupMenu.getMenu());
        cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                CardGroup cardGroup = (CardGroup) GroupExpandableListAdapter.this.getChild(cgroupPosition, cchildPosition);
                Card card = cardGroup.getCard();
                switch (item.getItemId()) {
                    case R.id.move:
                        activity.moveCardDialog(cgroupPosition, cchildPosition);
                        break;
                    case R.id.share:
                        activity.shareCard(card);
                        break;
                    case R.id.meet:
                        activity.showMeet(card);
                        break;
                    case R.id.call:
                        activity.callCard(card);
                        break;
                    case R.id.message:
                        activity.messageCard(card);
                        break;
                    case R.id.remove:
                        activity.removeCardPre(cgroupPosition, cchildPosition);
                        break;
                }
                return true;
            }
        });
//        cardPopupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                // 控件消失时的事件
//            }
//        });
        cardPopupMenu.show();
    }

    private void moveCard() {

    }


}
