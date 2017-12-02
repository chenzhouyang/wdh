package com.yskj.welcomeorchard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.fragment.FragmentInterface;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.NumberFormat;
import com.yskj.welcomeorchard.utils.UMengShareUtils;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/13on 14:49.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentGoodsListAdapter extends RecyclerView.Adapter<AllFragmentGoodsListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<GoodsListEntity.GoodsListBean> arrayList;
    private FragmentInterface.OnGetUrlListener onGetUrlListener;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String promotioncode, umurl;

    private OnItemClickListener mOnItemClickListener;
    public AllFragmentGoodsListAdapter(Context context, ArrayList<GoodsListEntity.GoodsListBean> arrayList, FragmentInterface.OnGetUrlListener onGetUrlListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onGetUrlListener = onGetUrlListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_all_goods_adapter,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GoodsListEntity.GoodsListBean goodsList = arrayList.get(position);
        GlideImage.loadImage(context,holder.img_goods,goodsList.originalFuImg,R.mipmap.img_error);
        holder.tv_name.setText(goodsList.goodsName);
        Spannable spannable = new SpannableString("￥" + String.format("%.2f", NumberFormat.convertToDouble(goodsList.shopPrice, 0d)));
        spannable.setSpan(new TextAppearanceSpan(context, R.style.small_price), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new TextAppearanceSpan(context, R.style.big_price), 1, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_price.setText(spannable, TextView.BufferType.SPANNABLE);
        holder.tv_m_value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_m_value.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(goodsList.marketPrice, 0d)));
        String goodsSn = goodsList.goodsSn;
        holder.tv_art_num.setVisibility(View.GONE);
        holder.tv_art_num.setText("赠送" + Math.floor(Double.parseDouble(arrayList.get(position).giveIntegral)/3)+"积分");
        holder.tv_sold.setText(goodsList.salesSum==null?"已售0":"已售"+goodsList.salesSum+"件");
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!caches.get("access_token").equals("null")) {
                    umurl = Urls.UMSTR + arrayList.get(position).goodsId + "&spreader=" + caches.get("spreadCode");
                    String imgUrl="";
                    if (!TextUtils.isEmpty(goodsList.originalImg)){
                        imgUrl = goodsList.originalFuImg;
                    }
                    if (!TextUtils.isEmpty(goodsList.originalFuImg)){
                        imgUrl = goodsList.originalImg;
                    }
//                    if (TextUtils.isEmpty(imgUrl)){
//                        return;
//                    }
                    UMengShareUtils.uMengShare(context,umurl,goodsList.goodsName,imgUrl);
                } else {
                    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                            .setMessage("您还没有登录，请先登录。")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }
                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            dialog.dismiss();
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
            }
        });
        holder.img_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetUrlListener.getData(arrayList.get(position).goodsId);
            }
        });
        holder.img_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsListEntity.GoodsListBean goodsListBean = arrayList.get(position);
                onGetUrlListener.getUrl(goodsListBean.goodsId);
            }
        });
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
        ImageView img_goods, img_shopping_cart, img_share, img_show;
        TextView tv_name, tv_price, tv_m_value, tv_art_num,tv_sold;
        public MyViewHolder(View view) {
            super(view);
            img_goods = (ImageView) view.findViewById(R.id.img_goods);
            img_shopping_cart = (ImageView) view.findViewById(R.id.img_shopping_cart);
            img_share = (ImageView) view.findViewById(R.id.img_share);
            img_show = (ImageView) view.findViewById(R.id.img_show);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_m_value = (TextView) view.findViewById(R.id.tv_m_value);
            tv_art_num = (TextView) view.findViewById(R.id.tv_art_num);
            tv_sold = (TextView) view.findViewById(R.id.tv_sold);
        }
    }

    public interface OnItemClickListener{
        void onItemClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}
