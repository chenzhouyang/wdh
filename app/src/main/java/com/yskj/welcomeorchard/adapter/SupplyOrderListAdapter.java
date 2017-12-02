package com.yskj.welcomeorchard.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.BountyDialog;
import com.yskj.welcomeorchard.dialog.DistributionDialog;
import com.yskj.welcomeorchard.entity.SupplyOrderEntity;
import com.yskj.welcomeorchard.ui.order.LogisticsActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.ui.supply.BountyActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;

import static com.yskj.welcomeorchard.ui.address.AddressActivity.SHUAXIN;

/**
 * 作者：陈宙洋
 * 日期：2017/8/14.
 * 描述：订单列表adapter
 */

public class SupplyOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<SupplyOrderEntity.DataBean.ListBean> listBeanList;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private Handler handler;
    private String url;
    private boolean isBouns = false,isDiart = false;

    public SupplyOrderListAdapter(Context context, List<SupplyOrderEntity.DataBean.ListBean> listBeanList,  Handler handler) {
        this.context = context;
        this.listBeanList = listBeanList;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return listBeanList==null?0:listBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return listBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_surce_orderlist, null);
            mHolder = new ViewHoder();
            mHolder.surce_order_name = (TextView) convertView.findViewById(R.id.surce_order_name);
            mHolder.surce_order_status = (TextView) convertView.findViewById(R.id.surce_order_status);
            mHolder.surce_order_listview = (NoScrollListView) convertView.findViewById(R.id.surce_order_listview);
            mHolder.surce_order_logistics = (TextView) convertView.findViewById(R.id.surce_order_logistics);
            mHolder.surce_order_bounty = (TextView) convertView.findViewById(R.id.surce_order_bounty);
            mHolder.surce_order_market = (TextView) convertView.findViewById(R.id.surce_order_market);
            mHolder.orderlist_status = (LinearLayout) convertView.findViewById(R.id.orderlist_status);
            mHolder.surce_order_condition = (TextView) convertView.findViewById(R.id.surce_order_condition);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHoder) convertView.getTag();
        }
        final SupplyOrderEntity.DataBean.ListBean orderBean = listBeanList.get(position);
        mHolder.surce_order_name.setText(orderBean.millName);
        mHolder.surce_order_condition.setText("共"+listBeanList.get(position).goodsList.size()+"件商品 合计：¥"+orderBean.totalAmount+"（含运费¥:"+orderBean.shippingPrice+"）");
        //shippingStatus 发货状态 0=未发货 1=已发货 2=确认收货
        //payStatus 支付状态 0=未支付 1=已支付
        if(orderBean.payStatus == 0){
            isBouns = false;
            mHolder.orderlist_status.setVisibility(View.VISIBLE);
            mHolder.surce_order_status.setText("买家未付款");
            mHolder.surce_order_bounty.setText("取消订单");
            url = Urls.CANCEL;
            mHolder.surce_order_market.setText("付款");
            mHolder.surce_order_logistics.setVisibility(View.GONE);
            isDiart = false;
        }else {
            if(orderBean.shippingStatus == 0){
                isBouns = false;
                mHolder.surce_order_status.setText("买家已付款");
                mHolder.orderlist_status.setVisibility(View.GONE);
            }else if(orderBean.shippingStatus == 1){
                isBouns = false;
                if(orderBean.shippingName.contains("无需")){
                    mHolder.surce_order_logistics.setVisibility(View.GONE);
                    mHolder.surce_order_logistics.setVisibility(View.GONE);
                }else {
                    mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                }
                mHolder.orderlist_status.setVisibility(View.VISIBLE);
                mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                mHolder.surce_order_status.setText("卖家已发货");
                mHolder.surce_order_logistics.setText("查看物流");
                mHolder.surce_order_bounty.setVisibility(View.VISIBLE);
                mHolder.surce_order_bounty.setText("确认收货");
                mHolder.surce_order_market.setVisibility(View.GONE);
                url = Urls.CONFIRMGOODS;
            }else if(orderBean.shippingStatus == 2){
                if(orderBean.bonusStatus==1&&orderBean.saleStatus == 1){
                    //查看物流、领取奖励金、扫码销售
                    if(orderBean.shippingName.contains("无需")){
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                    }else {
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    }
                    mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    mHolder.surce_order_status.setText("确认收货");
                    mHolder.surce_order_bounty.setVisibility(View.VISIBLE);
                    mHolder.surce_order_market.setVisibility(View.VISIBLE);
                    mHolder.surce_order_logistics.setText("查看物流");
                    isBouns = true;
                    isDiart = true;
                    mHolder.surce_order_bounty.setText("领取奖励金");
                    url = Urls.GETBONUS;
                    mHolder.surce_order_market.setText("扫码销售");
                }else if(orderBean.bonusStatus!=1&&orderBean.saleStatus == 1){
                    //查看物流、扫码销售
                    if(orderBean.shippingName.contains("无需")){
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                    }else {
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    }
                    mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    mHolder.surce_order_status.setText("确认收货");
                    mHolder.surce_order_logistics.setText("查看物流");
                    mHolder.surce_order_bounty.setText("领取奖励金");
                    isDiart = true;
                    mHolder.surce_order_bounty.setVisibility(View.GONE);
                    mHolder.surce_order_market.setVisibility(View.VISIBLE);
                    mHolder.surce_order_market.setText("扫码销售");
                }else if(orderBean.bonusStatus==1&&orderBean.saleStatus != 1){
                    //查看物流、领取奖励金
                    if(orderBean.shippingName.contains("无需")){
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                    }else {
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    }
                    mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    mHolder.surce_order_status.setText("确认收货");
                    mHolder.surce_order_logistics.setText("查看物流");
                    mHolder.surce_order_bounty.setText("领取奖励金");
                    isDiart = true;
                    isBouns = true;
                    mHolder.surce_order_bounty.setVisibility(View.VISIBLE);
                    mHolder.surce_order_market.setVisibility(View.GONE);
                    mHolder.surce_order_market.setText("扫码销售");
                }else {
                    //查看物流
                    if(orderBean.shippingName.contains("无需")){
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                        mHolder.surce_order_logistics.setVisibility(View.GONE);
                    }else {
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                        mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    }
                    mHolder.surce_order_logistics.setVisibility(View.VISIBLE);
                    mHolder.surce_order_status.setText("已收货");
                    mHolder.surce_order_logistics.setText("查看物流");
                    mHolder.surce_order_bounty.setText("领取奖励金");
                    isDiart = true;
                    mHolder.surce_order_bounty.setVisibility(View.GONE);
                    mHolder.surce_order_market.setVisibility(View.GONE);
                    mHolder.surce_order_market.setText("扫码销售");
                }
            }
        }

        //付款和扫码销售
        mHolder.surce_order_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //扫码
                if(isDiart){
                    DistributionDialog distributionDialog = new DistributionDialog(context,orderBean.id+"");
                    distributionDialog.show();
                }else {
                    //付款
                    context.startActivity(new Intent(context, OrderPayActivity.class).putExtra("type","3")
                            .putExtra("allAmount", orderBean.totalAmount+"")
                            .putExtra("tid",orderBean.id+""));
                }

            }
        });
        //取消订单和确认收货
        mHolder.surce_order_bounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bonusStatus 领取奖励金状态 0=不可领取 1=未领取 2=已领取
                if(isBouns){
                    getBounty(Urls.BOUNTS,orderBean.id+"");
                }else {
                    abolishOrder(url,orderBean.id+"");
                }

            }
        });
