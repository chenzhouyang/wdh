package com.yskj.welcomeorchard.ui.deposit;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.BankDepositListAdapter;
import com.yskj.welcomeorchard.adapter.RecordAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.BankListEntity;
import com.yskj.welcomeorchard.entity.RecordEntiity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.NoScrollListView;
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
 * Created by YSKJ-02 on 2017/1/13.
 * 提现界面
 */

public class DepositActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.deposit_bank_lv)
    NoScrollListView depositBankLv;
    @Bind(R.id.deposit_lv)
    NoScrollListView depositLv;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.bank_list_ll)
    LinearLayout bankListLl;
    @Bind(R.id.deposit_pull_scroll)
    PullToRefreshScrollView depositPullScroll;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int count = 10, cursor = 0;
    private ArrayList<RecordEntiity.DataBean.ListBean> dataBeenAll = new ArrayList<>();
    private ArrayList<RecordEntiity.DataBean.ListBean> dataPage = new ArrayList<>();
    private BankDepositListAdapter adapter;
    private RecordAdapter recordAdapte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);
        txtTitle.setText("提现");
        //获取银行卡列表
        getrecordlist();
        //提现列表、
        getbanlist();
        iniscrollview();
    }

    //刷新提现列表
    private void iniscrollview() {
        recordAdapte = new RecordAdapter(context,dataBeenAll);
        depositLv.setAdapter(recordAdapte);
        depositPullScroll.setMode(PullToRefreshBase.Mode.BOTH);
        depositPullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor = 0;
                getbanlist();
                depositPullScroll.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor++;
                getbanlist();
                depositPullScroll.onRefreshComplete();
            }
        });
    }

    private void getbanlist() {
        OkHttpUtils.get().url(Urls.RECORDLIST)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("count", count + "").addParams("cursor", cursor * 10 + "").build().
                execute(new RecordCallBack(context, depositLv));
    }
//银行卡列表
    private void getrecordlist() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.BANKLIST).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new BankDepositListCallBalk());
        stopMyDialog();
    }

    public class RecordCallBack extends Callback<RecordEntiity> {
        private Context context;
        private NoScrollListView depositLv;
        private ArrayList<RecordEntiity.DataBean.ListBean> listBeen;

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

        public RecordCallBack(Context context, NoScrollListView depositLv){
            super();
            this.context = context;
            this.depositLv = depositLv;
        }
        @Override
        public RecordEntiity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            RecordEntiity recordEntiity = new Gson().fromJson(s,new TypeToken<RecordEntiity>(){}.getType());
            return recordEntiity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);

        }

        @Override
        public void onResponse(RecordEntiity response, int id) {
            if(response.code == 0){
                //每次清空上页数据
                dataPage.clear();
                dataPage = response.data.list;
                if(dataPage.size() != 0){
                    if (cursor==0){
                        dataBeenAll.clear();
                    }

                    dataBeenAll.addAll(dataPage);
                    recordAdapte.notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "没有数据",Toast.LENGTH_SHORT).show();
                }
            }else {
                String message = MessgeUtil.geterr_code(response.code);
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }

        }
    }
    @OnClick({R.id.img_back, R.id.bank_list_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    public class BankDepositListCallBalk extends Callback<BankListEntity>  {

        @Override
        public BankListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            BankListEntity bankListEntity = new Gson().fromJson(s,new TypeToken<BankListEntity>(){}.getType());
            return bankListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(BankListEntity response, int id) {
            if(response.code == 0){
                adapter = new BankDepositListAdapter(context,response.data);
                depositBankLv.setAdapter(adapter);
            }else {
                Toast.makeText(context, MessgeUtil.geterr_code(response.code),Toast.LENGTH_SHORT).show();
            }
        }
    }


}
