package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class LocalServerNearListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LocalServerMainListBean.LocalLifesBean> arrayList;

    public LocalServerNearListAdapter(Context context, ArrayList<LocalServerMainListBean.LocalLifesBean> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local_server_near_list, null);
            holder.imgDes = (ImageView) convertView.findViewById(R.id.image_des);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.tvSend = (TextView) convertView.findViewById(R.id.tv_send);
            holder.tvHui = (TextView) convertView.findViewById(R.id.tv_hui);
            holder.tvGoodsName =(TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_sale_count = (TextView) convertView.findViewById(R.id.tv_sale_count);
            holder.tv_icon = (TextView) convertView.findViewById(R.id.tv_icon);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        LocalServerMainListBean.LocalLifesBean localLife = arrayList.get(i);
        if(localLife.lifeCover.length()!=0){
            GlideImage.loadImage(context,holder.imgDes,localLife.lifeCover,R.mipmap.img_error);
        }else{
            holder.imgDes.setImageResource(R.mipmap.img_error);
        }
        holder.tvName.setText(localLife.shopName);
        holder.tvGoodsName.setText(localLife.lifeName);
        holder.tvDistance.setText(localLife.distanceString);
        holder.tvHui.setText("暂时没用");
        holder.tvPrice.setText("￥"+localLife.teamBuyPrice);
        if (localLife.cloudOffset==0){
            holder.tv_icon.setVisibility(View.GONE);
            holder.tvSend.setVisibility(View.GONE);
        }else {
            holder.tv_icon.setVisibility(View.VISIBLE);
            holder.tvSend.setVisibility(View.VISIBLE);
            holder.tvSend.setText("抵扣"+localLife.cloudOffset+"积分");
        }
        holder.tv_sale_count.setText("已售："+localLife.saleCount);
        return convertView;
    }

    private class ViewHolder {
        private TextView tvName,tvSend,tvPrice,tvHui,tvDistance,tvGoodsName,tv_sale_count,tv_icon;
        private ImageView imgDes;
    }
}
