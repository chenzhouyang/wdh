package com.yskj.welcomeorchard.ui.commoditydetails;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.OneBuyGotRecordAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.OneBuyGotRecordEntity;
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
 * 创建日期 2017/4/27on 10:20.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OneBuyGotRecordActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.listView)
    PullToRefreshListView listView;
    @Bind(R.id.group)
    RadioGroup group;

    private OneBuyGotRecordAdapter adapter;
    private ArrayList<OneBuyGotRecordEntity.DataBean.ListBean> arrayList = new ArrayList<>();

    private String activityId;
    private String type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_buy_got_record);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        //type	查询的状态	状态，0=排名奖励 1=1元购物
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_got_other:
                        type = "0";
                        initData();
                        break;
                    case R.id.rb_got_one_buy:
                        type = "1";
                        initData();
                        break;
                }
            }
        });
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.ONEBUYGOT)
                .addParams("activityId", activityId).addParams("type",type)
                .build().execute(new OneBuyGotRecordCallBack());
    }

    private void initView() {
        txtTitle.setText("中奖名单");
        activityId = getIntent().getStringExtra("activityId");
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }

    private class OneBuyGotRecordCallBack extends Callback<OneBuyGotRecordEntity> {
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
        public OneBuyGotRecordEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            OneBuyGotRecordEntity registerEntity = new Gson().fromJson(string, OneBuyGotRecordEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(OneBuyGotRecordEntity response, int id) {
            if (response.code == 0) {
                arrayList.clear();
                adapter = new OneBuyGotRecordAdapter(context, arrayList,type);
                listView.setAdapter(adapter);
                arrayList.addAll(response.data.list);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
