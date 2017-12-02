package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdClearEntity;
import com.yskj.welcomeorchard.entity.ChooseVersionEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.ColorUtil;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.FlowLayout;
import com.yskj.welcomeorchard.widget.TagAdapter;
import com.yskj.welcomeorchard.widget.TagFlowLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用户选择广告模板，用flowlayout设置标签
 */
public class ChooseVersionActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    //标签集合
    private ArrayList<ChooseVersionEntity.DataBean> arrayList;

    private LoadingCaches caches = LoadingCaches.getInstance();

    private int uid ;
    private UserInfoEntity userInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_version);
        ButterKnife.bind(this);
        initView();
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.ADCHOOSEVERSION).build().execute(new chooseVersionCallBack());
//        OkHttpUtils.get().url(Urls.ADREDPAGERCLEAR).addParams("sk", Ips.SK).addParams("uid",uid+"").build().execute(new clearAdCallBack());
    }

    private class clearAdCallBack extends Callback<AdClearEntity>{
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }
        @Override
        public AdClearEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            AdClearEntity chooseVersionEntity = new Gson().fromJson(s, new TypeToken<AdClearEntity>() {}.getType());
            return chooseVersionEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            showToast("请检查网络");
        }

        @Override
        public void onResponse(AdClearEntity response, int id) {
            stopMyDialog();
            if (!response.errorCode.equals("000")){
                finish();
                showToast("网络请求错误");
            }
        }
    }

    private class chooseVersionCallBack extends Callback<ChooseVersionEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public ChooseVersionEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            ChooseVersionEntity chooseVersionEntity = new Gson().fromJson(s, new TypeToken<ChooseVersionEntity>() {}.getType());
            return chooseVersionEntity;
        }
        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            showToast("请检查网络");
        }
        @Override
        public void onResponse(ChooseVersionEntity response, int id) {
            stopMyDialog();
            arrayList = response.data;
            final LayoutInflater mInflater = LayoutInflater.from(context);
            mFlowLayout.setAdapter(new TagAdapter(arrayList) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.item_tag_tv,mFlowLayout, false);
                    GradientDrawable bgShape = (GradientDrawable)tv.getBackground();
                    int color = Color.parseColor(ColorUtil.getRandomColor());
                    bgShape.setStroke(2,color);
                    tv.setText(arrayList.get(position).name);
                    tv.setBackground(bgShape);
                    tv.setTextColor(color);
                    return tv;
                }
            });
            mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
            {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent)
                {
                    Intent intent = new Intent(context,ChooseVersionItemActivity.class);
                    intent.putExtra("cid",arrayList.get(position).id+"");
                    intent.putExtra("type","sample");
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
    private void initView() {
        txtTitle.setText("选版");
        imgBack.setOnClickListener(this);
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(ChooseVersionActivity.this, LoginActivity.class));
            return;
        }else {
                userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {}.getType());
                uid = userInfoEntity.data.userVo.id;
                initData();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
