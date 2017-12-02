package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.OrderListEntity;
import com.yskj.welcomeorchard.ui.order.OrderDetailActivity;
import com.yskj.welcomeorchard.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderListEntity.OrderListBean> list;
    public OrderListAdapter(Context context,ArrayList<OrderListEntity.OrderListBean> list){
        super();
        this.context =context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null&&list.size()!=0){
            return list.size();
        }
        return 0;
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
        ViewHolder holder = null;
        if(convertView ==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_list,null);
            holder.goods_list_lisetview = (NoScrollListView) convertView.findViewById(R.id.goods_list_lisetview);
            holder.tv_cost_jifen = (TextView) convertView.findViewById(R.id.tv_cost_jifen);
            holder.order_money = (TextView) convertView.findViewById(R.id.order_money);
            holder.order_freight = (TextView) convertView.findViewById(R.id.order_freight);
            holder.order_list_ll = (LinearLayout) convertView.findViewById(R.id.order_list_ll);
            convertView.setTag(holder);
       }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.size()!=0){
            ArrayList<OrderListEntity.OrderListBean.GoodsListBean> goodslist = list.get(position).goodsList;
            holder.tv_cost_jifen.setText("￥"+list.get(position).userMoney);
            holder.order_money.setText("￥"+list.get(position).orderAmount);
            holder.order_freight.setText("￥"+list.get(position).shippingPrice);
            OrderGoodsApapter goodsadapter = new OrderGoodsApapter(context,goodslist);
            holder.goods_list_lisetview.setAdapter(goodsadapter);
            holder.order_list_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    OrderListEntity.OrderListBean orderListBean = list.get(position);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("orderbean",orderListBean);
                    intent.putExtras(mBundle);
                    context.startActivity(intent);
                }
            });
        }else {
            Toast.makeText(context,"暂无数据",Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }
    public class ViewHolder{
        TextView  tv_cost_jifen,order_money,order_freight;
                NoScrollListView goods_list_lisetview;
        LinearLayout order_list_ll;
    }
}