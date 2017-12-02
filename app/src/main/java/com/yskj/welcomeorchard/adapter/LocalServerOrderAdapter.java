package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerOrderBean;
import com.yskj.welcomeorchard.ui.localserver.LocalServerOrderDetailActivity;
import com.yskj.welcomeorchard.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * Created by jianghe on 2016/11/14 0014.
 */
public class LocalServerOrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LocalServerOrderBean.DataBean.OrderVosBean> arrayList;

    public LocalServerOrderAdapter(Context context, ArrayList<LocalServerOrderBean.DataBean.OrderVosBean> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local_server_order, null);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.item_local_nolistview = (NoScrollListView) convertView.findViewById(R.id.item_local_nolistview);
            holder.item_local_name = (TextView) convertView.findViewById(R.id.item_local_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        final LocalServerOrderBean.DataBean.OrderVosBean localLife = arrayList.get(i);
        holder.item_local_name.setText("店铺："+localLife.shopName);
        switch (localLife.status){
            case 0:
                holder.tvStatus.setText("去使用");
                break;
            case 1:
                holder.tvStatus.setText("已使用");
                break;
            case 2:
                holder.tvStatus.setText("退款/售后");
                break;
        }
        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(localLife.status == 0){
                    Intent intent = new Intent(context, LocalServerOrderDetailActivity.class);
                    intent.putExtra("status", localLife.status + "");
                    intent.putExtra("payCode", localLife.consumePassword);
                    intent.putExtra("orderId", localLife.cashCouponId + "");
                    if (localLife.consumePassword== null ||
                            localLife.consumePassword.equals("") ||
                            localLife.consumePassword.length() == 0) {
                        Toast.makeText(context,"暂时没有付款码",Toast.LENGTH_SHORT).show();
                    } else {
                        context.startActivity(intent);
                    }
                }

            }
        });
        if(localLife.createTime.length()==0){
            holder.tvOrderTime.setVisibility(View.GONE);
        }else {
            holder.tvOrderTime.setText("下单时间："+localLife.createTime);
        }
        LocalServierItemAdapter adapter = new LocalServierItemAdapter(context,localLife.goodsList);
        holder.item_local_nolistview.setAdapter(adapter);
        return convertView;
    }

    private class ViewHolder {
        private TextView tvStatus,tvOrderTime,item_local_name;
        private NoScrollListView item_local_nolistview;

    }
}
