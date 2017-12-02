package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AllFragmentListTimeEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.NumberFormat;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/12on 19:01.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentHoriListAdapter extends RecyclerView.Adapter<AllFragmentHoriListAdapter.MyViewHolder > {
    private Context context;
    private ArrayList<AllFragmentListTimeEntity.PromotionListBean.GoodsListBean> arrayList;
    private OnItemClickListener mOnItemClickListener;
    private String activity_code;

    public AllFragmentHoriListAdapter(Context context,String activity_code,ArrayList<AllFragmentListTimeEntity.PromotionListBean.GoodsListBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity_code = activity_code;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_fragment_hori_list,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        AllFragmentListTimeEntity.PromotionListBean.GoodsListBean goodsListBean = arrayList.get(position);
        holder.tvName.setText(goodsListBean.goodsName);
        holder.tvPrice.setText("￥ "+ String.format("%.2f", NumberFormat.convertToDouble(goodsListBean.price, 0.00)));
        GlideImage.loadImage(context,holder.img,goodsListBean.originalFuImg,R.mipmap.img_error);
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
        if(activity_code!=null&&activity_code.equals("n_to_buy")){
            if(goodsListBean.is_open!=null&& arrayList.get(position).is_end.equals("1")){
                holder.llStatus.setGravity(Gravity.CENTER);
                holder.imgStatus.setImageResource(R.mipmap.one_buy_end);
                holder.itemAllHori.setBackgroundResource(R.drawable.bg_corner_f2f2f2);
                return;
            }
            if (goodsListBean.is_open!=null&& arrayList.get(position).is_open.equals("0")){
                holder.llStatus.setGravity(Gravity.RIGHT);
                holder.imgStatus.setImageResource(R.mipmap.one_buy_ready);
                holder.itemAllHori.setBackgroundResource(R.drawable.bg_corner_f2f2f2);
                return;
            }
        }else {
            holder.itemAllHori.setBackgroundResource(R.drawable.bg_corner_d2d2d2);
        }
        if(goodsListBean.is_open!=null&& arrayList.get(position).is_end.equals("2")){
            holder.llStatus.setGravity(Gravity.CENTER);
            holder.imgStatus.setImageResource(R.mipmap.one_buy_sold_out);
            holder.itemAllHori.setBackgroundResource(R.drawable.bg_corner_f2f2f2);
            return;
        }
    }


    @Override
    public int getItemCount() {
        if (arrayList == null || arrayList.size() == 0)
            return 0;
        return arrayList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName,tvPrice;
        LinearLayout itemAllHori,llStatus;
        ImageView imgStatus;
        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            img = (ImageView) view.findViewById(R.id.img);
            itemAllHori = (LinearLayout) view.findViewById(R.id.item_all_hori);
            llStatus = (LinearLayout) view.findViewById(R.id.ll_status);
            imgStatus = (ImageView) view.findViewById(R.id.img_status);
        }
    }

    public interface OnItemClickListener{
        void onItemClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}
