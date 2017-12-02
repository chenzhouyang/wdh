package com.yskj.welcomeorchard.ui.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.OrderGoodsApapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.OrderListEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.DateUtils;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class OrderDetailActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.shr_name)
    TextView shrName;
    @Bind(R.id.order_dz)
    TextView orderDz;
    @Bind(R.id.goods_list_details)
    NoScrollListView goodsListDetails;
    @Bind(R.id.order_yundou)
    TextView orderYundou;
    @Bind(R.id.order_zje)
    TextView orderZje;
    @Bind(R.id.order_yf)
    TextView orderYf;
    @Bind(R.id.order_zongjixiaofei)
    TextView orderZongjixiaofei;
    @Bind(R.id.txt_logistics)
    TextView txtLogistics;
    @Bind(R.id.txt_wuliuhao)
    TextView txtWuliuhao;
    @Bind(R.id.layout_logistics)
    LinearLayout layoutLogistics;
    @Bind(R.id.order_sfje)
    TextView orderSfje;
    @Bind(R.id.order_sj)
    TextView orderSj;
    @Bind(R.id.order_receive_goods)
    TextView orderReceiveGoods;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.order_receive_abolish)
    TextView orderReceiveAbolish;
    @Bind(R.id.adress_ll)
    LinearLayout adressLl;
    private OrderListEntity.OrderListBean orderListBeen;
    private int ordertype;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private UserInfoEntity userInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetial);
        ButterKnife.bind(this);
        txtTitle.setText("订单详情");
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        id = userInfoEntity.data.userVo.id;
        getorderlist();
    }

    private void getorderlist() {
        orderListBeen = (OrderListEntity.OrderListBean) getIntent().getSerializableExtra("orderbean");
        if(orderListBeen.consignee!=null&&orderListBeen.consignee.length()!=0){
            adressLl.setVisibility(View.VISIBLE);
            shrName.setText(orderListBeen.consignee + "");
            orderDz.setText(orderListBeen.fullAddress + "");
        }else {
            adressLl.setVisibility(View.GONE);
        }


        orderYundou.setText("￥" + orderListBeen.userMoney + "");
        OrderGoodsApapter apapter = new OrderGoodsApapter(context, orderListBeen.goodsList);
        goodsListDetails.setAdapter(apapter);
        orderZje.setText("￥" + orderListBeen.goodsPrice + "");
        orderYf.setText("￥" + orderListBeen.shippingPrice + "");
        orderZongjixiaofei.setText("￥" + orderListBeen.orderAmount + "");
        if (orderListBeen.shippingBtn == 1) {
            layoutLogistics.setVisibility(View.VISIBLE);
            txtWuliuhao.setText(orderListBeen.invoiceNo + "");
        }
        if (orderListBeen.payBtn == 1) {
            linearLayout.setVisibility(View.VISIBLE);
            orderReceiveGoods.setText("付款");
            orderReceiveAbolish.setText("取消");
            ordertype = 1;
        }
        orderSj.setText(DateUtils.timeStampToStr(Long.parseLong(orderListBeen.addTime)));
        if (orderListBeen.receiveBtn == 1) {
            linearLayout.setVisibility(View.VISIBLE);
            ordertype = 2;
            orderReceiveGoods.setText("确认收货");
            orderReceiveAbolish.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.img_back, R.id.order_receive_goods, R.id.image_right,
            R.id.order_receive_abolish, R.id.layout_logistics})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.order_receive_goods:
                if (ordertype == 1) {
                    Intent intent = new Intent(context, OrderPayActivity.class);
                    intent.putExtra("orderpaytype", caches.get("orderpaytype"));
                    intent.putExtra("orderSn", orderListBeen.orderSn);
                    intent.putExtra("total_amount", orderListBeen.totalAmount);
                    intent.putExtra("user_money", orderListBeen.userMoney);
                    intent.putExtra("order_amount", orderListBeen.orderAmount);
                    intent.putExtra("order_id", orderListBeen.orderId);
                    intent.putExtra("type", "0");
                    intent.putExtra("ordertype", "1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (ordertype == 2) {
                    OrderDetailAddress orderDetailAddress = new OrderDetailAddress(context);
                    orderDetailAddress.show();

                }

                break;
            case R.id.order_receive_abolish:
                startMyDialog();
                OkHttpUtils.get().url(Urls.ABOLISH + "/uid/" + id + "/order_id/" + orderListBeen.orderId).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        stopMyDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Map<String, Object> map = JSONFormat.jsonToMap(response);
                        String code = (String) map.get("error_code");
                        if (code.equals("000")) {
                            showToast("已取消");
                            startActivity(new Intent(context, OrderListActivity.class).putExtra("orderstaut", "1")
                                    .putExtra("orderpaytype", caches.get("orderpaytype")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            showToast((String) map.get("error_msg"));
                        }
                        stopMyDialog();
                    }
                });
                break;
            case R.id.layout_logistics:
                Intent intent1 = new Intent(context, LogisticsActivity.class);
                intent1.putExtra("number", orderListBeen.invoiceNo);
                intent1.putExtra("shipping_code", orderListBeen.shippingCode);
                intent1.putExtra("shipping_name", orderListBeen.shippingName);
                startActivity(intent1);
                break;
        }
    }

    public class OrderDetailAddress extends Dialog implements View.OnClickListener {
        private Button btnCancle;
        private Button btnSure;
        private Context context;
        private TextView message;


        public OrderDetailAddress(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_give_up);

            initView();
        }

        private void initView() {
            btnCancle = (Button) findViewById(R.id.btn_cancle);
            btnSure = (Button) findViewById(R.id.btn_sure);
            message = (TextView) findViewById(R.id.message);
            message.setText("确认收货？");
            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    dismiss();
                    setorderaddress();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }
    }


    //确认收货
    private void setorderaddress() {
        String url = Urls.ORDERTRUE + "/uid/" + id + "/sk/" + Ips.SK + "/order_id/" + orderListBeen.orderId;
        ;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = (String) map.get("error_code");
                if (code.equals("000")) {
                    showToast("收货成功");
                    startActivity(new Intent(context, OrderListActivity.class).putExtra("orderstaut", "3").putExtra("orderpaytype", "1"));
                } else {
                    showToast((String) map.get("error_msg"));
                }
            }
        });
    }
}
