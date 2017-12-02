package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.OpenRedListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdOpenRedDetailEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.utils.ThemeColor;
import com.yskj.welcomeorchard.widget.CircleImageView;
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
 * 创建日期 2017/3/16on 15:13.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OpenRedListActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.img_avatar)
    CircleImageView imgAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.open_trashy)
    TextView openTrashy;

    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private String adRedId, receiveAmount;
    private ArrayList<AdOpenRedDetailEntity.DataBean.ReceivedRedListBean> receiverList = new ArrayList<>();
    private OpenRedListAdapter adapter;
    private String template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_red_list);
        setCustomTheme();
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 重写此方法，达到某些界面不要沉浸式状态栏，直接从写一个空方法即可
     */
    protected void setCustomTheme() {
        ThemeColor.setTranslucentStatus(this, theme());
    }

    /**
     * 重写此方法，达到子类界面自定义颜色
     */
    protected int theme() {
        return R.color.activity_red;
    }

    private void initView() {
        listView.setFocusable(false);
        imgAvatar.setFocusable(true);
        txtTitle.setText("红包详情");
        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(OpenRedListActivity.this, LoginActivity.class));
            return;
        } else {
            token = caches.get("access_token");
        }
        adRedId = getIntent().getStringExtra("adRedId");
        receiveAmount = getIntent().getStringExtra("receiveAmount");
        if (receiveAmount == null || receiveAmount.equals("")) {
            return;
        }
        OkHttpUtils.get().url(Urls.ADREDDETAIL).addHeader("Authorization", token)
                .addParams("adRedId", adRedId).build().execute(new openRedDetailCallBack());
    }

    private class openRedDetailCallBack extends Callback<AdOpenRedDetailEntity> {
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
        public AdOpenRedDetailEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            AdOpenRedDetailEntity adOpenRedDetailEntity = new Gson().fromJson(s, new TypeToken<AdOpenRedDetailEntity>() {
            }.getType());
            return adOpenRedDetailEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(AdOpenRedDetailEntity response, int id) {
            if (response.code == 0) {
                AdOpenRedDetailEntity.DataBean data = response.data;

                GlideImage.loadImage(context, imgAvatar, data.avatar, R.mipmap.default_image);
                tvName.setText(data.nickName + "发出的红包");
                if(data.currUserAmount>0){
                    tvMoney.setVisibility(View.VISIBLE);
                    openTrashy.setVisibility(View.VISIBLE);
                    tvMoney.setText(StringUtils.getStringtodouble(data.currUserAmount));
                }else {
                    tvMoney.setVisibility(View.INVISIBLE);
                    openTrashy.setVisibility(View.INVISIBLE);
                }
                String text = "已领取" + data.receiveCount + "/" + data.sendCount+"个";
                if(data.showSendAmount){
                    text = String.format("%s,共 %.2f / %.2f元",text,data.receiveAmount,data.sendAmount);


                }
               tvMessage.setText(text);


                template = data.template;//红包相信url
                ArrayList<AdOpenRedDetailEntity.DataBean.ReceivedRedListBean> receiverListBean = data.receivedRedList;
                if (receiverListBean != null && receiverListBean.size() != 0) {
                    receiverList.addAll(receiverListBean);
                    adapter = new OpenRedListAdapter(context, receiverList);
                    listView.setAdapter(adapter);
                }
            } else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(context, VersionWebActivity.class);
                intent.putExtra("url", template);
                startActivity(intent);
                break;
        }
    }
}
