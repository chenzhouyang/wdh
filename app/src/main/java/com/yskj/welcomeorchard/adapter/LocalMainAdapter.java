package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerNearPopBean;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.CircleImageView;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/3/27.
 */

public class LocalMainAdapter extends BaseAdapter {
    private List<LocalServerNearPopBean.RetData> childrenArrayList;
    private Context context;
    public LocalMainAdapter(Context context,List<LocalServerNearPopBean.RetData> childrenArrayList){
        this.context = context;
        this.childrenArrayList = childrenArrayList;
    }
    @Override
    public int getCount() {
        return childrenArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenArrayList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item,null);
            holder.gridview_image = (CircleImageView) convertView.findViewById(R.id.gridview_image);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(childrenArrayList.get(position).name);
        GlideImage.loadImage(context,holder.gridview_image,childrenArrayList.get(position).icon,R.mipmap.img_error);
        return convertView;
    }
    public class ViewHolder{
        CircleImageView gridview_image;
        TextView tvName;
    }
}
