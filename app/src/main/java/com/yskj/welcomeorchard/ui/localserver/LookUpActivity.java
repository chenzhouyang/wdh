package com.yskj.welcomeorchard.ui.localserver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.unionpay.tsmservice.request.RequestParams;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.LocalServerMainListAdapter;
import com.yskj.welcomeorchard.adapter.NewLocalGoodsListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
import com.yskj.welcomeorchard.entity.NewLocalGoodsListEntity;
import com.yskj.welcomeorchard.ui.advertising.ChooseDishActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wdx on 2016/11/7 0007.
 */
public class LookUpActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_lookup;
    private ImageView img_sousuo,lookup_close;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private String token;
    private int max = 10;
    private int offset = 0;
    private NewLocalGoodsListAdapter adapter;
    private XRecyclerView listView;
    private ArrayList<NewLocalGoodsListEntity.DataBean.LocalShopsBean> list = new ArrayList<>();
    private ArrayList<NewLocalGoodsListEntity.DataBean.LocalShopsBean> listAll = new ArrayList<>();
    private String name = "";
    private String cityId;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookup_layout);
        initView();
        token = aCache.get("access_token");
        initScrollView();
    }

    private void initView() {
        cityId = getIntent().getStringExtra("cityid");
        lookup_close = (ImageView) findViewById(R.id.lookup_close);
        lookup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed_lookup = (EditText) findViewById(R.id.ed_lookup);
        img_sousuo = (ImageView) findViewById(R.id.img_sousuo);
        img_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_lookup.getText().toString() != null) {
                    name = ed_lookup.getText().toString();
                    offset = 0;
                    initData();
                }
            }

        });
        lookup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (XRecyclerView) findViewById(R.id.lv_mall_goods);
        try {
            adapter = new NewLocalGoodsListAdapter(context, listAll);
            layoutManager = new GridLayoutManager(context, 1);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            listView.setLayoutManager(layoutManager);
            listView.setHasFixedSize(true);
            adapter.setOnItemClickListener(new NewLocalGoodsListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    startActivity(new Intent(context, ChooseDishActivity.class)
                            .putExtra("shopid", listAll.get(position).shopId + "")
                            .putExtra("address", listAll.get(position).address)
                            .putExtra("distance", listAll.get(position).distanceString));
                }
            });
            listView.setAdapter(adapter);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
        }
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.NEWLOCALGOODSLIST)
                .addParams("areaId", cityId)
                .addParams("name", name)
                .addParams("max", max + "")
                .addParams("offset", offset + "")
                .build().execute(new FindCallBack());
    }

    private class FindCallBack extends Callback<NewLocalGoodsListEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
        }

        @Override
        public NewLocalGoodsListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            NewLocalGoodsListEntity localServerMainListBean = new Gson().fromJson(s, new TypeToken<NewLocalGoodsListEntity>() {
            }.getType());
            return localServerMainListBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(NewLocalGoodsListEntity response, int id) {
            if (offset == 0) {
                listAll.clear();
            }
            //每次清空上页数据
            list.clear();
            if (response.code == 0) {
                list=response.data.localShops;
                listAll.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }


    }

    private void initScrollView() {
        listView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        listView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                initData();
            }

            @Override
            public void onLoadMore() {
                offset++;
                initData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
