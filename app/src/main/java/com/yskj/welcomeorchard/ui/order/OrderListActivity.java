package com.yskj.welcomeorchard.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.OrderListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.OrderListEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.home.HomeActivity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.ui.store.ShoppingCartActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class OrderListActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.order_list_lv)
    PullToRefreshListView orderListLv;
    @Bind(R.id.deliver_goods)
    RadioButton deliverGoods;
    @Bind(R.id.send_goods)
    RadioButton sendGoods;
    @Bind(R.id.rb_send)
    RadioButton rbSend;
    @Bind(R.id.rb_got)
    RadioButton rbGot;
    @Bind(R.id.group)
    RadioGroup group;
    private String OrderType = "WAITPAY";
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private UserInfoEntity userInfoEntity;
    private String orderpaytype;
    private  ArrayList<OrderListEntity.OrderListBean> listBeen = new ArrayList<>();
    private OrderListAdapter listAdapter;
    private int p = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        ButterKnife.bind(this);
        txtTitle.setText("订单列表");
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"),new TypeToken<UserInfoEntity>(){}.getType());
        id = userInfoEntity.data.userVo.id;

        iniview();
        finview();
    }

    private void iniview() {
        orderListLv.setMode(PullToRefreshBase.Mode.BOTH);
        orderListLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                getorderlist();
                MethodUtils.stopRefresh(orderListLv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                p++;
                getorderlist();
                MethodUtils.stopRefresh(orderListLv);
            }
        });
        listAdapter = new OrderListAdapter(context, listBeen);
        orderListLv.setAdapter(listAdapter);
        String orderstaut = getIntent().getStringExtra("orderstaut");
        orderpaytype = getIntent().getStringExtra("orderpaytype");
        caches.put("orderpaytype",orderpaytype);
        if(orderstaut.equals("1")){
            OrderType = "WAITPAY";
            group.check(R.id.deliver_goods);
        }else if(orderstaut.equals("2")){
            OrderType = "WAITSEND";
            group.check(R.id.send_goods);
        }else if(orderstaut.equals("3")){
            OrderType = "WAITRECEIVE";
            group.check(R.id.rb_send);
        }else if(orderstaut.equals("4")){
            OrderType = "WAITCCOMMENT";
            group.check(R.id.rb_got);
        }
        getorderlist();
    }

    private void finview() {

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    //待付款
                    case R.id.deliver_goods:
                        OrderType = "WAITPAY";
                        p = 1;
                        listBeen.clear();
                        getorderlist();
                        listAdapter = new OrderListAdapter(context, listBeen);
                        orderListLv.setAdapter(listAdapter);
                        break;
                    //待发货
                    case R.id.send_goods:
                        OrderType = "WAITSEND";
                        p = 1;
                        listBeen.clear();
                        getorderlist();
                        listAdapter = new OrderListAdapter(context, listBeen);
                        orderListLv.setAdapter(listAdapter);
                        break;
                    //待收货
                    case R.id.rb_send:
                        OrderType = "WAITRECEIVE";
                        p = 1;
                        listBeen.clear();
                        getorderlist();
                        listAdapter = new OrderListAdapter(context, listBeen);
                        orderListLv.setAdapter(listAdapter);
                        break;
                    //已收货
                    case R.id.rb_got:
                        OrderType = "WAITCCOMMENT";
                        p = 1;
                        listBeen.clear();
                        getorderlist();
                        listAdapter = new OrderListAdapter(context, listBeen);
                        orderListLv.setAdapter(listAdapter);
                        break;
                }
            }
        });
    }

    private void getorderlist() {
        OkHttpUtils.get().url(Urls.ORDERLIST+"/uid/"+id+"/"+"sk/"+ Ips.SK+"/type/"+OrderType+"/p/"+p)
                .build().execute(new OrderCallBack(context,orderListLv));
    }


    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if(orderpaytype.equals("0")){
                    AppManager.getInstance().killActivity(OrderPayActivity.class);
                    AppManager.getInstance().killActivity(ConfirmOrderActivity.class);
                    AppManager.getInstance().killActivity(ShoppingCartActivity.class);
                    AppManager.getInstance().killActivity(CommodityDetailsActiviy.class);
                   startActivity(new Intent(context, HomeActivity.class));
                }else {
                    finish();
                }

                break;

        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(orderpaytype.equals("0")){
                AppManager.getInstance().killActivity(OrderPayActivity.class);
                AppManager.getInstance().killActivity(ConfirmOrderActivity.class);
                AppManager.getInstance().killActivity(ShoppingCartActivity.class);
                AppManager.getInstance().killActivity(CommodityDetailsActiviy.class);
                startActivity(new Intent(context, HomeActivity.class));
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class OrderCallBack extends Callback<OrderListEntity> {
        private Context context;
        private PullToRefreshListView orderListLv;
        private ArrayList<OrderListEntity> listEntities = new ArrayList<>();
        public OrderCallBack(Context context,PullToRefreshListView orderListLv){
            super();
            this.context = context;
            this.orderListLv = orderListLv;
        }

        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            startMyDialog();
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
        }

        @Override
        public OrderListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            OrderListEntity orderListEntity =  new Gson().fromJson(s,new TypeToken<OrderListEntity>(){}.getType());
            return orderListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse( OrderListEntity response, int id) {
            if(response.error_code==000){
                if (p==1){
                    listBeen.clear();
                }
                listBeen.addAll(response.orderList);
                if(response.orderList!=null&&response.orderList.size()!=0){
                    listAdapter.notifyDataSetChanged();
                }else {
                    showToast("暂无订单");
                }
            }else {
                showToast("暂无订单");
            }



        }
    }

}
