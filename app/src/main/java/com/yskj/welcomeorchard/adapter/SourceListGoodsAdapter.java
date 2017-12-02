package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.SupplyEntity;
import com.yskj.welcomeorchard.ui.supply.SupplyActivity;
import com.yskj.welcomeorchard.utils.DateUtils;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * 陈宙洋
 * 2017/8/3.
 */

public class SourceListGoodsAdapter extends RecyclerView.Adapter<SourceListGoodsAdapter.MyViewHolder>{
    private SourceListGoodsAdapter.OnItemClickListener mOnItemClickListener;
    private Context context;
    private List<SupplyEntity.DataBean.GoodsListBean> list;
    public SourceListGoodsAdapter(Context context,List<SupplyEntity.DataBean.GoodsListBean> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_source_goods, null);
        SourceListGoodsAdapter.MyViewHolder holder = new SourceListGoodsAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GlideImage.loadImage(context,holder.item_source_image,list.get(position).cover,R.mipmap.default_image);
        holder.item_source_title.setText(list.get(position).name);
        holder.item_source_abstract.setText(list.get(position).volume+"件已售");
        holder.item_source_price.setText("¥"+StringUtils.getStringtodouble(list.get(position).price));
        GlideImage.loadImage(context,holder.item_source_image,list.get(position).cover,R.mipmap.img_error);
        holder.consume_integral.setText("赠送积分"+StringUtils.getStringtodouble(DoubleUtils.div(list.get(position).mAccount,10,2)));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
        holder.souce_goods_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,SupplyActivity.class).putExtra("millid",list.get(position).millId+""));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0)
            return 0;
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_source_image,souce_goods_image;
        TextView item_source_title,item_source_abstract,item_source_price,consume_integral;
        public MyViewHolder(View view) {
            super(view);
            item_source_title = (TextView) view.findViewById(R.id.item_source_title);
            item_source_price = (TextView) view.findViewById(R.id.item_source_price);
            item_source_abstract = (TextView) view.findViewById(R.id.item_source_abstract);
            item_source_image = (ImageView) view.findViewById(R.id.item_source_image);
            consume_integral = (TextView) view.findViewById(R.id.consume_integral);
            souce_goods_image= (ImageView) view.findViewById(R.id.souce_goods_image);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(SourceListGoodsAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
