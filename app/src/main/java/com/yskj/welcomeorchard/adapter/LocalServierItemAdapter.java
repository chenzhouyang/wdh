package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerOrderBean;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * 作者：陈宙洋
 * 日期：2017/8/10.
 * 描述：
 */

public class LocalServierItemAdapter extends BaseAdapter {
    private Context context;
    private List<LocalServerOrderBean.DataBean.OrderVosBean.GoodsListBean> lifeCover;
    public LocalServierItemAdapter(Context context,List<LocalServerOrderBean.DataBean.OrderVosBean.GoodsListBean> lifeCover){
        this.context = context;
        this.lifeCover = lifeCover;
    }
    @Override
    public int getCount() {
        return lifeCover == null ? 0 : lifeCover.size();
    }

    @Override
    public Object getItem(int position) {
        return lifeCover.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local_order, null);
            holder.imgDes = (ImageView) convertView.findViewById(R.id.image_des);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        LocalServerOrderBean.DataBean.OrderVosBean.GoodsListBean localLife = lifeCover.get(position);
        if(localLife.cover.length()!=0){
            GlideImage.loadImage(context,holder.imgDes,localLife.cover, R.mipmap.img_error);
        }else{
            holder.imgDes.setImageResource(R.mipmap.img_error);
        }
        holder.tvName.setText(localLife.goodsName);
        if(localLife.price>0){
            holder.tvPrice.setText("¥"+ StringUtils.getStringtodouble(localLife.price-localLife.deduPoint)+"+抵用金"+StringUtils.getStringtodouble(localLife.deduPoint));
        }else {
            holder.tvPrice.setText("¥"+ StringUtils.getStringtodouble(localLife.price));
        }

        holder.tvCount.setText("数量："+localLife.count+"份");
        return convertView;
    }
    private class ViewHolder{
       private TextView tvName,tvPrice,tvCount;
        private ImageView imgDes;
    }
}
