package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerNearPopBean;

import java.util.ArrayList;

/**
 * Created by jianghe on 2016/11/10 0010.
 */
public class SubListViewAdapter extends BaseAdapter {

    private ArrayList<ArrayList<LocalServerNearPopBean.Children>> sub_items;
    private Context context;
    private int root_position;
    private LayoutInflater inflater;

    public SubListViewAdapter(Context context, ArrayList<ArrayList<LocalServerNearPopBean.Children>> sub_items,
                              int position) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sub_items = sub_items;
        this.root_position = position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sub_items.get(root_position).size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return sub_items.get(root_position).get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.root_listview_item,
                    parent, false);
            holder.item_text = (TextView) convertView
                    .findViewById(R.id.item_name_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_text.setText(sub_items.get(root_position).get(position).name);
        return convertView;
    }
    class ViewHolder{
        TextView item_text;
    }

}
