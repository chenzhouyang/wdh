package com.yskj.welcomeorchard.ui.localserver;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.LocalServerOrderAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerOrderBean;
import com.yskj.welcomeorchard.ui.advertising.ChooseDishActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.yskj.welcomeorchard.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;


/**
 * 本地生活订单页面
 */
public class LocalServerOrderActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.rb_all)
    RadioButton rb_all;
    /*@Bind(R.id.rb_wait_pay)
    RadioButton rb_wait_pay;*/
    @Bind(R.id.rb_wait_use)
    RadioButton rb_wait_use;
    @Bind(R.id.rb_over_due)
    RadioButton rb_over_due;
    @Bind(R.id.rb_after_sell)
    RadioButton rb_after_sell;
    @Bind(R.id.group)
    RadioGroup group;
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    private String status = "";
    private int offset = 0;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ArrayList<LocalServerOrderBean.DataBean.OrderVosBean> lifeOrders = new ArrayList<>();
    private LocalServerOrderAdapter adapter;
    private LocalServerOrderBean localServerOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_server_order);
        ButterKnife.bind(this);
        initView();
        initScroll();
        initListener();
    }


    private void initView() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().killActivity(OrderPayActivity.class);
                AppManager.getInstance().killActivity(LocalServerDetailActivity.class);
                AppManager.getInstance().killActivity(WXPayEntryActivity.class);
                AppManager.getInstance().killActivity(ChooseDishActivity.class);
                AppManager.getInstance().killActivity(LocalServerOrderActivity.class);
            }
        });
        txtTitle.setText("我的订单");
        adapter = new LocalServerOrderAdapter(context, lifeOrders);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        //status	查询的状态	状态，0：待付款；1：已付款待消费；2：已消费；3：申请退款，待审批；4：已退款 不填默认全部
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                offset = 0;
                lifeOrders.clear();
                switch (checkedId) {
                    case R.id.rb_all:
                        status = "";
                        getData();
                        break;
                   /* case R.id.rb_wait_pay:
                        status = "0";
                        getData();
                        break;*/
                    case R.id.rb_wait_use:
                        status = "0";
                        getData();
                        break;
                    case R.id.rb_over_due:
                        status = "1";
                        getData();
                        break;
                    case R.id.rb_after_sell:
                        status = "2";
                        getData();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeOrders.clear();
        getData();
    }

    private void getData() {
        OkHttpUtils.get().url(Urls.LOCALSERVERORDER).addHeader("Authorization", caches.get("access_token"))
                .addParams("cursor", (offset * 10) + "")
                .addParams("count","100")
                .addParams("status", status)
                .build().execute(new StringCallback() {
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
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                localServerOrderBean = new Gson().fromJson(response, LocalServerOrderBean.class);
                if(localServerOrderBean.code == 0){
                    lifeOrders.addAll(localServerOrderBean.data.orderVos);
                    scrollView.onRefreshComplete();
                    if (LocalServerOrderActivity.this.lifeOrders == null || LocalServerOrderActivity.this.lifeOrders.size() == 0) {
                        showToast("暂无数据");
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    showToast(MessgeUtil.geterr_code(localServerOrderBean.code));
                }

            }
        });

    }

    private void initScroll() {
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset = 0;
                lifeOrders.clear();
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset++;
                getData();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AppManager.getInstance().killActivity(OrderPayActivity.class);
            AppManager.getInstance().killActivity(LocalServerDetailActivity.class);
            AppManager.getInstance().killActivity(WXPayEntryActivity.class);
            AppManager.getInstance().killActivity(ChooseDishActivity.class);
            AppManager.getInstance().killActivity(LocalServerOrderActivity.class);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
