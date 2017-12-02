package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AdOpenRedDetailEntity;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * 创建日期 2017/3/17on 10:09.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OpenRedListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AdOpenRedDetailEntity.DataBean.ReceivedRedListBean> list;
    public OpenRedListAdapter(Context context,ArrayList<AdOpenRedDetailEntity.DataBean.ReceivedRedListBean> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list == null || list.size() == 0)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reddetial,null);
            holder.item_reddetial_amount = (TextView) convertView.findViewById(R.id.item_reddetial_amount);
            holder.item_reddetial_time = (TextView) convertView.findViewById(R.id.item_reddetial_time);
            holder.item_reddetial_name = (TextView) convertView.findViewById(R.id.item_reddetial_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        AdOpenRedDetailEntity.DataBean.ReceivedRedListBean receiverListBean = list.get(position);
        holder.item_reddetial_amount.setText(StringUtils.getStringtodouble(receiverListBean.amount)+"元");
        holder.item_reddetial_name.setText(receiverListBean.nickName);
        holder.item_reddetial_time.setText(receiverListBean.receiveTime);
        return convertView;
    }
    public class ViewHolder{
        TextView item_reddetial_name,item_reddetial_time,item_reddetial_amount;
    }
}
