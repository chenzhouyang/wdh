package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.OneBuyGotRecordEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.CircleImageView;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/27on 10:39.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OneBuyGotRecordAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OneBuyGotRecordEntity.DataBean.ListBean> arrayList;
    private String type;

    public OneBuyGotRecordAdapter(Context context, ArrayList<OneBuyGotRecordEntity.DataBean.ListBean> arrayList,String type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_one_buy_got_record,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        OneBuyGotRecordEntity.DataBean.ListBean listBean = arrayList.get(position);
        if (type.equals("0")){
            switch (listBean.rank){
                case 1:
                    holder.imgStatus.setImageResource(R.mipmap.one_buy_got_first);
                    break;
                case 2:
                    holder.imgStatus.setImageResource(R.mipmap.one_buy_got_second);
                    break;
                case 3:
                    holder.imgStatus.setImageResource(R.mipmap.one_buy_got_three);
                    break;
            }
        }else {
            holder.imgStatus.setImageResource(R.mipmap.one_buy_got);
        }
        GlideImage.loadImage(context,holder.imgUser, listBean.avatar,R.mipmap.default_image);
        holder.tvName.setText(listBean.nickName);
        holder.tvPhone.setText(listBean.mobile);

        return convertView;
    }

    private class ViewHolder {
        CircleImageView imgUser;
        TextView tvName,tvPhone;
        ImageView  imgStatus;
        public ViewHolder(View itemView) {
            imgUser = (CircleImageView) itemView.findViewById(R.id.img_user);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            imgStatus = (ImageView) itemView.findViewById(R.id.img_status);
        }
    }

}
