package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AllFragmentHeadViewEntity;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/10on 15:49.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AdverMainDialogGVAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AllFragmentHeadViewEntity> arrayList;

    public AdverMainDialogGVAdapter(Context context, ArrayList<AllFragmentHeadViewEntity> arrayList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_fragment_head_gridview,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(arrayList.get(position).getName());
        holder.imageView.setImageResource(arrayList.get(position).getIconId());
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
        TextView title;
        public ViewHolder(View itemview) {
            title = (TextView) itemview.findViewById(R.id.tv_title);
            imageView = (ImageView) itemview.findViewById(R.id.img);
        }
    }
}
