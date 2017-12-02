package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.SupplyOrderEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;

import java.util.List;

/**
 * 作者：陈宙洋
 * 日期：2017/8/14.
 * 描述：订单中的商品列表
 */

public class SupplyOrderAdapter extends BaseAdapter {
    private Context context;
    private List<SupplyOrderEntity.DataBean.ListBean.GoodsListBean> goodsListBeen;
    public SupplyOrderAdapter(Context context, List<SupplyOrderEntity.DataBean.ListBean.GoodsListBean> goodsListBeen) {
        this.context = context;
        this.goodsListBeen = goodsListBeen;
    }

    @Override
    public int getCount() {
        return goodsListBeen == null?0:goodsListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsListBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_surce_ordergoods, null);
            mHolder = new ViewHolder();
            mHolder.surce_order_image = (ImageView) convertView.findViewById(R.id.surce_order_image);
            mHolder.surce_order_title = (TextView) convertView.findViewById(R.id.surce_order_title);
            mHolder.surce_order_specification = (TextView) convertView.findViewById(R.id.surce_order_specification);
            mHolder.surce_order_price = (TextView) convertView.findViewById(R.id.surce_order_price);
            mHolder.surce_order__number = (TextView) convertView.findViewById(R.id.surce_order__number);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        SupplyOrderEntity.DataBean.ListBean.GoodsListBean goodsListBean = goodsListBeen.get(position);
        GlideImage.loadImage(context,mHolder.surce_order_image,goodsListBean.cover,R.mipmap.img_error);
        mHolder.surce_order_title.setText(goodsListBean.goodsName);
        mHolder.surce_order_specification.setText(goodsListBean.specName);
        mHolder.surce_order_price.setText("¥"+StringUtils.getStringtodouble(goodsListBean.specPrice));
        mHolder.surce_order__number.setText("X"+goodsListBean.goodsCount);

        return convertView;
    }
    class ViewHolder{
        ImageView surce_order_image;
        TextView surce_order_title,surce_order_specification,surce_order_price,surce_order__number;
    }
}
