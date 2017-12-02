package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.OrderListEntity;
import com.yskj.welcomeorchard.utils.GlideImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class SuuplyConfirmItemApapter extends BaseAdapter {
    private Context context;
    private List<LocalServerNumEntity> orderListBeen;
    private String goodsid;
    public SuuplyConfirmItemApapter(Context context,String goodsid, List<LocalServerNumEntity> orderListBeen){
        super();
        this.context = context;
        this.orderListBeen = orderListBeen;
        this.goodsid = goodsid;
    }
    @Override
    public int getCount() {
        if (orderListBeen!=null&&orderListBeen.size()!=0){
            return orderListBeen.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return orderListBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_suuply_confirmlist, null);
            holder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
            holder.order_number = (TextView) convertView.findViewById(R.id.order_number);
            holder.order_image = (ImageView) convertView.findViewById(R.id.order_image);
            holder.order_paramert = (TextView) convertView.findViewById(R.id.order_paramert);
            holder.confirm_ll = (LinearLayout) convertView.findViewById(R.id.confirm_ll);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder.confirm_ll.setVisibility(View.VISIBLE);
            holder.order_name.setText(orderListBeen.get(position).getGoodsname());
            holder.order_number.setText("X"+orderListBeen.get(position).getCount());
            String goodsPrice = "￥"+orderListBeen.get(position).getPrice();
            holder.goods_price.setText(Html.fromHtml(goodsPrice));
            holder.order_paramert.setText(orderListBeen.get(position).getParametername());
            GlideImage.loadImage(context,holder.order_image,orderListBeen.get(position).getGoodsimage(),R.mipmap.img_error);
        return convertView;
    }
   public class ViewHolder{
       ImageView order_image;
       LinearLayout confirm_ll;
       TextView order_name,order_number,goods_price,order_paramert;

   }
}
