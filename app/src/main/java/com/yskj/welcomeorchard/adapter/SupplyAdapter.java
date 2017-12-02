package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.dialog.StoreSupplyDialog;
import com.yskj.welcomeorchard.entity.SupplyEntity;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * 陈宙洋
 * 2017/8/2.
 */

public class SupplyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SupplyEntity.DataBean.GoodsListBean> list ;
    public SupplyAdapter(Context context,ArrayList<SupplyEntity.DataBean.GoodsListBean> list){
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if(convertView ==null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_supply,null);
            holder.item_supply_image = (ImageView) convertView.findViewById(R.id.item_supply_image);
            holder.item_supply_shopcar = (ImageView) convertView.findViewById(R.id.item_supply_shopcar);
            holder.item_supply_title = (TextView) convertView.findViewById(R.id.item_supply_title);
            holder.item_supply_abstract = (TextView) convertView.findViewById(R.id.item_supply_abstract);
            holder.item_supply_price = (TextView) convertView.findViewById(R.id.item_supply_price);
            holder.item_consume_integral = (TextView) convertView.findViewById(R.id.item_consume_integral);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.item_supply_title.setText(list.get(position).name);
        holder.item_supply_abstract.setText(list.get(position).name);
        holder.item_supply_price.setText("¥"+ StringUtils.getStringtodouble(list.get(position).price));
        holder.item_consume_integral.setText("赠送积分"+StringUtils.getStringtodouble(DoubleUtils.div(list.get(position).mAccount,10,2)));
        GlideImage.loadImage(context,holder.item_supply_image,list.get(position).cover,R.mipmap.img_error);
        holder.item_supply_shopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSupplyDialog dialog = new StoreSupplyDialog(context,list.get(position).goodId+"",list.get(position).cover,list.get(position).shopname);
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        return convertView;
    }
    class MyViewHolder  {
        ImageView item_supply_image,item_supply_shopcar;
        TextView item_supply_title,item_supply_abstract,item_supply_price,item_consume_integral;
    }
}
