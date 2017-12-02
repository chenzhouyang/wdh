package com.yskj.welcomeorchard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yskj.welcomeorchard.widget.ToastHelper;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePalApplication;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class PgyApplication extends LitePalApplication {
    public static PgyApplication instance = null;
    public static SharedPreferences sp;
    public static PgyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        instance = this;
        //分享
        UMShareAPI.get(this);
        //定位
        SDKInitializer.initialize(getApplicationContext());

        sp = getSharedPreferences("UserInfor", 0);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //TODO		打包的时候 LogLevel.NONE
        if (BuildConfig.DEBUG) {
            Logger.init("LSKMall").setLogLevel(LogLevel.FULL);
        } else {
            Logger.init("LSKMall").setLogLevel(LogLevel.NONE);
        }
        initToastHelper();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信 wx12342956d1cab4f9,a5ae111de7d9ea13788a5e02c07c94d
        PlatformConfig.setWeixin(com.yskj.welcomeorchard.config.Config.APP_ID, com.yskj.welcomeorchard.config.Config.WEIXIN_APPSECRET);
        //新浪微博
        PlatformConfig.setSinaWeibo(com.yskj.welcomeorchard.config.Config.SSINA_APP_ID, com.yskj.welcomeorchard.config.Config.SSINA_APP_SECRET);
        /*最新的版本需要加上这个回调地址，可以在微博开放平台申请的应用获取，必须要有*/
        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        //QQ
        PlatformConfig.setQQZone(com.yskj.welcomeorchard.config.Config.QQ_APPID, com.yskj.welcomeorchard.config.Config.QQ_APPKEY);

    }
    private void initToastHelper() {
        ToastHelper.getInstance().init(getApplicationContext());
    }
}
