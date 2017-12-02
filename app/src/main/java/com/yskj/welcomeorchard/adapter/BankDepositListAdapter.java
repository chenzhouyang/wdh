package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.BankListEntity;
import com.yskj.welcomeorchard.ui.deposit.DepositToBankActivity;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class BankDepositListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BankListEntity.DataBean> list;
    private  BankListEntity.DataBean  databean;
    public BankDepositListAdapter(Context context, ArrayList<BankListEntity.DataBean> list){
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if(list!= null&&list.size()!=0){
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
        ViewBankHolder holder = null;
        if(convertView == null){
            holder = new ViewBankHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank,null);
            holder.item_bank_image = (ImageView) convertView.findViewById(R.id.item_bank_image);
            holder.item_bank_name = (TextView) convertView.findViewById(R.id.item_bank_name);
            holder.item_bank_number = (TextView) convertView.findViewById(R.id.item_bank_number);
            holder.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
            convertView.setTag(holder);
        }else {
            holder = (ViewBankHolder) convertView.getTag();
        }
        holder.item_bank_name.setText(list.get(position).name);
        holder.item_bank_number.setText(list.get(position).cardNo);

        switch (list.get(position).bank){
            case "建设银行":
                holder.item_bank_image.setImageResource(R.mipmap.jian_pay_ico);
                break;
            case "中国银行":
                holder.item_bank_image.setImageResource(R.mipmap.china_pay_ico);
                break;
            case "农业银行":
                holder.item_bank_image.setImageResource(R.mipmap.long_pay_ico);
                break;
            case "工商银行":
                holder.item_bank_image.setImageResource(R.mipmap.buess_pay_ico);
                break;
            case "招商银行":
                holder.item_bank_image.setImageResource(R.mipmap.zhao_pay_ico);
                break;
            case "交通银行":
                holder.item_bank_image.setImageResource(R.mipmap.jiao_pay_ico);
                break;
            case "支付宝":
                holder.item_bank_image.setImageResource(R.mipmap.zhifu_pay_ico);
                break;
            case "微信":
                holder.item_bank_image.setImageResource(R.mipmap.wei_pay_ico);
                break;
        }
        holder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).bank.equals("微信")){
                    Toast.makeText(context,"暂不支持微信提现",Toast.LENGTH_SHORT).show();
                }else {
                    context.startActivity(new Intent(context,DepositToBankActivity.class).putExtra("cardid",list.get(position).id+""));
                }

            }
        });
        return convertView;
    }
     class ViewBankHolder{
        ImageView item_bank_image;
        TextView item_bank_name,item_bank_number;
        LinearLayout ll_layout;

    }
}
