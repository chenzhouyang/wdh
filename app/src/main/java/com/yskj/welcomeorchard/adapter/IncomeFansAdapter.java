package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.IncomeEntitiy;
import com.yskj.welcomeorchard.utils.Level;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class IncomeFansAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<IncomeEntitiy.DataBean> list;
    public IncomeFansAdapter(Context context, ArrayList<IncomeEntitiy.DataBean> list){
        super();
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewIncomeHolder holder = null;
        if(convertView == null){
            holder = new ViewIncomeHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_income,null);
            holder.item_income_grand = (TextView) convertView.findViewById(R.id.item_income_grand);
            holder.item_income_name = (TextView) convertView.findViewById(R.id.item_income_name);
            holder.item_income_title = (TextView) convertView.findViewById(R.id.item_income_title);
            holder.item_income_tv = (TextView) convertView.findViewById(R.id.item_income_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewIncomeHolder) convertView.getTag();
        }
        IncomeEntitiy.DataBean dataBean = list.get(position);
        holder.item_income_grand.setText(Level.geterr_code(dataBean.level));
        holder.item_income_name.setText(dataBean.mobile+"");
        holder.item_income_title.setText(dataBean.nickName);
        holder.item_income_tv.setText(dataBean.createTime);
        return convertView;
    }
    public class ViewIncomeHolder{
        TextView item_income_name,item_income_title,item_income_grand,item_income_tv;
    }
}
