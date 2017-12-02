package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.NewsBoxEntity;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/20.
 */

public class NewsBoxAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsBoxEntity.ListBean> listData;

    public NewsBoxAdapter(Context context, ArrayList<NewsBoxEntity.ListBean> listBeen) {
        this.context = context;
        this.listData = listBeen;
    }

    @Override
    public int getCount() {
        if (listData == null || listData.size() == 0) {
            return 0;
        }
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pushmessage, null);
            holder.imgType = (ImageView) convertView.findViewById(R.id.img_type);
            holder.tvTitleType = (TextView) convertView.findViewById(R.id.tv_title_type);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsBoxEntity.ListBean listBean = listData.get(position);
        int type = listBean.type;
        // type: 消息类型 0:个人消息 1:公告消息 2:培训消息 3:活动消息
        switch (type) {
            case 0:
                holder.tvTitleType.setText("个人消息");
                holder.imgType.setImageResource(R.mipmap.messge_me);
                break;
            case 1:
                holder.tvTitleType.setText("公告消息");
                holder.imgType.setImageResource(R.mipmap.messge_notice);
                break;
            case 2:
                holder.tvTitleType.setText("培训消息");
                holder.imgType.setImageResource(R.mipmap.messge_train);
                break;
            case 3:
                holder.tvTitleType.setText("活动消息");
                holder.imgType.setImageResource(R.mipmap.messge_activity);
                break;
        }
        holder.tvTime.setText(listBean.createTime);
        holder.tvContent.setText(listBean.content);
        return convertView;
    }

    public class ViewHolder {
        ImageView imgType;
        TextView tvTitleType, tvTime, tvContent;
    }
}
