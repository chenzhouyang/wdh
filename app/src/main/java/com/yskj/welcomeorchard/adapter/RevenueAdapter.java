package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.http.RevenueEntiity;
import com.yskj.welcomeorchard.utils.ProfitEnum;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class RevenueAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RevenueEntiity.DataBean.ListBean> listBeen;
    public RevenueAdapter(Context context,ArrayList<RevenueEntiity.DataBean.ListBean> listBeen){
        super();
        this.context = context;
        this.listBeen = listBeen;
    }
    @Override
    public int getCount() {
        if(listBeen!=null&&listBeen.size()!=0){
            return listBeen.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return listBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewRevenueHolder holder = null;
        if(convertView == null){
            holder = new ViewRevenueHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_revenue,null);
            holder.leiing_text = (TextView) convertView.findViewById(R.id.leiing_text);
            holder.revenue_time = (TextView) convertView.findViewById(R.id.revenue_time);
            holder.tiem = (TextView) convertView.findViewById(R.id.tiem);
            holder.text_vb = (TextView) convertView.findViewById(R.id.text_vb);
            holder.yanse_re = (RelativeLayout) convertView.findViewById(R.id.yanse_re);
            holder.item_revenuse_ll = (LinearLayout) convertView.findViewById(R.id.item_revenuse_ll);
            convertView.setTag(holder);
        }else {
            holder = (ViewRevenueHolder) convertView.getTag();
        }
        RevenueEntiity.DataBean.ListBean redalist = listBeen.get(position);
        ProfitEnum profitEnum = ProfitEnum.getByType(redalist.profitType);
        if(profitEnum!=null){
            holder.text_vb.setTextColor(context.getResources().getColor(profitEnum.getProfitColor()));
            holder.yanse_re.setBackgroundResource(profitEnum.getLeiXingColor());
            holder.revenue_time.setText(profitEnum.getText());
            holder.leiing_text.setText(profitEnum.getLeixing());

            if(profitEnum.getProfit()){
                holder.text_vb.setText(String.format("+%s", StringUtils.getStringtodouble(redalist.amount)));
            }else {
                holder.text_vb.setText(String.format("-%s", StringUtils.getStringtodouble(redalist.amount)));
            }

            holder.tiem.setText(redalist.createTime);
        }else {
            holder.item_revenuse_ll.setVisibility(View.GONE);
        }


        return convertView;
    }
    public class ViewRevenueHolder{
        TextView leiing_text,revenue_time,tiem,text_vb;
        RelativeLayout yanse_re;
        LinearLayout item_revenuse_ll;
    }
}
