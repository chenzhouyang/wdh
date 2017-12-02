package com.yskj.welcomeorchard.ui.buyersShow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.BuyerShowListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.BuyersShowEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/22.
 */

public class BuyersShowListActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.listView)
    PullToRefreshListView listView;
    @Bind(R.id.tv_select)
    TextView tvSelect;
    @Bind(R.id.tv_my_material)
    TextView tvMyMaterial;
    private String goodsId;
    private int page = 1;
    private BuyerShowListAdapter adapter;
    private ArrayList<BuyersShowEntity.CommentlistBean> listALL = new ArrayList<>();//全部数据
    private ArrayList<BuyersShowEntity.CommentlistBean> listPage = new ArrayList<>();//请求的每一页的数据
    private LoadingCaches caches = LoadingCaches.getInstance();

    private String type = "tvSelect";
    private UserInfoEntity userInfoEntity;
    private int uid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyers_sholw_list);
        ButterKnife.bind(this);
        initView();
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equals("tvSelect")) {
            getList();
        } else {
            getMyList();
        }
    }

    //精选
    private void getList() {
        //goodsId 正式用   测试为140
        OkHttpUtils.get().url(Urls.BUYSHOWLIST + "/gid/"  + goodsId + "/p/" + page+"/commentType/5").build().execute(new buyShowListCallBack());
    }

    //我的素材
    private void getMyList() {
        OkHttpUtils.get().url(Urls.BUYSHOWLIST + "/uid/" + uid +"/gid/"  + goodsId + "/p/" + page+"/commentType/5").build().execute(new buyShowListCallBack());
    }

    private class buyShowListCallBack extends Callback<BuyersShowEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public BuyersShowEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            BuyersShowEntity buyersShowEntity = new Gson().fromJson(s, new TypeToken<BuyersShowEntity>() {
            }.getType());
            return buyersShowEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            listView.onRefreshComplete();
            showToast("请检查网络");
        }

        @Override
        public void onResponse(BuyersShowEntity response, int id) {
            stopMyDialog();
            listView.onRefreshComplete();
            if (page == 1) {
                listALL.clear();
            }
            //每次清空上页数据
            listPage.clear();
            if (response.errorCode.equals("000")) {
                listPage = response.commentlist;
                listALL.addAll(listPage);
                adapter.notifyDataSetChanged();
            }
            if (response.errorCode.equals("001")){
                showToast("暂无数据");
               adapter.notifyDataSetChanged();
            }
        }
    }

    private void initList() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                if (type.equals("tvSelect")) {
                    getList();
                } else {
                    getMyList();
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                if (type.equals("tvSelect")) {
                    getList();
                } else {
                    getMyList();
                }
            }
        });
    }

    private void initView() {
        txtTitle.setText("买家秀库");
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setImageResource(R.mipmap.buyer_fabu);
        imageRight.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        tvMyMaterial.setOnClickListener(this);
        goodsId = getIntent().getStringExtra("goodsId");
        adapter = new BuyerShowListAdapter(context, listALL);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_right:
                if (caches.get("access_token").equals("null")) {
                    startActivity(new Intent(BuyersShowListActivity.this, LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(context, BuyersShowActivity.class);
                intent.putExtra("goodsId", goodsId);
                startActivity(intent);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_select:
                type = "tvSelect";
                page = 1;
                listALL.clear();
                adapter = new BuyerShowListAdapter(context, listALL);
                listView.setAdapter(adapter);
                tvSelect.setTextColor(getResources().getColor(R.color.activity_red));
                tvMyMaterial.setTextColor(getResources().getColor(R.color.gray));
                getList();
                break;
            case R.id.tv_my_material:
                if (caches.get("access_token").equals("null")) {
                    startActivity(new Intent(BuyersShowListActivity.this, LoginActivity.class));
                    return;
                } else {
                    userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                    }.getType());
                    uid = userInfoEntity.data.userVo.id;
                    type = "tvMyMaterial";
                    page = 1;
                    listALL.clear();
                    adapter = new BuyerShowListAdapter(context, listALL);
                    listView.setAdapter(adapter);
                    tvSelect.setTextColor(getResources().getColor(R.color.gray));
                    tvMyMaterial.setTextColor(getResources().getColor(R.color.activity_red));
                    getMyList();
                }
                break;
        }
    }
}
