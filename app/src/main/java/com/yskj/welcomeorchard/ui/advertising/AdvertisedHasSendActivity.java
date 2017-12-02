package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AdvertiseAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdvertiseEntity;
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
 * 创建日期 2017/3/15on 11:27.
 * 描述：广告已领取红包列表
 * 作者：姜贺YSKJ-JH
 */
public class AdvertisedHasSendActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.activity_advertised_has_send)
    LinearLayout activityAdvertisedHasSend;
    @Bind(R.id.advertised_pulv)
    PullToRefreshListView advertisedPulv;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int count = 10,cursor = 0;
    private ArrayList<AdvertiseEntity.DataBean.AdRedVoListBean> AdvertisingAll = new ArrayList<>();
    private AdvertiseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertised_has_send);
        ButterKnife.bind(this);
        adapter = new AdvertiseAdapter(context,AdvertisingAll);
        advertisedPulv.setAdapter(adapter);
        initView();
        //获取数据
        iniDate();
        intiview();
    }
    //上拉下拉刷新
    private void intiview(){
        advertisedPulv.setMode(PullToRefreshBase.Mode.BOTH);
        advertisedPulv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor = 0;
                AdvertisingAll.clear();
                iniDate();
                MethodUtils.stopRefresh(advertisedPulv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor++;
                iniDate();
                MethodUtils.stopRefresh(advertisedPulv);
            }
        });
    }
    private void iniDate() {
        OkHttpUtils.get().url(Urls.SENDADRED).addHeader("Authorization", caches.get("access_token"))
                .addParams("count",count+"")
                .addParams("cursor",cursor*10+"")
                .build().execute(new AdvertisedCalllBack());
    }

    private void initView() {
        txtTitle.setText("已发布广告");
        imgBack.setOnClickListener(this);
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(AdvertisedHasSendActivity.this, LoginActivity.class));
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

    private class AdvertisedCalllBack extends Callback<AdvertiseEntity> {
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
        public AdvertiseEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            AdvertiseEntity advertiseentity = new Gson().fromJson(s,new TypeToken<AdvertiseEntity>(){}.getType());
            return advertiseentity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);

        }

        @Override
        public void onResponse(AdvertiseEntity response, int id) {
            if(response.code == 0){
                AdvertisingAll.addAll(response.data.adRedVoList);
                if(response.data.adRedVoList.size()!=0){
                    adapter.notifyDataSetChanged();
                    advertisedPulv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (AdvertisingAll.get(position-1).status==4){
                                showToast("该红包已被禁用");
                                return;
                            }
                            Intent intent = new Intent(context,OpenRedListActivity.class);
                            intent.putExtra("receiveAmount",AdvertisingAll.get(position-1).receiveAmount+"");
                            intent.putExtra("adRedId",AdvertisingAll.get(position-1).id+"");
                            startActivity(intent);
                        }
                    });
                }else {
                    showToast("没有数据了");
                }
            }else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }
}
