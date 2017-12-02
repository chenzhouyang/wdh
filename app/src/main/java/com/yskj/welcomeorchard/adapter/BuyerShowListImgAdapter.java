package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/22.
 */

public class BuyerShowListImgAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;

    public BuyerShowListImgAdapter(Context context, ArrayList<String> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_buy_show_list_img,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideImage.loadImage(context,holder.imageView,arrayList.get(position),R.mipmap.img_error);
        return convertView;
    }
    private class ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemview) {
            imageView = (ImageView) itemview.findViewById(R.id.img);
        }
    }
}
