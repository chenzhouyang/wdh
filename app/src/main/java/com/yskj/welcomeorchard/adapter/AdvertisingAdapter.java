package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AdvertisingHsaOpenEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/3/17.
 */

public class AdvertisingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AdvertisingHsaOpenEntity.DataBean.AdRedVoListBean> adredlist;
    public AdvertisingAdapter(Context context,ArrayList<AdvertisingHsaOpenEntity.DataBean.AdRedVoListBean> adredlist){
        super();
        this.context = context;
        this.adredlist = adredlist;
    }
    @Override
    public int getCount() {
        if(adredlist!=null&&adredlist.size()!=0){
            return adredlist.size();

        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return adredlist.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_advertising,null);
            holder.adver_image = (ImageView) convertView.findViewById(R.id.adver_image);
            holder.advertising_amount = (TextView) convertView.findViewById(R.id.advertising_amount);
            holder.adver_time = (TextView) convertView.findViewById(R.id.adver_time);
            holder.adver_title = (TextView) convertView.findViewById(R.id.adver_title);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();

        }
        if(adredlist.get(position).title!=null||adredlist.get(position).title.length()!=0){
            holder.adver_title.setText(adredlist.get(position).title);
        }else {
            holder.adver_title.setText("广告红包");
        }

        holder.adver_time.setText(adredlist.get(position).createTime);
        holder.advertising_amount.setText("+"+StringUtils.getStringtodouble(adredlist.get(position).amount));
        GlideImage.loadImage(context,holder.adver_image,adredlist.get(position).cover,R.mipmap.img_error);
        return convertView;
    }
    public class ViewHolder{
        ImageView adver_image;
        TextView adver_title,advertising_amount,adver_time;
    }
}
