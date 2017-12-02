package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AdvertiseEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/3/17.
 */

public class AdvertiseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AdvertiseEntity.DataBean.AdRedVoListBean> advertiselist;
    public AdvertiseAdapter(Context context,ArrayList<AdvertiseEntity.DataBean.AdRedVoListBean> advertiselist){
        super();
        this.context = context;
        this.advertiselist = advertiselist;
    }
    @Override
    public int getCount() {
        if(advertiselist!=null&&advertiselist.size()!=0){
            return advertiselist.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return advertiselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_advertise,null);
            holder.adver_image = (ImageView) convertView.findViewById(R.id.adver_image);
            holder.adver_title = (TextView) convertView.findViewById(R.id.adver_title);
            holder.adver_amount = (TextView) convertView.findViewById(R.id.adver_amount);
            holder.adver_number = (TextView) convertView.findViewById(R.id.adver_number);
            holder.advertise_time = (TextView) convertView.findViewById(R.id.advertise_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.adver_amount.setText("已领取金额："+StringUtils.getStringtodouble(advertiselist.get(position).receiveAmount));
        holder.adver_number.setText("已领取人数："+advertiselist.get(position).receiveCount+"");
        holder.adver_title.setText(advertiselist.get(position).title);
        GlideImage.loadImage(context,holder.adver_image,advertiselist.get(position).cover,R.mipmap.img_error);
        holder.advertise_time.setText(advertiselist.get(position).createTime);
        return convertView;
    }
public class ViewHolder{
    ImageView adver_image;
    TextView adver_title,adver_amount,adver_number,advertise_time;
}
}
