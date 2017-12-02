package com.yskj.welcomeorchard.home;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.IntroOrderAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.IntroOrderEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class IntroOrderActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.inreoorderlist_lisetview)
    PullToRefreshListView inreoorderlistLisetview;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private List<IntroOrderEntity.DataBean> listAll = new ArrayList<>();
    private IntroOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introorderlist);
        ButterKnife.bind(this);
        txtTitle.setText("预约列表");
        adapter = new IntroOrderAdapter(context, listAll);
        inreoorderlistLisetview.setAdapter(adapter);
        indate();
    }

    private void indate() {
        OkHttpUtils.get().url(Urls.INTEROLIST)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("count", "100")
                .build().execute(new IntroOrderCallBack());
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
        AppManager.getInstance().killActivity(IntroActivity.class);
    }

    private class IntroOrderCallBack extends Callback<IntroOrderEntity> {
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
        public IntroOrderEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            IntroOrderEntity introOrderEntity = new Gson().fromJson(s, new TypeToken<IntroOrderEntity>() {
            }.getType());
            return introOrderEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(IntroOrderEntity response, int id) {
            if (response.code == 0) {
                listAll.addAll(response.data);
                if (response.data.size() != 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("暂没有数据");
                }

            }
        }
    }
}
