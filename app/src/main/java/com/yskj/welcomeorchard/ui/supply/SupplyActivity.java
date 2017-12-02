package com.yskj.welcomeorchard.ui.supply;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SupplyAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.MillEntivity;
import com.yskj.welcomeorchard.entity.SupplyEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 陈宙洋
 * 2017/8/2.
 * 描述：货源店铺activity
 */

public class SupplyActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.supply_store_image)
    ImageView supplyStoreImage;
    @Bind(R.id.supply_name)
    TextView supplyName;
    @Bind(R.id.supply_name_describe)
    TextView supplyNameDescribe;
    @Bind(R.id.supply_name_abstract)
    TextView supplyNameAbstract;
    @Bind(R.id.supply_listview)
    NoScrollGridView supplyListview;
    @Bind(R.id.supply_webview)
    WebView supplyWebview;
    private ArrayList<SupplyEntity.DataBean.GoodsListBean> list = new ArrayList<>();
    private SupplyAdapter adapter;
    private GridLayoutManager linearLayoutManager;
    private int cursor = 0;
    private String millid;
    private FrameLayout fullScreenView;
    private View customView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);
        ButterKnife.bind(this);
        iniview();
        getMill();
        getGoodsList();
    }

    private void iniview() {
        millid = getIntent().getStringExtra("millid");
        fullScreenView = (FrameLayout) findViewById(R.id.video_fullscreen);
        supplyListview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        WebSettings settings = supplyWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        supplyWebview.setWebChromeClient(new MyWebChromeClient());
        supplyWebview.setWebViewClient(new MyWebViewClient());
        adapter = new SupplyAdapter(context, list);
        linearLayoutManager = new GridLayoutManager(context, 2);
        supplyListview.setAdapter(adapter);
        supplyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context,SourceParticularsActivity.class)
                        .putExtra("goodsid",list.get(position).goodId+"")
                        .putExtra("imageurl",list.get(position).cover)
                        .putExtra("shopname",list.get(position).shopname));
            }
        });

    }
    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("intent")||url.startsWith("youku")){
                return true;
            }else{
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient{

        private CustomViewCallback customViewCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            supplyWebview.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (customView != null) {
                callback.onCustomViewHidden();
                return;
            }
            fullScreenView.addView(view);
            customView = view;
            customViewCallback = callback;
            fullScreenView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {
            if (customView == null)// 不是全屏播放状态
                return;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            customView.setVisibility(View.GONE);
            fullScreenView.removeView(customView);
            customView = null;
            fullScreenView.setVisibility(View.GONE);
            customViewCallback.onCustomViewHidden();
            supplyWebview.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(supplyWebview!=null){
            supplyWebview.resumeTimers();
        }

    }
    @Override
    protected void onPause() {

        super.onPause();
        //暂停播放
        if(supplyWebview!=null){
            supplyWebview.pauseTimers();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //一定要销毁，否则无法停止播放
        if(supplyWebview!=null){
            supplyWebview.destroy();
        }


    }
    @OnClick(R.id.img_back)
    public void onViewClicked() {
        supplyWebview.destroy();
        finish();
    }
    /**
     * 获取头部商家信息
     */
    private void getMill(){
        OkHttpUtils.get().url(Urls.MILLBYID).addParams("millId",millid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                isLogin(e);
            }

            @Override
            public void onResponse(String s, int i) {
                MillEntivity millEntivity = new Gson().fromJson(s,MillEntivity.class);
                if(millEntivity.code == 0){
                    GlideImage.loadImage(context, supplyStoreImage, millEntivity.data.cover, R.mipmap.img_error);
                    supplyName.setText(millEntivity.data.name);
                    supplyWebview.getSettings().setDomStorageEnabled(true);
                    supplyNameAbstract.setText(millEntivity.data.content);
                    supplyWebview.loadUrl(millEntivity.data.url);

                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            supplyWebview.destroy();
            AppManager.getInstance().killActivity(SupplyActivity.class);
        }
        return super.onKeyDown(keyCode, event);
        }

    //商品的数据请求
    private void getGoodsList() {
        OkHttpUtils.get().url(Urls.KEYWOEDGOODSLIST)
                .addParams("millId",millid)
                .addParams("cursor", (cursor*10) + "")
                .addParams("count", "10")
                .build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                SupplyEntity goodsBean = new Gson().fromJson(response, new TypeToken<SupplyEntity>() {
                }.getType());
                if(goodsBean.code == 0){
                   list.addAll(goodsBean.data.goodsList);
                        adapter.notifyDataSetChanged();
                    }else {
                        showToast(MessgeUtil.geterr_code(goodsBean.code));
                    }
                }
        });
    }
}
