package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerNearPopBean;

import java.util.ArrayList;

/**
 * Created by jianghe on 2016/11/10 0010.
 */
public class MerChantAdapter extends BaseAdapter {


    private Context context;

    private LayoutInflater inflater;

    private ArrayList<LocalServerNearPopBean.RetData> items;

    private int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


    public MerChantAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }



    public void setItems(ArrayList<LocalServerNearPopBean.RetData> items) {
        this.items = items;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
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
        if(convertView == null){

            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.root_listview_item, parent , false);
            holder.item_text =(TextView) convertView.findViewById(R.id.item_name_text);
            holder.item_layout = (LinearLayout)convertView.findViewById(R.id.root_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        /**
         * 该项被选中时改变背景色
         */
        if(selectedPosition == position){
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.WHITE);
        }else{
//          Drawable item_bg = new ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.item_text.setText(items.get(position).name);
        return convertView;
    }
    class ViewHolder{
        TextView item_text;
        LinearLayout item_layout;
    }

}