package com.yskj.welcomeorchard.ui.supply;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SourceListGoodsAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.SupplyEntity;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 作者： chen
 * 时间： 2017/8/31
 * 描述：搜索界面
 */

public class SouceSearchActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.ed_keyword)
    EditText edKeyword;
    @Bind(R.id.tv_right_text)
    TextView tvRightText;
    @Bind(R.id.search_listview)
    XRecyclerView soucelistGoods;
    private SourceListGoodsAdapter goodsAdapter;
    private GridLayoutManager linearLayoutManager;
    private List<SupplyEntity.DataBean.GoodsListBean> listall = new ArrayList<>();
    private int cursor;
    private List<SupplyEntity.DataBean.GoodsListBean>  listbean = new ArrayList<>();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_search);
        ButterKnife.bind(this);
        iniview();
    }

    private void iniview() {
        imgBack.setOnClickListener(this);
        tvRightText.setOnClickListener(this);
        goodsAdapter = new SourceListGoodsAdapter(context, listall);
        linearLayoutManager = new GridLayoutManager(context, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        soucelistGoods.setLayoutManager(linearLayoutManager);
        soucelistGoods.setHasFixedSize(true);
        soucelistGoods.setPullRefreshEnabled(false);
        soucelistGoods.addItemDecoration(new DividerGridItemDecoration(context, 15, R.drawable.divider));
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
            }
        });
        type = getIntent().getStringExtra("type");
        if(type.equals("1")){
            String keyword = getIntent().getStringExtra("keyword");
            edKeyword.setText(keyword);
            getGoodsList();
        }
    }

    //商品的数据请求
    private void getGoodsList() {
        OkHttpUtils.get().url(Urls.KEYWOEDGOODSLIST)
                .addParams("cursor", (cursor*10) + "")
                .addParams("count", "10")
                .addParams("keyword",edKeyword.getText().toString())
                .build().execute(new StringCallback() {
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
            case R.id.tv_right_text:
                if(edKeyword.getText().length()>0){
                    getGoodsList();
                }else {
                    showToast("请输入要搜索的内容");
                }
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }
}
