package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerDetailBean;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.NumberFormat;

import java.util.ArrayList;

/**
 * Created by jianghe on 2016/11/10 0010.
 */
public class LocalServerDetailRecommendAadpter extends BaseAdapter {
    private Context context;
    private ArrayList<LocalServerDetailBean.OtherLifesBean> arrayList;
    private String name;

    public LocalServerDetailRecommendAadpter(Context context, ArrayList<LocalServerDetailBean.OtherLifesBean> arrayList, String name) {
        this.context = context;
        this.arrayList = arrayList;
        this.name = name;
    }

    @Override
    public int getCount() {
        if (arrayList == null || arrayList.size() == 0) {
            return 0;
        }
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local_server_recommend, null);
            holder.imageDes = (ImageView) convertView.findViewById(R.id.image_des);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvSell = (TextView) convertView.findViewById(R.id.tv_sell);
            holder.tv_send_red = (TextView) convertView.findViewById(R.id.tv_send_red);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocalServerDetailBean.OtherLifesBean otherLife = arrayList.get(i);
        GlideImage.loadImage(context, holder.imageDes, otherLife.lifeCover, R.mipmap.img_error);
        holder.tvName.setText(otherLife.lifeName);
        holder.tvDes.setText(otherLife.lifeProfile);
        holder.tvPrice.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(otherLife.teamBuyPrice, 0d)));
        holder.tvSell.setText("已售" + otherLife.saleCount + "份");
        if (otherLife.cloudOffset == 0) {
            holder.tv_send_red.setVisibility(View.GONE);
        } else {
            holder.tv_send_red.setVisibility(View.VISIBLE);
            holder.tv_send_red.setText(String.format("抵充 %.2f 积分", otherLife.cloudOffset));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tvName, tvDes, tvPrice, tvSell, tv_send_red;
        private ImageView imageDes;
    }
}
