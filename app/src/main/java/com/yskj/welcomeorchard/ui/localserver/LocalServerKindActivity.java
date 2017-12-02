package com.yskj.welcomeorchard.ui.localserver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.LocalServerNearListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
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
 * Created by jianghe on 2016/11/15 0015.
 */
public class LocalServerKindActivity extends BaseActivity {
    @Bind(R.id.image_del)
    ImageView imageDel;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.scrollView)
    PullToRefreshScrollView scrollView;

    private String category_id, title;
    private int offset = 0;
    private ArrayList<LocalServerMainListBean.LocalLifesBean> localLifes = new ArrayList<>();
    private ArrayList<LocalServerMainListBean.LocalLifesBean> list = new ArrayList<>();
    private LocalServerNearListAdapter adapter;
    private String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ThemeColor.setTranslucentStatus(this,R.color.local_server_title);
        setContentView(R.layout.activity_local_server_kind);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        Intent intent = getIntent();
        category_id = intent.getStringExtra("categoryId");
        title = intent.getStringExtra("title");
        txtTitle.setText(title);
        adapter = new LocalServerNearListAdapter(context, localLifes);
        listView.setAdapter(adapter);
        imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset = 0;
                localLifes.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset++;
                initData();
            }
        });
    }

    private void initData() {
        cityId = sp.getString(Config.SPKEY_CITYID, "0");
        OkHttpUtils.get().url(Urls.LOCALSERVERFIND).addParams("areaId", cityId)
                .addParams("categoryId", category_id).addParams("offset", offset * 10+"").build().execute(new LocalServerKindCallBack());
    }

    private class LocalServerKindCallBack extends Callback<LocalServerMainListBean>{

        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            startMyDialog();
        }
        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }
        @Override
        public LocalServerMainListBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            LocalServerMainListBean localServerMainListBean = new Gson().fromJson(s, new TypeToken<LocalServerMainListBean>() {
            }.getType());
            return localServerMainListBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public void onResponse(LocalServerMainListBean response, int id) {
            if (response.code == 0) {
                list.clear();
                Logger.json(response.data.localLifes.toString());
                list = response.data.localLifes;
                localLifes.addAll(list);
                if (list == null || list.size() == 0) {
                    showToast("暂时没有数据");
                }
                adapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(LocalServerKindActivity.this, LocalServerDetailActivity.class);
                        intent.putExtra("life_id", localLifes.get(position).lifeId + "");
                        intent.putExtra("sell", localLifes.get(position).saleCount + "");
                        intent.putExtra("distance", localLifes.get(position).distanceString + "");
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
