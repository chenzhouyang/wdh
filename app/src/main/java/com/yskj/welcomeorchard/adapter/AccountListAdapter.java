package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AccountListEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AccountListEntity.DataBean.ListBean> list;
    private OnItemClickListener mOnItemClickListener;
    public AccountListAdapter(Context context,ArrayList<AccountListEntity.DataBean.ListBean> list){
            super();
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_accounts,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AccountListEntity.DataBean.ListBean recordBean = list.get(position);
        if(list.get(position).amount>0){
            holder.item_accounts_moeny.setTextColor(context.getResources().getColor(R.color.activity_red));
            holder.item_accounts_moeny.setText("+"+StringUtils.getStringtodouble(recordBean.amount));
            holder.item_accounts_number.setText(recordBean.senderMobile);
            GlideImage.loadImage(context,holder.record_image,recordBean.receiverAvatar,R.mipmap.default_image);
            holder.item_accounts_name.setText(recordBean.senderNick);
        }else{
            holder.item_accounts_moeny.setText(StringUtils.getStringtodouble(recordBean.amount));
            holder.item_accounts_moeny.setTextColor(context.getResources().getColor(R.color.c01a809));
            holder.item_accounts_number.setText(recordBean.receiverMobile);
            GlideImage.loadImage(context,holder.record_image,recordBean.receiverAvatar,R.mipmap.default_image);
            holder.item_accounts_name.setText(recordBean.receiverNick);
        }
        String[] year = recordBean.createTime.split(" ");
        holder.item_accounts_year.setText(year[0]);
        holder.item_accounts_hours.setText(year[1]);
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
        TextView item_accounts_year,item_accounts_hours,item_accounts_name,item_accounts_number,item_accounts_moeny;
        ImageView record_image;
        public MyViewHolder(View view) {
            super(view);
            item_accounts_year = (TextView) view.findViewById(R.id.item_accounts_year);
            item_accounts_hours = (TextView) view.findViewById(R.id.item_accounts_hours);
            item_accounts_name = (TextView) view.findViewById(R.id.item_accounts_name);
            item_accounts_number = (TextView) view.findViewById(R.id.item_accounts_number);
            item_accounts_moeny = (TextView) view.findViewById(R.id.item_accounts_moeny);
            record_image = (ImageView) view.findViewById(R.id.record_image);
        }
    }
    public interface OnItemClickListener{
        void onItemClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}
