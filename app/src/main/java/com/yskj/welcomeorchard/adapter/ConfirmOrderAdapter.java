package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.ConfirmOrderEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.NumberFormat;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/2/13.
 */

public class ConfirmOrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ConfirmOrderEntity.CartListBean> arrayList;

    public ConfirmOrderAdapter(Context context, ArrayList<ConfirmOrderEntity.CartListBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (arrayList==null||arrayList.size()==0)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_confirm_order,null);
            holder.imgDes = (ImageView) convertView.findViewById(R.id.img_des);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_gid = (TextView) convertView.findViewById(R.id.tv_gid);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            holder.shopname_type = (TextView) convertView.findViewById(R.id.shopname_type);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ConfirmOrderEntity.CartListBean cartListBean = arrayList.get(position);
        holder.tv_name.setText(cartListBean.goodsName);
        holder.tv_price.setText("￥"+String.format("%.2f", NumberFormat.convertToDouble(cartListBean.goodsPrice,0d)));
        holder.tv_gid.setText("规格:"+cartListBean.specKeyName);
        holder.tv_count.setText("数量:X"+cartListBean.goodsNum+"");
        if(cartListBean.originalImg.length()!=0){
            GlideImage.loadImage(context,holder.imgDes,cartListBean.originalImg,R.mipmap.img_error);
        }else{
            holder.imgDes.setImageResource(R.mipmap.img_error);
        }
        if(cartListBean.freightType == 2){
            holder.shopname_type.setVisibility(View.VISIBLE);
            holder.shopname_type.setText("本商品暂时不支持当前位置发货");
        }else if(arrayList.get(position).freightType == 3){
            holder.shopname_type.setVisibility(View.VISIBLE);
            holder.shopname_type.setText("本商品为到付商品");
        }else {
            holder.shopname_type.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imgDes;
        TextView tv_name,tv_price,tv_gid,tv_count,shopname_type;

    }
}
