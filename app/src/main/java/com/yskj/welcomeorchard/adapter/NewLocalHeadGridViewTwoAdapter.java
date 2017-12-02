package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yskj.welcomeorchard.R;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/10on 15:36.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class NewLocalHeadGridViewTwoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> arrayList;
    private OnItemClickListener mOnItemClickListener;
    public NewLocalHeadGridViewTwoAdapter(Context context, ArrayList<Integer> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_head_graiview_two,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(arrayList.get(position));
        if( mOnItemClickListener!= null){
            convertView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemview) {
            imageView = (ImageView) itemview.findViewById(R.id.img);
        }
    }
    public interface OnItemClickListener{
        void onItemClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}
