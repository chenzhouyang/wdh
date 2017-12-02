package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.ArticleListEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.List;

import static com.yskj.welcomeorchard.R.id.fragment_foundation;

/**
 * Created by YSKJ-02 on 2017/3/23.
 */

public class ImageFragMentAdapter extends BaseAdapter {
    private Context context;
    private List<ArticleListEntity.AdListBean> adListBeen;
    public ImageFragMentAdapter(Context context,List<ArticleListEntity.AdListBean> adListBeen){
        super();
        this.context = context;
        this.adListBeen = adListBeen;
    }
    @Override
    public int getCount() {
        return adListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return adListBeen.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fragment_listview,null);
            holder.fragment_foundation = (ImageView) convertView.findViewById(fragment_foundation);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideImage.loadImage(context,holder.fragment_foundation,adListBeen.get(position).thumb,R.mipmap.default_image);
        return convertView;
    }
    public class ViewHolder{
        ImageView fragment_foundation;
    }
}
