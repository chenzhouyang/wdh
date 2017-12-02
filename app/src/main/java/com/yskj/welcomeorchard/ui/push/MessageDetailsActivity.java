package com.yskj.welcomeorchard.ui.push;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.NewsBoxDetailAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.NewsBoxEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by YSKJ-02 on 2017/1/13.
 * 消息详情列表
 */

public class MessageDetailsActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.message_details_listview)
    NoScrollListView messageDetailsListview;
    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;

    private int type;
    private int page;
    private ArrayList<NewsBoxEntity.ListBean> newsListALL = new ArrayList<>();//全部数据
    private ArrayList<NewsBoxEntity.ListBean> newsListPage = new ArrayList<>();//请求的每一页的数据
    private NewsBoxDetailAdapter adapter;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        ButterKnife.bind(this);
        initView();
        initListView();
        getNewsList();
    }

    private void initListView() {
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                getNewsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                getNewsList();
            }
        });
    }

    private void getNewsList() {
        OkHttpUtils.get().url(Urls.NEWSBOX)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("type",type+"")
                .addParams("count","10")
                .addParams("cursor",page*10+"")
                .build().execute(new NewsBoxCallBack());
    }

    private class NewsBoxCallBack extends Callback<NewsBoxEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public NewsBoxEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            NewsBoxEntity newsBoxEntity = new Gson().fromJson(s, new TypeToken<NewsBoxEntity>() {
            }.getType());
            return newsBoxEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            scrollView.onRefreshComplete();
            showToast("请检查网络");
        }

        @Override
        public void onResponse(NewsBoxEntity response, int id) {
            stopMyDialog();
            if (response.code == 0) {
                //每次清空上页数据
                newsListPage.clear();
                newsListPage = response.data.list;
                if(newsListPage.size()==0){
                    showToast("没有数据了");
                }
                scrollView.onRefreshComplete();
                if (page == 0) {
                    newsListALL.clear();
                }
                newsListALL.addAll(newsListPage);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 0:
                txtTitle.setText("个人消息");
                break;
            case 1:
                txtTitle.setText("公告消息");
                break;
            case 2:
                txtTitle.setText("培训消息");
                break;
            case 3:
                txtTitle.setText("活动消息");
                break;
        }
        imgBack.setOnClickListener(this);
        adapter = new NewsBoxDetailAdapter(context, newsListALL, type);
        messageDetailsListview.setAdapter(adapter);
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
        }
    }
}
