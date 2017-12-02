package com.yskj.welcomeorchard.ui.deposit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.BankListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.BankListEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.SwipeListView;
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
 * Created by YSKJ-02 on 2017/1/15.
 * 银行卡列表
 */

public class BankListActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.bank_list_pulllistview)
    SwipeListView bankListPulllistview;
    @Bind(R.id.add_bank)
    ImageView addBank;
    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banklist);
        ButterKnife.bind(this);
        txtTitle.setText("银行卡");
        scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        //获取银行卡列表
        getbanklist();
    }

    private void getbanklist() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.BANKLIST).
                addHeader("Authorization", caches.get("access_token")).build().execute(new BankListCallBalk(context,bankListPulllistview));
        stopMyDialog();
    }

    @OnClick({R.id.img_back, R.id.add_bank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.add_bank:
                startActivity(new Intent(context, AddBankActivity.class).putExtra("type", "0"));
                break;
        }
    }

    public class BankListCallBalk extends Callback<BankListEntity> {
        private Context context;
        private SwipeListView bankListPulllistview;
        private ArrayList<BankListEntity.DataBean> dataBeen;

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

        public BankListCallBalk(Context context, SwipeListView bankListPulllistview) {
            super();
            this.context = context;
            this.bankListPulllistview = bankListPulllistview;
        }

        @Override
        public BankListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            BankListEntity bankListEntity = new Gson().fromJson(s, new TypeToken<BankListEntity>() {
            }.getType());
            return bankListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(BankListEntity response, int id) {
            if (response.code == 0) {
                dataBeen = response.data;
                if (dataBeen.size() == 0) {
                    bankListPulllistview.setVisibility(View.GONE);
                } else {
                    bankListPulllistview.setVisibility(View.VISIBLE);
                    BankListAdapter adapter = new BankListAdapter(context, dataBeen,bankListPulllistview.getRightViewWidth());
                    bankListPulllistview.setAdapter(adapter);
                    adapter.setOnRightItemClickListener(new BankListAdapter.onRightItemClickListener() {
                        @Override
                        public void onRightItemClick(View v, int position) {
                            bankListPulllistview.hiddenRight(bankListPulllistview.getChildAt(position));

                        }
                    });
                }
            } else {
                Toast.makeText(context, MessgeUtil.geterr_code(response.code), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
