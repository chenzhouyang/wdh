package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerDetailBean;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class LocalServerDetailMealAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LocalServerDetailBean.SetMealListBean> localServerMealBeen;

    public LocalServerDetailMealAdapter(Context context, ArrayList<LocalServerDetailBean.SetMealListBean> localServerMealBeen) {
        this.context = context;
        this.localServerMealBeen = localServerMealBeen;
    }
    @Override
    public int getCount() {
        if (localServerMealBeen==null||localServerMealBeen.size()==0){
            return 0;
        }
        return localServerMealBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return localServerMealBeen.get(i);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_local_server_detail, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvSend = (TextView) convertView.findViewById(R.id.tv_send);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        LocalServerDetailBean.SetMealListBean setMall = localServerMealBeen.get(i);
        holder.tvName.setText(setMall.name);
        holder.tvPrice.setText("￥"+ StringUtils.getStringtodouble(setMall.price));
        holder.tvSend.setText(setMall.count+"份");
        return convertView;
    }

    private class ViewHolder {
        private TextView tvName,tvPrice,tvSend;
    }
}
