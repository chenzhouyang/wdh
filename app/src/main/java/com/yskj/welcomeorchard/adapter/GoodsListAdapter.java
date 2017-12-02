package com.yskj.welcomeorchard.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.fragment.FragmentInterface;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.NumberFormat;
import com.yskj.welcomeorchard.widget.SimplexToast;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/15.
 */

public class GoodsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GoodsListEntity.GoodsListBean> arrayList;
    private FragmentInterface.OnGetUrlListener onGetUrlListener;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String promotioncode, umurl;

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                SimplexToast.show(context, platform + " 收藏成功啦");
            } else {
                SimplexToast.show(context, platform + " 分享成功啦");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SimplexToast.show(context, platform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SimplexToast.show(context, platform + " 分享取消了");
        }
    };

    public GoodsListAdapter(Context context, ArrayList<GoodsListEntity.GoodsListBean> arrayList, FragmentInterface.OnGetUrlListener onGetUrlListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onGetUrlListener = onGetUrlListener;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fragment_all, null);
            holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);
            holder.img_shopping_cart = (ImageView) convertView.findViewById(R.id.img_shopping_cart);
            holder.img_share = (ImageView) convertView.findViewById(R.id.img_share);
            holder.img_show = (ImageView) convertView.findViewById(R.id.img_show);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_m_value = (TextView) convertView.findViewById(R.id.tv_m_value);
            holder.tv_art_num = (TextView) convertView.findViewById(R.id.tv_art_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsListEntity.GoodsListBean goodsList = arrayList.get(position);
        GlideImage.loadImage(context,holder.img_goods,goodsList.originalImg,R.mipmap.img_error);
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

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!caches.get("access_token").equals("null")) {
                    umurl = Urls.UMSTR + arrayList.get(position).goodsId + "&spreader=" + caches.get("spreadCode");
                    new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .withTitle("唯多惠精品")
                            .withText(goodsList.goodsName+"\n"+context.getResources().getString(R.string.share_content))
                            .withMedia(new UMImage(context, Ips.PHPURL + "/" + goodsList.originalImg.toString()))
                            .withTargetUrl(umurl)
                            .setCallback(umShareListener)
                            .open();
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
        return convertView;
    }

    private class ViewHolder {
        ImageView img_goods, img_shopping_cart, img_share, img_show;
        TextView tv_name, tv_price, tv_m_value, tv_art_num;
    }
}
