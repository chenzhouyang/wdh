package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.WDHFragmentTopRecycleEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.List;

/**
 * 创建日期 2017/4/18on 10:00.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class HomeTextileTopRecycleAdapter extends  RecyclerView.Adapter<HomeTextileTopRecycleAdapter.MyViewHolder> {
    private Context context;
    private List<WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean> arrayList;
    private OnItemClickListener mOnItemClickListener;

    public HomeTextileTopRecycleAdapter(Context context, List<WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hometextile_top_recycle_adapter, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean goodsList = arrayList.get(position);
        GlideImage.loadImage(context,holder.img_goods,goodsList.image.toString(),R.mipmap.img_error);
        holder.tv_name.setText(goodsList.name);
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

        public MyViewHolder(View view) {
            super(view);
            img_goods = (ImageView) view.findViewById(R.id.img);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
