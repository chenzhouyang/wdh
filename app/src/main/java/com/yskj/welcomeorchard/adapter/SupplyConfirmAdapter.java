package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.OrderListEntity;
import com.yskj.welcomeorchard.ui.order.OrderDetailActivity;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class SupplyConfirmAdapter extends BaseAdapter {
    private Context context;
    private List<LocalServerEntity> list;
    private List<LocalServerNumEntity> localServerNumEntityList;
    private List<LocalServerNumEntity> localServerNumEntities = new ArrayList<>();
    private double allAmount;
    private int allCount;

    public SupplyConfirmAdapter(Context context,List<LocalServerNumEntity> localServerNumEntityList, List<LocalServerEntity> list){
        super();
        this.context =context;
        this.list = list;
        this.localServerNumEntityList = localServerNumEntityList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_supply_list,null);
            holder.goods_list_lisetview = (NoScrollListView) convertView.findViewById(R.id.goods_list_lisetview);
            holder.order_sn = (TextView) convertView.findViewById(R.id.order_sn);
            convertView.setTag(holder);
       }else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.order_sn.setText(list.get(position).getShopname());
            localServerNumEntities.clear();
            for (int i = 0; i < localServerNumEntityList.size(); i++) {
                if(list.get(position).getShopid().equals(localServerNumEntityList.get(i).getShopid())){
                    localServerNumEntities.add(localServerNumEntityList.get(i));
                }
            }
            SuuplyConfirmItemApapter suuplyConfirmItemApapter = new SuuplyConfirmItemApapter(context,list.get(position).getGoodsid(),localServerNumEntities);
            holder.goods_list_lisetview.setAdapter(suuplyConfirmItemApapter);
        return convertView;
    }
    public class ViewHolder{
        TextView  order_sn;
        NoScrollListView goods_list_lisetview;

    }
}