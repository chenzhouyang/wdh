package com.yskj.welcomeorchard.ui.supply;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SupplyOrderListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.SupplyOrderEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 陈宙洋
 * 2017/8/4.
 * 描述：订单列表activity
 */

public class SurceOrderActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
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
    @Bind(R.id.order_list_lv)
    PullToRefreshListView orderListLv;
    private SupplyOrderListAdapter orderListAdapter;
    private SupplyOrderEntity orderEntity;
    private String status = "1";
    private List<SupplyOrderEntity.DataBean.ListBean> listBeenAll = new ArrayList<>();
    public static final int SHUAXIN = 1;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHUAXIN:
                    listBeenAll.clear();
                    getDate();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surce_order);
        ButterKnife.bind(this);
        txtTitle.setText("订单列表");
        finview();
        getDate();
    }
    private void finview() {
        orderListAdapter = new SupplyOrderListAdapter(context,listBeenAll,handler);
        orderListLv.setAdapter(orderListAdapter);
        imgBack.setOnClickListener(this);
        status = getIntent().getStringExtra("orderstaut");
        if(status.equals("1")){
            group.check(R.id.deliver_goods);
        }else if(status.equals("2")){
            group.check(R.id.send_goods);
        }else if(status.equals("3")){
            group.check(R.id.rb_send);
        }else if(status.equals("4")){
            group.check(R.id.rb_got);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                listBeenAll.clear();
                switch (checkedId){
                    //待付款
                    case R.id.deliver_goods:
                        status = "1";
                        getDate();
                        break;
                    //待发货
                    case R.id.send_goods:
                        status = "2";
                        getDate();
                        break;
                    //待收货
                    case R.id.rb_send:
                        status = "3";
                        getDate();
                        break;
                    //已收货
                    case R.id.rb_got:
                        status = "4";
                        getDate();
                        break;
                }
            }
        });
    }
    private void getDate(){
        OkHttpUtils.get().url(Urls.MGOESWE)
                .addHeader("Authorization",caches.get("access_token"))
                .addParams("status",status).build().execute(new StringCallback() {
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
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                orderEntity = new Gson().fromJson(response,new TypeToken<SupplyOrderEntity>(){}.getType());
                if(orderEntity.code == 0){
                    if(orderEntity.data.list!=null&&orderEntity.data.list.size()!=0){
                        listBeenAll.addAll(orderEntity.data.list);
                    }else {
                        showToast("暂无订单信息");
                    }
                    orderListAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                AppManager.getInstance().killActivity(SurceOrderActivity.class);
                AppManager.getInstance().killActivity(OrderPayActivity.class);
                AppManager.getInstance().killActivity(ShoppingCartActivity.class);
                AppManager.getInstance().killActivity(SourceParticularsActivity.class);
                AppManager.getInstance().killActivity(SupplyConfirmActivity.class);
                AppManager.getInstance().killActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AppManager.getInstance().killActivity(SurceOrderActivity.class);
        AppManager.getInstance().killActivity(OrderPayActivity.class);
        AppManager.getInstance().killActivity(SupplyConfirmActivity.class);
        AppManager.getInstance().killActivity(ShoppingCartActivity.class);
        AppManager.getInstance().killActivity(SourceParticularsActivity.class);
        AppManager.getInstance().killActivity(LoginActivity.class);
        return super.onKeyDown(keyCode, event);

    }
}
