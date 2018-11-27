package com.adc2018.bpmhw3.adapter.content;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecycleViewAdapterTemplate<T>  extends RecyclerView.Adapter<RecycleViewAdapterTemplate.ViewHolder> {
    private List<T> list;
    /**
     * 内部的 ViewHodler 类，绑定控件和缓存控件
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // 在这里添加控件成员

        public ViewHolder(View view) {
            super(view);
            //在这里绑定控件成员
        }

        //在这里设置显示内容
        public void setContent(Object content){

        }
    }

    /**
     * 构造函数
     * @param list 要显示的数据队列
     */
    public RecycleViewAdapterTemplate(List<T> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecycleViewAdapterTemplate.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout,parent, false);
//        final ViewHolder viewHolder = new ViewHolder(view);
//        // 整个item view 的监听事件
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = viewHolder.getAdapterPosition();
//            }
//        });
//        return viewHolder;
        return null;
    }

    /**
     * 对象绑定 item
     * @param holder viewholder
     * @param position 对象在 list 中的索引位置
     */
    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterTemplate.ViewHolder holder, int position) {
        Object obj = list.get(position);
        holder.setContent(obj);
    }

    /**
     * 获取 list 的大小
     * @return
     */
    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }
}
