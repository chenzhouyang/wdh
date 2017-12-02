package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.IntroOrderEntity;
import com.yskj.welcomeorchard.home.IntroActivity;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class IntroOrderAdapter extends BaseAdapter {
    private Context context;
    private List<IntroOrderEntity.DataBean> beanList;
    public IntroOrderAdapter(Context context,List<IntroOrderEntity.DataBean> beanList){
        super();
        this.context  = context;
        this.beanList = beanList;
    }
    @Override
    public int getCount() {
        if(beanList.size()!=0){
            return beanList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return beanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_introorder,null);
            holder.again_submit = (TextView) convertView.findViewById(R.id.again_submit);
            holder.introorder_status = (TextView) convertView.findViewById(R.id.introorder_status);
            holder.introorder_remit = (TextView) convertView.findViewById(R.id.introorder_remit);
            holder.introorder_name = (TextView) convertView.findViewById(R.id.introorder_name);
            holder.introorder_time = (TextView) convertView.findViewById(R.id.introorder_time);
            holder.introorder_order = (TextView) convertView.findViewById(R.id.introorder_order);
            holder.tickling = (TextView) convertView.findViewById(R.id.tickling);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String[] year = beanList.get(position).createTime.split(" ");
        holder.introorder_time.setText(year[0]);
        holder.introorder_order.setText("订单号："+beanList.get(position).orderNo);
        holder.introorder_name.setText("服务名："+beanList.get(position).goodName);
        holder.introorder_remit.setText("打款金额："+StringUtils.getStringtodouble(beanList.get(position).amount));
        if(beanList.get(position).status == 1){
            holder.introorder_status.setText("预约状态："+"待审批");
        }else if(beanList.get(position).status == 2){
            holder.introorder_status.setText("预约状态："+"审批不通过");
            holder.again_submit.setVisibility(View.VISIBLE);
        }else if(beanList.get(position).status == 3){
            holder.introorder_status.setText("预约状态："+"审核通过");
        } else if (beanList.get(position).status == 4) {
            holder.introorder_status.setText("预约状态："+"预约订单作废");
        }
        holder.tickling.setText("反馈信息："+beanList.get(position).remark);
        holder.again_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,IntroActivity.class).putExtra("discern","1").putExtra("id",beanList.get(position).id+""));
            }
        });
        return convertView;
    }
    public class ViewHolder{
        TextView introorder_order,introorder_time,introorder_name,introorder_remit,introorder_status,again_submit,tickling;
    }

}
