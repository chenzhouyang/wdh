package com.yskj.welcomeorchard.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SearchGoodsListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/17.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.ed_keyword)
    EditText edKeyword;
    @Bind(R.id.listView)
    PullToRefreshListView listView;
    @Bind(R.id.tv_right_text)
    TextView tvRightText;
    @Bind(R.id.iv_return)
    ImageView ivReturn;

    private String type;
    private int page;

    private ArrayList<GoodsListEntity.GoodsListBean> goodsListALL = new ArrayList<>();//全部数据
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListPage = new ArrayList<>();//请求的每一页的数据

    private SearchGoodsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        initListView();
    }

    private void initListView() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                swithKeyWord();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                swithKeyWord();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, CommodityDetailsActiviy.class);
                GoodsListEntity.GoodsListBean goodsListBean = goodsListALL.get(position - 1);
                intent.putExtra("goodid", goodsListBean.goodsId);
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 4) {
                    ivReturn.setVisibility(View.VISIBLE);
                } else {
                    ivReturn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void swithKeyWord() {
        switch (type) {
            case "goodsCategory":
                page = 1;
                goodsListALL.clear();
                getGoodsList();
                break;
        }
    }

    private void initView() {
        type = getIntent().getStringExtra("from");
        switch (type) {
            case "goodsCategory":
                edKeyword.setHint("请输入商品名称");
                initGoodsAdapter();
                break;
        }
        imgBack.setOnClickListener(this);
        tvRightText.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
    }

    private void initGoodsAdapter() {
        adapter = new SearchGoodsListAdapter(context, goodsListALL);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, CommodityDetailsActiviy.class);
                GoodsListEntity.GoodsListBean goodsListBean = goodsListALL.get(position);
                intent.putExtra("goodid", goodsListBean.goodsId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right_text:
                getGoodsList();
                break;
            case R.id.iv_return:
                listView.getRefreshableView().setSelection(0);
                break;
        }
    }

    private void getGoodsList() {
        if (edKeyword.getText().toString() == null || edKeyword.getText().toString().equals("")) {
            showToast("请输入商品名称");
            return;
        }
        OkHttpUtils.get().url(Urls.SEARCHGOODSLIST + "p/" + page + "?q=" + edKeyword.getText().toString()).build().execute(new GoodsListCallBack());
    }

    //网络请求
    private class GoodsListCallBack extends Callback<GoodsListEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public GoodsListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            GoodsListEntity registerEntity = new Gson().fromJson(string, GoodsListEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            if(null!=listView){
                listView.onRefreshComplete();
            }

            showToast("请检查网络");
        }

        @Override
        public void onResponse(GoodsListEntity response, int id) {
            stopMyDialog();
            listView.onRefreshComplete();
            if (page == 1) {
                goodsListALL.clear();
            }
            //每次清空上页数据
            goodsListPage.clear();
            if (response.error_code == 000) {
                goodsListPage = response.goodsList;
                goodsListALL.addAll(goodsListPage);
                if (goodsListALL == null || goodsListALL.size() == 0) {
                    showToast("暂没有符合条件的商品");
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
            if (response.error_code == 001) {
                showToast("暂没有符合条件的商品");
            }
        }
    }
}
