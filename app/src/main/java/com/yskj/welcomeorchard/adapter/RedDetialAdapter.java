package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.RedDetailsEntity;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/2/9.
 */

public class RedDetialAdapter extends RecyclerView.Adapter<RedDetialAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<RedDetailsEntity.DataBean.ReceiverListBean> list;
    private OnItemClickListener mOnItemClickListener;
    public RedDetialAdapter(Context context,ArrayList<RedDetailsEntity.DataBean.ReceiverListBean> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_red_detial,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RedDetailsEntity.DataBean.ReceiverListBean receiverListBean = list.get(position);
        holder.item_reddetial_amount.setText(StringUtils.getStringtodouble(receiverListBean.amount)+"元");
        holder.item_reddetial_name.setText(receiverListBean.nickName);
        holder.item_reddetial_time.setText(receiverListBean.receiveTime);
        Glide.with(context).load(list.get(position).avatar).error(R.mipmap.default_image).into(holder.item_reddetial_image);
        //GlideImage.loadImage(context,holder.item_reddetial_image,list.get(position).avatar,R.mipmap.default_image);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0)
            return 0;
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_reddetial_image;
        TextView item_reddetial_name,item_reddetial_time,item_reddetial_amount;
        public MyViewHolder(View view) {
            super(view);
            item_reddetial_amount = (TextView) view.findViewById(R.id.item_reddetial_amount);
            item_reddetial_time = (TextView) view.findViewById(R.id.item_reddetial_time);
           item_reddetial_name = (TextView) view.findViewById(R.id.item_reddetial_name);
            item_reddetial_image = (ImageView) view.findViewById(R.id.item_reddetial_image);
        }
    }
    public interface OnItemClickListener{
        void onItemClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}
