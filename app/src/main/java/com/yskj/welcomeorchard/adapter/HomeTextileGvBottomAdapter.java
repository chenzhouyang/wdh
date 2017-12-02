package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.WDHFragmentNewPeopleEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/17on 18:25.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class HomeTextileGvBottomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WDHFragmentNewPeopleEntity.AdListBean> arrayList;

    public HomeTextileGvBottomAdapter(Context context, ArrayList<WDHFragmentNewPeopleEntity.AdListBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (arrayList == null || arrayList.size() == 0)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hometextile_gv_bottom,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideImage.loadImage(context,holder.imageView,arrayList.get(position).adCode,R.mipmap.img_error);
        holder.tvName.setText(arrayList.get(position).adName);
        holder.tvDes.setText("立即购买");
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
        TextView tvName,tvDes;
        public ViewHolder(View itemview) {
            imageView = (ImageView) itemview.findViewById(R.id.img);
            tvName = (TextView) itemview.findViewById(R.id.tv_name);
            tvDes = (TextView) itemview.findViewById(R.id.tv_des);
        }
    }
}