//        查看物流
        mHolder.surce_order_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent1 = new Intent(context, LogisticsActivity.class);
                    intent1.putExtra("number", orderBean.shippingCode);
                    intent1.putExtra("shipping_code", orderBean.shippingType);
                    intent1.putExtra("shipping_name", orderBean.shippingName);
                    context.startActivity(intent1);

            }
        });

        SupplyOrderAdapter orderAdapter = new SupplyOrderAdapter(context,orderBean.goodsList);
        mHolder.surce_order_listview.setAdapter(orderAdapter);
        return convertView;
    }
    class ViewHoder{
        TextView surce_order_name,surce_order_status,surce_order_logistics,surce_order_bounty,surce_order_market,surce_order_condition;
        NoScrollListView surce_order_listview;
        LinearLayout orderlist_status;
    }

    /**
     * 订单操作
     */
    private void abolishOrder(String url,String orderid){
        OkHttpUtils.post().url(url)
                .addHeader("Authorization",caches.get("access_token"))
                .addParams("orderId",orderid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                Map<String,Object> map = JSONFormat.jsonToMap(s);
                if(map!=null){
                    int code = (int) map.get("code");
                    if(code == 0){
                        //取消成功刷新数据
                        Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                        Message message =new Message();
                        message.what=SHUAXIN;
                        handler.handleMessage(message);
                    }
                }
            }
        });
    }

    /**
     * 订单操作
     */
    private void getBounty(String url,String orderid){
        OkHttpUtils.get().url(url)
                .addHeader("Authorization",caches.get("access_token"))
                .addParams("orderId",orderid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                Map<String,Object> map = JSONFormat.jsonToMap(s);
                if(map!=null){
                    int code = (int) map.get("code");
                    if(code == 0){
                        JSONObject object = (JSONObject) map.get("data");
                        try {
                            String bounsAmount = object.getString("bonusAmount");
                                BountyDialog dialog = new BountyDialog(context,handler,bounsAmount);
                                dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }
}
