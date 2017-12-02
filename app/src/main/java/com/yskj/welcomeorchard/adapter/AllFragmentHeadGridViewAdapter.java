package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AllFragmentHeadViewEntity;
import com.yskj.welcomeorchard.fragment.FragmentInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/12on 8:48.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentHeadGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AllFragmentHeadViewEntity> arrayList;
    private FragmentInterface.OnGetUrlListener onGetUrlListener;

    public AllFragmentHeadGridViewAdapter(Context context, ArrayList<AllFragmentHeadViewEntity> arrayList,FragmentInterface.OnGetUrlListener onGetUrlListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onGetUrlListener = onGetUrlListener;
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
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("from", "allFragment");
                bundle.putString("position", position + "");
                onGetUrlListener.getBandle(bundle);
            }
        });
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
        TextView title;
        LinearLayout linearLayout;
        public ViewHolder(View itemview) {
            title = (TextView) itemview.findViewById(R.id.tv_title);
            imageView = (ImageView) itemview.findViewById(R.id.img);
            linearLayout = (LinearLayout) itemview.findViewById(R.id.item_all_fragment_head_ll);
        }
    }
}
