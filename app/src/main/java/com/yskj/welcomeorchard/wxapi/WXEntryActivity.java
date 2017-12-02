package com.yskj.welcomeorchard.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AdOpenRedEntity;
import com.yskj.welcomeorchard.payment.wx.Constants;
import com.yskj.welcomeorchard.ui.advertising.AdvertisingOpenActivity;
import com.yskj.welcomeorchard.ui.advertising.OpenRedListActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {


    private IWXAPI api;
    private LoadingCaches caches = LoadingCaches.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int code = baseResp.errCode;
        switch (code){
            case 0://分享成功后
                if(caches.get("share").equals("1")){
                    AgainOpenRed();
                }else {
                    showToast("分享成功");
                   finish();
                }
                break;
            case -1:
                finish();
                break;
            case -2://用户取消支付后的界面
                finish();
                break;
        }
    }
    /**
     * 分享成功之后调取
     */
    private void AgainOpenRed(){
        OkHttpUtils.post().url(Urls.ADOPENRED).addHeader("Authorization", caches.get("access_token"))
                .addParams("adRedId", caches.get("adRedId")).addParams("latitude", caches.get("latitude"))
                .addParams("haveToShare","1")
                .addParams("longitude", caches.get("longitude")).build().execute(new StringCallback() {
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
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
                finish();
                AppManager.getInstance().killActivity(AdvertisingOpenActivity.class);
            }
        });
    }
}
