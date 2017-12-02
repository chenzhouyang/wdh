package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.NewLocalGoodsListEntity;
import com.yskj.welcomeorchard.utils.GlideCircleTransform;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/10on 11:19.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class NewLocalGoodsListAdapter extends RecyclerView.Adapter<NewLocalGoodsListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NewLocalGoodsListEntity.DataBean.LocalShopsBean> arrayList;
    private OnItemClickListener mOnItemClickListener;
    public NewLocalGoodsListAdapter(Context context, ArrayList<NewLocalGoodsListEntity.DataBean.LocalShopsBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_local_goods_list,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NewLocalGoodsListEntity.DataBean.LocalShopsBean goodsList = arrayList.get(position);
        Glide.with(context).load(goodsList.cover).transform(new CenterCrop(context),new GlideCircleTransform(context,3)).error(R.mipmap.img_error).into(holder.imgDes);
//        GlideImage.loadImage(context,holder.imgDes,goodsList.cover,R.mipmap.img_error);
        holder.tvName.setText(goodsList.shopName);
        holder.tvCityName.setText(goodsList.address);
        holder.tvPerson.setText(Html.fromHtml("共<font color = \"#fa2f5a\">"+goodsList.saleCount+"</font>人消费"));
        holder.tvDistance.setText(goodsList.distanceString);
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
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
        private TextView tvName,tvPerson,tvCityName,tvDistance;
        private ImageView imgDes;
        public MyViewHolder(View view) {
            super(view);
            imgDes = (ImageView) view.findViewById(R.id.image_des);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDistance = (TextView) view.findViewById(R.id.tv_distance);
            tvPerson = (TextView) view.findViewById(R.id.tv_person);
            tvCityName = (TextView) view.findViewById(R.id.tv_city_name);
        }
    }

    public interface OnItemClickListener{
        void onItemClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}
