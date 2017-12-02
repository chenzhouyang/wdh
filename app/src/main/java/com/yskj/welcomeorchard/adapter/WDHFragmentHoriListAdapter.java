package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.WDHFragmentTopRecycleEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/14on 18:59.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class WDHFragmentHoriListAdapter  extends RecyclerView.Adapter<WDHFragmentHoriListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean> arrayList;
    private OnItemClickListener mOnItemClickListener;

    public WDHFragmentHoriListAdapter(Context context, ArrayList<WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wdhfragment_hori_adapter, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean goodsList = arrayList.get(position);
        GlideImage.loadImage(context,holder.img_goods,goodsList.image,R.mipmap.img_error);
        holder.tv_name.setText(goodsList.name);
        if (position==arrayList.size()-1){
            holder.linearLayout.setVisibility(View.GONE);
        }else {
            holder.linearLayout.setVisibility(View.VISIBLE);
        }
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
        ImageView img_goods;
        TextView tv_name;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            img_goods = (ImageView) view.findViewById(R.id.img);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_line);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
