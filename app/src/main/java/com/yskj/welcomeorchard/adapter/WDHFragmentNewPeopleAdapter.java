package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.WDHFragmentNewPeopleEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/14on 18:25.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class WDHFragmentNewPeopleAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WDHFragmentNewPeopleEntity.AdListBean> arrayList;

    public WDHFragmentNewPeopleAdapter(Context context, ArrayList<WDHFragmentNewPeopleEntity.AdListBean> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wdh_fragment_new_people,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideImage.loadImage(context,holder.imageView,arrayList.get(position).adCode,R.mipmap.img_error);
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemview) {
            imageView = (ImageView) itemview.findViewById(R.id.image);
        }
    }
}
