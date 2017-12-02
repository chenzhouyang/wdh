package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.AllFragmentListRecommendEntity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.NumberFormat;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/12on 15:46.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentListRecommendAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AllFragmentListRecommendEntity.GoodsListBean> arrayList;

    public AllFragmentListRecommendAdapter(Context context, ArrayList<AllFragmentListRecommendEntity.GoodsListBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (arrayList == null || arrayList.size() == 0)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_fragment_list_recommend,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AllFragmentListRecommendEntity.GoodsListBean goodsList = arrayList.get(position);

        holder.tvName.setText(goodsList.goodsName);
        GlideImage.loadImage(context,holder.img,goodsList.originalImg,R.mipmap.img_error);
        String goodsSn = goodsList.goodsSn;
        holder.tvSn.setVisibility(View.GONE);
        holder.tvSn.setText("赠送" + Math.floor(Double.parseDouble(arrayList.get(position).giveIntegral)/3)+"积分");
        Spannable spannable = new SpannableString("￥" + String.format("%.2f", NumberFormat.convertToDouble(goodsList.shopPrice, 0d)));
        spannable.setSpan(new TextAppearanceSpan(context, R.style.price), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new TextAppearanceSpan(context, R.style.price), 1, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvPrice.setText(spannable, TextView.BufferType.SPANNABLE);
        holder.item_recommend_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CommodityDetailsActiviy.class).putExtra("goodid",arrayList.get(position).goodsId));            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView tvName,tvSn,tvPrice;
        LinearLayout item_recommend_ll;
        public ViewHolder(View itemview) {
            tvPrice = (TextView) itemview.findViewById(R.id.tv_price);
            tvName = (TextView) itemview.findViewById(R.id.tv_name);
            tvSn = (TextView) itemview.findViewById(R.id.tv_sn);
            img = (ImageView) itemview.findViewById(R.id.img);
            item_recommend_ll = (LinearLayout) itemview.findViewById(R.id.item_recommend_ll);
        }
    }
}
