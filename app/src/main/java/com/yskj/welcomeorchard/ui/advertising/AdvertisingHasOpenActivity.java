package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AdvertisingAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdvertisingHsaOpenEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建日期 2017/3/16on 9:27.
 * 描述：已领取红包列表
 * 作者：姜贺YSKJ-JH
 */

public class AdvertisingHasOpenActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.adversiti_pulv)
    PullToRefreshListView adversitiPulv;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int count = 10,cursor = 0;
    private ArrayList<AdvertisingHsaOpenEntity.DataBean.AdRedVoListBean> AdvertisingAll = new ArrayList<>();
    private AdvertisingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising_has_open);
        ButterKnife.bind(this);
        adapter = new AdvertisingAdapter(context,AdvertisingAll);
        adversitiPulv.setAdapter(adapter);
        initView();
        intiview();
        //获取数据
        iniDate();
    }
    //获取数据
    private void iniDate() {
        OkHttpUtils.get().url(Urls.RECEIVEADRED).addHeader("Authorization", caches.get("access_token"))
                .addParams("count",count+"")
                .addParams("cursor",cursor*10+"")
                .build().execute(new AdvertisingCallBack());

    }
    //上拉下拉刷新
    private void intiview(){
        adversitiPulv.setMode(PullToRefreshBase.Mode.BOTH);
        adversitiPulv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor = 0;
                AdvertisingAll.clear();
                iniDate();
                MethodUtils.stopRefresh(adversitiPulv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor++;
                iniDate();
                MethodUtils.stopRefresh(adversitiPulv);
            }
        });
        adversitiPulv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,VersionWebActivity.class);
                intent.putExtra("url",AdvertisingAll.get(position-1).template);
                startActivity(intent);
            }
        });
    }
    private void initView() {
        txtTitle.setText("已领取红包");
        imgBack.setOnClickListener(this);
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(AdvertisingHasOpenActivity.this, LoginActivity.class));
            return;
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

    private class AdvertisingCallBack extends Callback<AdvertisingHsaOpenEntity> {
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
        public AdvertisingHsaOpenEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
           AdvertisingHsaOpenEntity advertisingentity = new Gson().fromJson(s,new TypeToken<AdvertisingHsaOpenEntity>(){}.getType());
            return advertisingentity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);

        }

        @Override
        public void onResponse(AdvertisingHsaOpenEntity response, int id) {
            if(response.code == 0){
                AdvertisingAll.addAll(response.data.adRedVoList);
                if(response.data.adRedVoList.size()!=0){
                    adapter.notifyDataSetChanged();
                }else {
                    showToast("没有数据了");
                }
            }else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }


    }
}
