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
import com.yskj.welcomeorchard.widget.CircleImageView;

import java.util.List;

/**
 * 创建日期 2017/4/17on 11:29.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class BeautyMakeupHeadGridAdapter  extends RecyclerView.Adapter<BeautyMakeupHeadGridAdapter.MyViewHolder> {
    private Context context;
    private List<WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean> arrayList;
    private int type;
    private String catGroup;
    private OnItemClickListener mOnItemClickListener;

    public BeautyMakeupHeadGridAdapter(Context context, List<WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean> arrayList,int type,String catGroup) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
        this.catGroup = catGroup;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_beauty_makeup_head_grid, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean.TmenuBean goodsList = arrayList.get(position);
        //如果catGroup==3 用圆图 icon ，否则用方形 image。
        if (catGroup.equals("3")){
            holder.circleImageView.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.GONE);
            switch (type){
                case 0:
                    GlideImage.loadImage(context,holder.circleImageView,goodsList.icon,R.mipmap.img_error);
                    holder.tvName.setText(goodsList.name);
                    break;
                case 1:
                    if (position==4){
                        holder.circleImageView.setImageResource(R.mipmap.fragment_more_cate1);
                        holder.tvName.setText("更多");
                    }else {
                        GlideImage.loadImage(context,holder.circleImageView,goodsList.icon,R.mipmap.img_error);
                        holder.tvName.setText(goodsList.name);
                    }
                    break;
                case 2:
                    if (position==9){
                        holder.circleImageView.setImageResource(R.mipmap.fragment_more_cate1);
                        holder.tvName.setText("更多");
                    }else {
                        GlideImage.loadImage(context,holder.circleImageView,goodsList.icon,R.mipmap.img_error);
                        holder.tvName.setText(goodsList.name);
                    }
                    break;

            }
        }else {
            holder.circleImageView.setVisibility(View.GONE);
            holder.img.setVisibility(View.VISIBLE);
            switch (type){
                case 0:
                    GlideImage.loadImage(context,holder.img,goodsList.image,R.mipmap.img_error);
                    holder.tvName.setText(goodsList.name);
                    break;
                case 1:
                    if (position==4){
                        holder.img.setImageResource(R.mipmap.fragment_more_cate);
                        holder.tvName.setText("更多");
                    }else {
                        GlideImage.loadImage(context,holder.img,goodsList.image,R.mipmap.img_error);
                        holder.tvName.setText(goodsList.name);
                    }
                    break;
                case 2:
                    if (position==9){
                        holder.img.setImageResource(R.mipmap.fragment_more_cate);
                        holder.tvName.setText("更多");
                    }else {
                        GlideImage.loadImage(context,holder.img,goodsList.image,R.mipmap.img_error);
                        holder.tvName.setText(goodsList.name);
                    }
                    break;

            }
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
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName;
        CircleImageView circleImageView;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            img = (ImageView) view.findViewById(R.id.img);
            circleImageView = (CircleImageView) view.findViewById(R.id.circle_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
