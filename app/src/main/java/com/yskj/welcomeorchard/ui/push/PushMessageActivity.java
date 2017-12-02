package com.yskj.welcomeorchard.ui.push;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.NewsBoxAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.NewsBoxEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.http.PushMessageCallBalk;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
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
 * 消息分类
 */

public class PushMessageActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.push_nolistview)
    NoScrollListView pushNolistview;
    @Bind(R.id.poput_message_nolistview)
    NoScrollListView poputMessageNolistview;
    @Bind(R.id.pull_push_scrollview)
    PullToRefreshScrollView pullPushScrollview;
    private NewsBoxAdapter adapter;
    private ArrayList<NewsBoxEntity.ListBean> listData = new ArrayList<>();
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private UserInfoEntity userInfoEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushmessage);
        ButterKnife.bind(this);
        txtTitle.setText("消息盒子");
        if(!caches.get("access_token").equals("null")){
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"),new TypeToken<UserInfoEntity>(){}.getType());
            id = userInfoEntity.data.userVo.id;
        }else {
            startActivity(new Intent(context, LoginActivity.class));
        }

        initListView();
        getWindcow();
        getNewsBox();

    }

    private void initListView() {
        adapter = new NewsBoxAdapter(context, listData);
        pushNolistview.setAdapter(adapter);
        pushNolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,MessageDetailsActivity.class);
                intent.putExtra("type",listData.get(position).type);
                startActivity(intent);
            }
        });
    }

    //获取消息盒子list
    private void getNewsBox() {
        OkHttpUtils.get().url(Urls.NEWSBOX)
                .addHeader("Authorization", caches.get("access_token"))
                .build().execute(new NewsBoxCallBack());
    }

    private void getWindcow() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.WINDOWS + "/uid/"+id).build().execute(new PushMessageCallBalk(context, poputMessageNolistview));
        stopMyDialog();
    }

    private class NewsBoxCallBack extends Callback<NewsBoxEntity> {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            startMyDialog();
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
            isLogin(e);
        }

        @Override
        public void onResponse(NewsBoxEntity response, int id) {
            stopMyDialog();
            if (response.code == 0) {
                if (response.data.list.size()==0){
                    showToast("没有数据了");
                }
                listData.addAll(response.data.list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
