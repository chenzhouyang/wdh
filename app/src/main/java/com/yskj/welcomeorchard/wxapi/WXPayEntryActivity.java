package com.yskj.welcomeorchard.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.SendAdEntity;
import com.yskj.welcomeorchard.payment.wx.Constants;
import com.yskj.welcomeorchard.ui.advertising.AdvertisingDraftActivity;
import com.yskj.welcomeorchard.ui.advertising.ChooseVersionActivity;
import com.yskj.welcomeorchard.ui.advertising.SendAdvertisingActivity;
import com.yskj.welcomeorchard.ui.advertising.VersionWebActivity;
import com.yskj.welcomeorchard.ui.localserver.LocalServerDetailActivity;
import com.yskj.welcomeorchard.ui.localserver.LocalServerOrderActivity;
import com.yskj.welcomeorchard.ui.order.OrderListActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.ui.redboxx.RedListActivity;
import com.yskj.welcomeorchard.ui.supply.ShoppingCartActivity;
import com.yskj.welcomeorchard.ui.supply.SourceParticularsActivity;
import com.yskj.welcomeorchard.ui.supply.SourcelistActivity;
import com.yskj.welcomeorchard.ui.supply.SupplyConfirmActivity;
import com.yskj.welcomeorchard.ui.supply.SurceOrderActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private String wxPayOrderNumber;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private boolean ispaycode = true;//判断微信是否支付成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case 0: // 支付成功
                switch (caches.get("ordertype")){
                    case "0":
                        setmoeny();
                        break;
                    case "1":
                        Localpay();
                        break;
                    case "2":
                        Openvip();
                        break;
                    case "3":
                        if (ispaycode){
                            ispaycode = false;
                            paycodement();
                        }
                        break;
                    case "4":
                        if (ispaycode){
                            ispaycode = false;
                            isChargePaySendOk();
                        }

                        break;
                    case "orderpay":
                        orderpay();
                        break;
                    case "iescpfinish":
                        Iescpfinish();
                        break;
                }
                break;
            case -1:
                showToast("支付失败");
                finish();
                break;
            case -2:
                showToast("取消支付");
                finish();
                break;
        }
    }
    private void Iescpfinish(){
        OkHttpUtils.post().url(Urls.IESCPF)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId",caches.get("tid"))
                .addParams("rechargeOrderNo", caches.get("pay_trade_no"))
                .build().execute(new StringCallback() {
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
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    showToast("付款成功");
                } else {
                    showToast(MessgeUtil.geterr_code(code));
                }
                AppManager.getInstance().killActivity(WXPayEntryActivity.class);
                AppManager.getInstance().killActivity(CaptureActivity.class);
            }
        });
    }
    //广告红包充值之后
    private void isChargePaySendOk() {
        //检查是否发送成功
        OkHttpUtils.post().url(Urls.ADSENDRED)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("adRedId", caches.get("tid"))
                .addParams("tradeNo",caches.get("pay_trade_no"))
                .build().execute(new sendADCallBack());
    }

    //检查是否发送成功
    private class sendADCallBack extends Callback<SendAdEntity> {
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
        public SendAdEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            SendAdEntity sendAdEntity = new Gson().fromJson(s, new TypeToken<SendAdEntity>() {
            }.getType());
            return sendAdEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("服务器正忙，稍等。。。");
        }

        @Override
        public void onResponse(SendAdEntity response, int id) {
            if (response.code == 0) {
                getuserinfo();
            }else {
                showToast(MessgeUtil.geterr_code(response.code));
                AppManager.getInstance().killActivity(ChooseVersionActivity.class);
                AppManager.getInstance().killActivity(VersionWebActivity.class);
                AppManager.getInstance().killActivity(SendAdvertisingActivity.class);
                AppManager.getInstance().killActivity(AdvertisingDraftActivity.class);
                AppManager.getInstance().killActivity(WXPayEntryActivity.class);

            }
        }
    }
    //扫码充值完成之后地区接口
    private void paycodement(){
        OkHttpUtils.post().url(Urls.CODEPAYMENT)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("tid",caches.get("tid"))
                .addParams("rechargeOrderNo",caches.get("pay_trade_no"))
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
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    getuserinfos();
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                    finish();
                }
            }
        });
    }
    //本地生活付款成功调取
    private void Localpay() {
        String url = null;
        if(caches.get("orderwxtype").equals("2")||caches.get("orderwxtype").equals("1")){
            url = Urls.CREATE;
        }else if(caches.get("orderwxtype").equals("3")){
            url = Urls.MGORDERPAY;
        }
        OkHttpUtils.post().url(url)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId", caches.get("tid"))
                .addParams("rechargeOrderNo",caches.get("pay_trade_no"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                if(caches.get("orderwxtype").equals("2")||caches.get("orderwxtype").equals("1")){
                    AppManager.getInstance().killActivity(SupplyConfirmActivity.class);
                    AppManager.getInstance().killActivity(SourceParticularsActivity.class);
                    AppManager.getInstance().killActivity(SourcelistActivity.class);
                    AppManager.getInstance().killActivity(ShoppingCartActivity.class);
                    AppManager.getInstance().killActivity(OrderPayActivity.class);
                    startActivity(new Intent(context, LocalServerOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else if(caches.get("orderwxtype").equals("3")){
                    startMyDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             *要执行的操作
                             */
                            AppManager.getInstance().killActivity(SupplyConfirmActivity.class);
                            AppManager.getInstance().killActivity(SourceParticularsActivity.class);
                            AppManager.getInstance().killActivity(SourcelistActivity.class);
                            AppManager.getInstance().killActivity(ShoppingCartActivity.class);
                            AppManager.getInstance().killActivity(OrderPayActivity.class);
                            startActivity(new Intent(context, SurceOrderActivity.class)
                                    .putExtra("orderstaut","2").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }, 1000);//3秒后执行Runnable中的run方法
                }
                } else {
                    showToast(MessgeUtil.geterr_code(code));
                    AppManager.getInstance().killActivity(WXPayEntryActivity.class);
                    AppManager.getInstance().killActivity(LocalServerDetailActivity.class);
                    AppManager.getInstance().killActivity(OrderPayActivity.class);
                }
            }
        });
    }


    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int ids) {
                caches.put("userinfo", response);
                if(caches.get("ordertype").equals("4")){
                    showToast("投放成功");
                    AppManager.getInstance().killActivity(ChooseVersionActivity.class);
                    AppManager.getInstance().killActivity(VersionWebActivity.class);
                    AppManager.getInstance().killActivity(SendAdvertisingActivity.class);
                    AppManager.getInstance().killActivity(AdvertisingDraftActivity.class);
                    AppManager.getInstance().killActivity(WXPayEntryActivity.class);
                }else {
                    showToast("购买成功");
                    startActivity(new Intent(context, LocalServerOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }



            }
        });
    }


    //商城充值完成后完成订单交易
    private void setmoeny(){
        OkHttpUtils.post().url(Urls.PAYORDER)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId",caches.get("orderid"))
                .addParams("tradeNo",caches.get("pay_trade_no"))
                .addParams("payType", "1")
                .build().execute(new StringCallback() {
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
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    showToast("购买成功");
                    startActivity(new Intent(context,OrderListActivity.class)
                            .putExtra("orderpaytype","0").putExtra("orderstaut","2")
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                    finish();
                }
            }
        });
    }
    //vip付款成功调取接口
    private void Openvip(){
        OkHttpUtils.post().url(Urls.OPENVIP) .addHeader("Authorization", caches.get("access_token"))
                .addParams("vipKey", Config.VIPKEY)
                .addParams("userVipId",caches.get("tid"))
                .addParams("tradeNo",caches.get("pay_trade_no"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    startActivity(new Intent(context, RedListActivity.class).putExtra("data","1"));
                    showToast("开通成功，您可以享受vip服务了");
                }else {
                    startActivity(new Intent(context, RedListActivity.class).putExtra("data","1"));
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }
    //订单支付
    public void orderpay(){
        OkHttpUtils.post().url(Urls.GOODORDERPAY).addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId",caches.get("orderid"))
                .addParams("rechargeOrderNo",caches.get("pay_trade_no"))
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

            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    showToast("支付成功");
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
                AppManager.getInstance().killActivity(WXPayEntryActivity.class);
                AppManager.getInstance().killActivity(CaptureActivity.class);
            }
        });
    }
    private void getuserinfos() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int ids) {
                caches.put("userinfo", response);
                showToast("付款成功");
                AppManager.getInstance().killActivity(WXPayEntryActivity.class);
                AppManager.getInstance().killActivity(CaptureActivity.class);
            }
        });
    }
}