package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.ChooseSampleEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/22on 9:36.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ChooseSampleAdapter extends BaseAdapter {
    private ArrayList<ChooseSampleEntity.AdrListBean> arrayList;
    private Context context;

    public ChooseSampleAdapter(ArrayList<ChooseSampleEntity.AdrListBean> arrayList, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_version_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideImage.loadImage(context,holder.imageView,arrayList.get(position).thumb,R.mipmap.img_error);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemview) {
            imageView = (ImageView) itemview.findViewById(R.id.img);
        }
    }
}
