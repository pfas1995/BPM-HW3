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

import java.util.List;


public class GroupExpandableListAdapter extends BaseExpandableListAdapter {

    private Context ctx;
    private GroupManageActivity activity;
    private UserGroup userGroup;
    private PopupMenu cardPopupMenu;
    private PopupMenu groupPopupMenu;
    private int cgroupPosition;
    private int cchildPosition;

    public static GroupExpandableListAdapter Factory(Context ctx, UserGroup userGroup) {
        GroupExpandableListAdapter adapter = new GroupExpandableListAdapter();
        adapter.ctx = ctx;
        adapter.userGroup = userGroup;
        adapter.activity = (GroupManageActivity) ctx;
        return adapter;
    }

    public void setGroups(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Override
    public int getGroupCount() {
        return userGroup.getGroup().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<CardGroup> groups = userGroup.getGroup();
        return groups.get(groupPosition).getCards().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        List<CardGroup> groups = userGroup.getGroup();
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<CardGroup> groups = userGroup.getGroup();
        return groups.get(groupPosition).getCards().get(childPosition);
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

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        List<CardGroup> groups = userGroup.getGroup();
        if(convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.group_layout, parent, false);
        }
        else {
            view = convertView;
        }
        CardGroup cardGroup = groups.get(groupPosition);
        GroupViewHolder viewHolder = GroupViewHolder.Factory(view);
        viewHolder.setContent(cardGroup);
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

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        List<CardGroup> groups = userGroup.getGroup();
        if(convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.group_member_layout, parent, false);
        }
        else {
            view = convertView;
        }
        Card card = groups.get(groupPosition).getCards().get(childPosition);
        GroupMemberViewHolder viewHolder = GroupMemberViewHolder.Factory(view);
        viewHolder.setContent(card);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                GroupExpandableListAdapter.this.cgroupPosition = groupPosition;
                GroupExpandableListAdapter.this.cchildPosition = cchildPosition;
                showCardPopupMenu(v);
                return false;
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
        public void setContent(CardGroup cardGroup) {
            textView.setText(cardGroup.getGroup_name());
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

        public void setContent(Card card) {
            cardName.setText(card.getName());
            cardCompany.setText(card.getCompany());
            cardPosition.setText(card.getPosition());
            cardPhone.setText(card.getPhone_number());
            cardAddress.setText(card.getAddress());
            cardEmail.setText(card.getEmail());
        }
    }

    private void showCardPopupMenu(View view) {
        cardPopupMenu = new PopupMenu(activity, view);
        cardPopupMenu.getMenuInflater().inflate(R.menu.card_menu, cardPopupMenu.getMenu());
        cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.move:
                        activity.moveCardDialog(cgroupPosition, cchildPosition);
//                        Toast.makeText(activity, "move：" + String.valueOf(cgroupPosition), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.remove:
                        break;
                    case R.id.call:
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
