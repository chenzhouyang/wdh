package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class LocalServerMainListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LocalServerMainListBean.LocalLifesBean> arrayList;

    public LocalServerMainListAdapter(Context context, ArrayList<LocalServerMainListBean.LocalLifesBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (arrayList==null||arrayList.size()==0){
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
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local_server_main_list, null);
            holder.imgDes = (ImageView) convertView.findViewById(R.id.image_des);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
            holder.tvSend = (TextView) convertView.findViewById(R.id.tv_send);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvRank = (TextView) convertView.findViewById(R.id.tv_rank);
            holder.tvSell = (TextView) convertView.findViewById(R.id.tv_sell);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        LocalServerMainListBean.LocalLifesBean localLife = arrayList.get(i);
        holder.tvName.setText(localLife.shopName);
        if (TextUtils.isEmpty(localLife.distanceString)){
            holder.tvDistance.setVisibility(View.GONE);
        }else {
            holder.tvDistance.setText("距离"+localLife.distanceString);
        }
        holder.tvDes.setText(localLife.lifeName);
        holder.tvPrice.setText("￥"+ StringUtils.getStringtodouble(localLife.teamBuyPrice));
        if (localLife.cloudOffset==0){
            holder.tvSend.setVisibility(View.GONE);
        }else {
            holder.tvSend.setVisibility(View.VISIBLE);
            holder.tvSend.setText("抵扣"+StringUtils.getStringtodouble(localLife.cloudOffset)+"积分");
        }

        holder.tvSell.setText("已售"+localLife.saleCount);
        holder.tvRank.setText(localLife.shopAreaString);
        if(localLife.lifeCover.length()!=0){
            GlideImage.loadImage(context,holder.imgDes,localLife.lifeCover,R.mipmap.img_error);
        }else{
            holder.imgDes.setImageResource(R.mipmap.img_error);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tvName,tvDes,tvSend,tvPrice,tvRank,tvDistance,tvSell;
        private ImageView imgDes;
    }
}
