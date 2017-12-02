package com.yskj.welcomeorchard.ui.supply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SouceListClassifyAdapter;
import com.yskj.welcomeorchard.adapter.SourceListGoodsAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.SouceListClassEntity;
import com.yskj.welcomeorchard.entity.SupplyEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.store.SearchActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
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
 * 2017/8/3.
 * 描述：微商货源商品列表activity
 */

public class SourcelistActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.ed_keyword)
    TextView edKeyword;
    @Bind(R.id.soucelist_classify)
    RecyclerView soucelistClassify;
    @Bind(R.id.soucelist_goods)
    XRecyclerView soucelistGoods;
    @Bind(R.id.iv_shopping_car)
    ImageView iv_shopping_car;
    @Bind(R.id.iv_shopping_order)
    ImageView iv_shopping_order;
    private List<SupplyEntity.DataBean.GoodsListBean> listall = new ArrayList<>();
    private List<SupplyEntity.DataBean.GoodsListBean> listbean = new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();
    private GridLayoutManager linearLayoutManager;
    private SouceListClassifyAdapter classifyAdapter;
    private SourceListGoodsAdapter goodsAdapter;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int type = 0;
    private int cursor = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soucelist);
        ButterKnife.bind(this);
        initview();
        //分类数据
        getKeyWord();
        //商品数据
        getGoodsList();
        initListen();

    }

    private void initListen() {
        soucelistGoods.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        soucelistGoods.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                cursor = 0;
                getGoodsList();
                soucelistGoods.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                cursor++;
                getGoodsList();
                soucelistGoods.refreshComplete();
            }
        });
    }

    private void initview() {
        imgBack.setOnClickListener(this);
        iv_shopping_order.setOnClickListener(this);
        iv_shopping_car.setOnClickListener(this);
        edKeyword.setOnClickListener(this);
        //商品显示数据
        goodsAdapter = new SourceListGoodsAdapter(context, listall);
        linearLayoutManager = new GridLayoutManager(context, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        soucelistGoods.setLayoutManager(linearLayoutManager);
        soucelistGoods.setHasFixedSize(true);
        soucelistGoods.setPullRefreshEnabled(false);
        soucelistGoods.addItemDecoration(new DividerGridItemDecoration(context, 9, R.drawable.divider));
        soucelistGoods.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemClickListener(new SourceListGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(context,SourceParticularsActivity.class)
                        .putExtra("goodsid",listall.get(position).goodId+"")
                        .putExtra("imageurl",listall.get(position).cover)
                        .putExtra("shopname",listall.get(position).shopname)
                );
            }
        });
    }

    //网络请求数据
    private void getKeyWord() {
        //分类的请求数据
        OkHttpUtils.get().url(Urls.KEYWOEDLIST).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                SouceListClassEntity classEntity = new Gson().fromJson(response, new TypeToken<SouceListClassEntity>() {
                }.getType());
                arrayList.addAll(classEntity.data);
                //分类数据
                if (arrayList.size() < 4) {
                    type = 1;
                } else if (arrayList.size() < 8) {
                    type = 2;
                }
                classifyAdapter = new SouceListClassifyAdapter(context, arrayList, type);
                linearLayoutManager = new GridLayoutManager(context, 4);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                soucelistClassify.setLayoutManager(linearLayoutManager);
                soucelistClassify.setHasFixedSize(true);
                soucelistClassify.addItemDecoration(new DividerGridItemDecoration(context, 15, R.drawable.divider));
                soucelistClassify.setAdapter(classifyAdapter);
                classifyAdapter.setOnItemClickListener(new SouceListClassifyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        startActivity(new Intent(context, SouceSearchActivity.class).putExtra("type","1")
                        .putExtra("keyword",arrayList.get(position)));
                    }
                });
            }
        });
    }

    //商品的数据请求
    private void getGoodsList() {
        OkHttpUtils.get().url(Urls.KEYWOEDGOODSLIST)
                .addParams("cursor", (cursor*10) + "")
                .addParams("count", "10")
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
                SupplyEntity goodsBean = new Gson().fromJson(response, new TypeToken<SupplyEntity>() {
                }.getType());
                if(goodsBean.code == 0){
                    listbean.clear();
                    listbean.addAll(goodsBean.data.goodsList);
                    if(listbean.size()!=0){
                        if(cursor == 0){
                            listall.clear();
                        }
                        listall.addAll(listbean);
                        goodsAdapter.notifyDataSetChanged();
                    }else {
                        showToast("暂无数据");
                    }
                }else {
                    showToast(MessgeUtil.geterr_code(goodsBean.code));
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_shopping_car:
                startActivity(new Intent(context,ShoppingCartActivity.class));
                break;
            case R.id.iv_shopping_order:
                if(caches.get("access_token").equals("null")){
                    startActivity(new Intent(context, LoginActivity.class));
                }
                startActivity(new Intent(context,SurceOrderActivity.class).putExtra("orderstaut","1"));
                break;
            case R.id.img_back:
                AppManager.getInstance().killActivity(SourcelistActivity.class);
                break;
            case R.id.ed_keyword:
                startActivity(new Intent(context, SouceSearchActivity.class).putExtra("type","0"));
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }
}
