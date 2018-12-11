package com.adc2018.bpmhw3.adapter.content;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.activity.ShareManageActivity;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.ShareCard;

import java.util.List;

public class ShareMessageRecycleViewAdapter extends RecyclerView.Adapter<ShareMessageRecycleViewAdapter.ViewHolder> {

    private Context ctx;
    private ShareManageActivity activity;
    private List<ShareCard>  shareCards;

    private PopupMenu cardPopupMenu;
    /**
     * 内部的 ViewHodler 类，绑定控件和缓存控件
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 在这里添加控件成员

        private TextView name;
        private TextView company;
        private TextView position;
        private TextView phone;
        private TextView email;
        private TextView address;

        public ViewHolder(View view) {
            super(view);
            //在这里绑定控件成员
            name = view.findViewById(R.id.name);
            company = view.findViewById(R.id.company);
            position = view.findViewById(R.id.position);
            phone = view.findViewById(R.id.phone);
            email = view.findViewById(R.id.email);
            address = view.findViewById(R.id.address);
        }

        //在这里设置显示内容
        public void setContent(Object content){
            Card card = (Card) content;
            name.setText(card.getName());
            company.setText(card.getCompany());
            position.setText(card.getPosition());
            phone.setText(card.getPhone_number());
            address.setText(card.getAddress());
            email.setText(card.getEmail());
        }
    }


    public void removeShareCard(ShareCard shareCard) {
        shareCards.remove(shareCard);
    }




    public ShareMessageRecycleViewAdapter(Context ctx, List<ShareCard> shareCards) {
        this.ctx = ctx;
        activity = (ShareManageActivity) ctx;
        this.shareCards = shareCards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.card_share_card, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        ((CardView)view).setUseCompatPadding(true);
        return new ShareMessageRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ShareCard shareCard = shareCards.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCardPopupMenu(holder.itemView, position);
                return true;
            }
        });
        holder.setContent(shareCard.getCard());

    }

    @Override
    public int getItemCount() {
        if(shareCards != null) {
            return shareCards.size();
        }
        return 0;
    }


    /**
     * 某个名片长按弹出选项
     * @param view
     */
    private void showCardPopupMenu(View view, final int position) {
        cardPopupMenu = new PopupMenu(activity, view);
        cardPopupMenu.getMenuInflater().inflate(R.menu.share_card_menu, cardPopupMenu.getMenu());
        cardPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                ShareCard shareCard = shareCards.get(position);
                switch (item.getItemId()) {
                    case R.id.accept:
                        activity.acceptCard(shareCard);
                        break;
                    case R.id.reject:
                        activity.rejectCard(shareCard);
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





}
