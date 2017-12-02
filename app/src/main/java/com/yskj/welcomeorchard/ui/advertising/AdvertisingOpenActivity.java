package com.yskj.welcomeorchard.ui.advertising;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdOpenRedEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.OnMultiClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/3/14.
 */

public class AdvertisingOpenActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.web_view)
    BridgeWebView webView;
    @Bind(R.id.button)
    LinearLayout button;
    @Bind(R.id.button_time)
    TextView buttonTime;
    private String adRedId, latitude, longitude, adRedTemplate;
    private TimeCount time;
    private boolean canOpen = false;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private UserInfoEntity userInfoEntity;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                //showToast( platform + " 收藏成功啦");
            } else {
                AgainOpenRed();

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            //showToast("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //showToast("分享取消了");
        }
    };
    private String spreadCode;
    private String shareurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisopen);
        ButterKnife.bind(this);
        initView();
        setButton();
    }

    private void setButton() {
        time = new TimeCount(3000, 1000);//构造CountDownTimer对象
    }

    @Override
    protected void onResume() {
        time.start();
        super.onResume();

    }

    @Override
    protected void onPause() {
        time.cancel();
        super.onPause();
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            canOpen = true;
            buttonTime.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            buttonTime.setText("（" + millisUntilFinished / 1000 + "秒）");
        }
    }

    private void initView() {
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {}.getType());
        spreadCode = userInfoEntity.data.userVo.spreadCode+"";
        token = caches.get("access_token");
        shareurl = Ips.PHPURL+"/index.php?m=Mobile&c=User&a=reg&spreader="+spreadCode+"";

        txtTitle.setText("查看广告");
        adRedId = getIntent().getStringExtra("adRedId");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        adRedTemplate = getIntent().getStringExtra("adRedTemplate");

        caches.put("adRedId",adRedId);
        caches.put("latitude",latitude);
        caches.put("longitude",longitude);
        caches.put("share","1");

        imgBack.setOnClickListener(this);

        initJs();
        button.setOnClickListener(this);
//        button.setVisibility(View.GONE);
    }

    private void initJs() {
        webView.loadUrl(Ips.PHPURL + adRedTemplate);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.requestFocus();
        webView.setWebViewClient(new BridgeWebViewClient(webView));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.button:
                    if (canOpen) {
                        if (OnMultiClickListener.isFastClick()) {
                            openRed();
                        }
                    }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
    /**
     * 拆红包
     */
    private void openRed() {
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(AdvertisingOpenActivity.this, LoginActivity.class));
            return;
        } else {
            OkHttpUtils.post().url(Urls.ADOPENRED).addHeader("Authorization", token)
                    .addParams("adRedId", adRedId).addParams("latitude", latitude)
                    .addParams("haveToShare","0")
                    .addParams("longitude", longitude).build().execute(new StringCallback() {
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
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {
                        AdOpenRedEntity adOpenRedEntity = new Gson().fromJson(response, new TypeToken<AdOpenRedEntity>() {
                        }.getType());
                        Intent intent = new Intent(context, OpenRedListActivity.class);
                        intent.putExtra("receiveAmount", adOpenRedEntity.data.receiveAmount + "");
                        intent.putExtra("adRedId", adOpenRedEntity.data.adRedId + "");
                        startActivity(intent);
                        finish();
                    } else if(code == 815){
                        Shareintegral dialog = new Shareintegral(context);
                        dialog.show();
                    }else {
                        showToast(MessgeUtil.geterr_code(code));
                    }
                }
            });
        }
    }
    /**
     * 分享成功之后调取
     */
    private void AgainOpenRed(){
        OkHttpUtils.post().url(Urls.ADOPENRED).addHeader("Authorization", token)
                .addParams("adRedId", adRedId).addParams("latitude", latitude)
                .addParams("haveToShare","1")
                .addParams("longitude", longitude).build().execute(new StringCallback() {
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
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    AdOpenRedEntity adOpenRedEntity = new Gson().fromJson(response, new TypeToken<AdOpenRedEntity>() {
                    }.getType());
                    Intent intent = new Intent(context, OpenRedListActivity.class);
                    intent.putExtra("receiveAmount", adOpenRedEntity.data.receiveAmount + "");
                    intent.putExtra("adRedId", adOpenRedEntity.data.adRedId + "");
                    startActivity(intent);
                    finish();
                } else if(code == 815){
                    Shareintegral dialog = new Shareintegral(context);
                    dialog.show();
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    /**
     * 分享弹出框
     */
    public class Shareintegral extends Dialog implements View.OnClickListener{
        private ImageView close_image;
        private Button btn_share;
        private Context context;

        public Shareintegral(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_share);
            iniview();
        }

        private void iniview() {
            close_image = (ImageView) findViewById(R.id.close_image);
            btn_share = (Button) findViewById(R.id.btn_share);
            close_image.setOnClickListener(this);
            btn_share.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.close_image:
                    dismiss();
                    break;
                case R.id.btn_share:
                    dismiss();
                    new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle("唯多惠广告红包")
                            .withText("注册成为唯多惠会员，送10元本地生活消费大礼包，分享他人注册，再送5元/人。惠惠在这里等你哦！")
                            .withMedia(new UMImage(context, R.mipmap.lsk_icon))
                            .withTargetUrl(Ips.PHPURL + adRedTemplate)
                            .setCallback(umShareListener)
                            .open();
                    break;
            }
        }
    }
}
