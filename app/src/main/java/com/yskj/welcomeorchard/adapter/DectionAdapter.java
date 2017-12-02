package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.DectionEntity;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/5/16.
 */

public class DectionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DectionEntity.DataBean.ListBean> dectionEntityArrayList;
    public DectionAdapter(Context context, ArrayList<DectionEntity.DataBean.ListBean> dectionEntityArrayList){
        this.context = context;
        this.dectionEntityArrayList = dectionEntityArrayList;
    }
    @Override
    public int getCount() {
        if(dectionEntityArrayList!=null&&dectionEntityArrayList.size()!=0){
            return dectionEntityArrayList.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return dectionEntityArrayList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dection,null);
            holder.dection_name = (TextView) convertView.findViewById(R.id.dection_name);
             holder.dection_amount = (TextView) convertView.findViewById(R.id.dection_amount);
            /*holder.dection_degree = (TextView) convertView.findViewById(R.id.dection_degree);*/
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dection_name.setText(dectionEntityArrayList.get(position).name);
        holder.dection_amount.setText("¥"+StringUtils.getStringtodouble(dectionEntityArrayList.get(position).price));
       /* holder.dection_degree.setText(dectionEntityArrayList.get(position).getDegree()+"次");*/
        return convertView;
    }
    public class ViewHolder{
        TextView dection_name,dection_amount,dection_degree;
    }
}
