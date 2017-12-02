package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.ChooseSampleEntity;
import com.yskj.welcomeorchard.entity.ParticularsEntitiy;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期 2017/5/22on 9:36.
 * 描述：
 * 作者：
 */

public class SourceParticularsAdapter extends BaseAdapter {
    private List<ParticularsEntitiy.DataBean.ContentListBean> arrayList;
    private Context context;

    public SourceParticularsAdapter(Context context,List<ParticularsEntitiy.DataBean.ContentListBean> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_version_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(arrayList.get(position).content.contains("http://")){
            holder.imageView.setVisibility(View.VISIBLE);
            holder.tv_item.setVisibility(View.GONE);
            GlideImage.loadImage(context,holder.imageView,arrayList.get(position).content,R.mipmap.img_error);
        }else {
            holder.tv_item.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            if(arrayList.get(position).content.contains("<P")||arrayList.get(position).content.contains("<div")||arrayList.get(position).content.contains("<ul")){
                holder.tv_item.setText(Html.fromHtml(arrayList.get(position).content));
            }else {
                holder.tv_item.setText(arrayList.get(position).content);
            }

        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tv_item;
        public ViewHolder(View itemview) {
            imageView = (ImageView) itemview.findViewById(R.id.img);
            tv_item = (TextView) itemview.findViewById(R.id.tv_item);
        }
    }
}
