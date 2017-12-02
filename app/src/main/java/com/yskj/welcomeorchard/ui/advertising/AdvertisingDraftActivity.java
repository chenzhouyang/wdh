package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AdvertisingDraftAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdvertisingDraftEntity;
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
 * 创建日期 2017/3/15on 11:27.
 * 描述：广告草稿界面
 * 作者：姜贺YSKJ-JH
 */
public class AdvertisingDraftActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.gridView)
    ListView gridView;

    private LoadingCaches caches = LoadingCaches.getInstance();

    private int uid;
    private UserInfoEntity userInfoEntity;
    private AdvertisingDraftAdapter adapter;
    private ArrayList<AdvertisingDraftEntity.AdrListBean> draftList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising_draft);
        ButterKnife.bind(this);
        initView();
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.ADDRAFT + "/state/0/uid/" + uid).build().execute(new draftCallBack());
    }

    private class draftCallBack extends Callback<AdvertisingDraftEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            stopMyDialog();
            super.onAfter(id);
        }

        @Override
        public AdvertisingDraftEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            AdvertisingDraftEntity advertisingDraftEntity = new Gson().fromJson(s, new TypeToken<AdvertisingDraftEntity>() {
            }.getType());
            return advertisingDraftEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(AdvertisingDraftEntity response, int id) {
            if (response.errorCode.equals("000")) {
                draftList = response.adrList;
                adapter = new AdvertisingDraftAdapter(context, draftList);
                gridView.setAdapter(adapter);
            }
        }
    }

    private void initView() {
        txtTitle.setText("草稿箱");
        imgBack.setOnClickListener(this);
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(AdvertisingDraftActivity.this, LoginActivity.class));
            return;
        } else {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
            uid = userInfoEntity.data.userVo.id;
            initData();
        }
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
