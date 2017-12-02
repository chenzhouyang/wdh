package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.MyItem;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.CircleImageView;

import java.util.ArrayList;

/**
 * Created by jianghe on 2016/12/7 0007.
 */
public class ItemRedRecordAdapter extends  RecyclerView.Adapter<ItemRedRecordAdapter.MyViewHolder > {
    private Context context;
    private ArrayList<MyItem> arrayList;
    private OnInterfaceListener mOnItemClickListener;
    public ItemRedRecordAdapter(Context context, ArrayList<MyItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (arrayList.get(position).getRedName()==null||arrayList.get(position).getRedName().equals("")){
            holder.textView.setText("暂无昵称");
        }else {
            holder.textView.setText(arrayList.get(position).getRedName());
        }
        holder.get_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.getRed(position);
            }
        });
        holder.red_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.showdialog();
            }
        });
        Logger.d(arrayList.get(position).getRedTitle());
        if(arrayList.get(position).getRedTitle()!=null&&arrayList.get(position).getRedTitle().length()!=0){
            holder.red_title.setText(arrayList.get(position).getRedTitle());
        }else {
            holder.red_title.setText("广告红包");
        }
        holder.red_message.setText("广告红包让您的生活更便捷");
        GlideImage.loadImage(context,holder.circleImageView,arrayList.get(position).getRedImage(),R.mipmap.img_error);
        switch (arrayList.get(position).getRedSex()){
            case "":
                holder.imgRedRvType.setVisibility(View.GONE);
                break;
            case "0":
                holder.imgRedRvType.setVisibility(View.VISIBLE);
                holder.imgRedRvType.setImageResource(R.mipmap.red_woman_rv);
                break;
            case "1":
                holder.imgRedRvType.setVisibility(View.VISIBLE);
                holder.imgRedRvType.setImageResource(R.mipmap.red_man_rv);
                break;
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_red_record_adapter,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView,get_red,red_title,red_message;
        CircleImageView circleImageView;
        ImageView red_close,imgRedRvType;
        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_name);
            circleImageView = (CircleImageView) view.findViewById(R.id.circle_img);
            get_red = (TextView) view.findViewById(R.id.get_red);
            red_close = (ImageView) view.findViewById(R.id.red_close);
            red_title = (TextView) view.findViewById(R.id.red_title);
            red_message = (TextView) view.findViewById(R.id.red_message);
            imgRedRvType = (ImageView) view.findViewById(R.id.img_red_rv_type);
        }
    }
    @Override
    public int getItemCount() {
        if (arrayList == null || arrayList.size() == 0)
            return 0;
        return arrayList.size();
    }

    public interface OnInterfaceListener{
        void onItemClick( int position);
        //领取红包
        void getRed(int postion);
        //关闭弹出框
        void showdialog();
    }
    public void setInterfaceListener(OnInterfaceListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}
