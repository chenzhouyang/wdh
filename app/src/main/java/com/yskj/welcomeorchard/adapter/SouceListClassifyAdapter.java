package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;

import java.util.ArrayList;

/**
 * 陈宙洋
 * 2017/8/3.
 */

public class SouceListClassifyAdapter extends RecyclerView.Adapter<SouceListClassifyAdapter.MyViewHolder> {
    private Context context;
    private SouceListClassifyAdapter.OnItemClickListener mOnItemClickListener;
    private ArrayList<String> arrayList;
    private int type;
    public SouceListClassifyAdapter(Context context,ArrayList<String> arrayList,int type){
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coucelist_classify, null);
        SouceListClassifyAdapter.MyViewHolder holder = new SouceListClassifyAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.item_coucelist_classify_tv.setText(arrayList.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (arrayList == null || arrayList.size() == 0)
            return 0;
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_coucelist_classify_tv;
        public MyViewHolder(View view) {
            super(view);
            item_coucelist_classify_tv = (TextView) view.findViewById(R.id.item_coucelist_classify_tv);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(SouceListClassifyAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
