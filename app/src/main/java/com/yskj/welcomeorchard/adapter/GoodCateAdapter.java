package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.GoodsCategoryListEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/12.
 */

public class GoodCateAdapter extends BaseAdapter {
    // 定义Context
    private LayoutInflater mInflater;
    private ArrayList<GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean> list;
    private Context context;
    private GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean type;
    public GoodCateAdapter(Context context,ArrayList<GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean> list){
        mInflater=LayoutInflater.from(context);
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        if(list!=null&&list.size()>0)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyView view;
        if(convertView==null){
            view=new MyView();
            convertView=mInflater.inflate(R.layout.item_good_cate_type, null);
            view.icon=(ImageView)convertView.findViewById(R.id.typeicon);
            view.name=(TextView)convertView.findViewById(R.id.typename);
            convertView.setTag(view);
        }else{
            view=(MyView) convertView.getTag();
        }
        if(list!=null&&list.size()>0)
        {
            type=list.get(position);
            view.name.setText(type.mobileName);
            GlideImage.loadImage(context,view.icon,type.image,R.mipmap.img_error);
        }
        return convertView;
    }
    private class MyView{
        private ImageView icon;
        private TextView name;
    }
}
