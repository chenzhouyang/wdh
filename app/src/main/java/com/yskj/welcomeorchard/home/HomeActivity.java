package com.yskj.welcomeorchard.home;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.GoodsInfoDialog;
import com.yskj.welcomeorchard.entity.CommodityEntity;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.entity.UpdateApp;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.receiver.UpdateInfoService;
import com.yskj.welcomeorchard.ui.advertising.AdvertisingMainActivity;
import com.yskj.welcomeorchard.ui.mine.MineActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Network;
import com.yskj.welcomeorchard.utils.SystemBarTintManager;
import com.yskj.welcomeorchard.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.yskj.welcomeorchard.PgyApplication.sp;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class HomeActivity extends TabActivity {

    @Bind(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @Bind(android.R.id.tabs)
    TabWidget tabs;
    //主页商城
    @Bind(R.id.home_tab_main)
    RadioButton homeTabMain;
    //本地
    @Bind(R.id.home_tab_local)
    RadioButton homeTabLocal;
    //广告
    @Bind(R.id.home_tab_ad)
    RadioButton homeTabAd;
    //购物车
    @Bind(R.id.home_tab_shopping_cart)
    RadioButton homeTabShoppingCart;
    //我的
    @Bind(R.id.home_tab_user)
    RadioButton homeTabUser;

    @Bind(R.id.home_radio_button_group)
    RadioGroup homeRadioButtonGroup;
    @Bind(android.R.id.tabhost)
    TabHost tabhost;
    private SystemBarTintManager mBarTintManager;

    public static final String TAB_MAIN = "MAIN_ACTIVITY";
    public static final String TAB_LOCAL = "LOCAL_ACTIVITY";
    public static final String TAB_AD = "AD_ACTIVITY";
    public static final String TAB_CART = "CART_ACTIVITY";
    public static final String TAB_PERSONAL = "USER_ACTIVITY";
    private LoadingCaches caches = LoadingCaches.getInstance();

    ClipboardManager clipboardManager;
    String tempStr;

    private UpdateApp info;
    private boolean isFirst = false;  //判断是否是第一次进入app
    private CommodityEntity commodityEntity;
    private UpdateInfoService updateInfoService;
    private UserInfoEntity userInfoEntity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mBarTintManager = new SystemBarTintManager(this);
        mBarTintManager.setStatusBarTintEnabled(true);
        updateInfoService = new UpdateInfoService(this);
        initView();
    }
    //判断app是否是在前台
    private boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (currentPackageName != null && currentPackageName.equals(getPackageName())) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst){
            GetClipBoardContent();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在前台
        if (isRunningForeground()){
            isFirst = true;
        }else {
            isFirst = false;
        }
    }

    //获取剪切板上信息，判断是否弹出商品对话框
    public void GetClipBoardContent() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager == null) {
                    return;
                }
                if (clipboardManager.getText() != null && clipboardManager.getText().toString().contains(Ips.PHPURL + "/index.php?m=Mobile&c=Goods&a=goodsInfo&id=")) {
                    tempStr = clipboardManager.getText().toString();
                    String goodsId = tempStr.substring(tempStr.indexOf("&id=") + 4, tempStr.indexOf("&spreader="));
                    OkHttpUtils.get().url(Urls.COMMODITY + goodsId).build().execute(new CommodityCallBack());
                }
            }
        });
    }

    /**
     * 获取数据进行实例化
     */
    private class CommodityCallBack extends Callback<CommodityEntity> {

        @Override
        public CommodityEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            commodityEntity = new Gson().fromJson(s, new TypeToken<CommodityEntity>() {
            }.getType());
            return commodityEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(CommodityEntity response, int id) {
            if (response.error_code.equals("000")) {
                CommodityEntity.GoodsBean goodsBean = response.goodsBean;
                GoodsInfoDialog dialog = new GoodsInfoDialog(HomeActivity.this, goodsBean.goodsName, "￥" + goodsBean.shopPrice, goodsBean.originalImg, goodsBean.goodsId);
                dialog.show();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将系统剪贴板设置为""。
                cm.setText("");
            }
        }
    }
    private void initView() {
        tabhost = getTabHost();
        Intent i_local = new Intent(this, NewLocalActivity.class);// 本地
        Intent i_main = new Intent(this, SolaFidesActivity.class);// 商城改为唯信
        Intent i_advertising = new Intent(this, AdvertisingMainActivity.class);// 广告
        Intent shopping_cart = new Intent(this, CaptureActivity.class);// 购物车改为扫码
        Intent i_mine = new Intent(this, MineActivity.class);// 我的
        tabhost.addTab(tabhost.newTabSpec(TAB_LOCAL).setIndicator(TAB_LOCAL).setContent(i_local));
        tabhost.addTab(tabhost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN).setContent(i_main));
        tabhost.addTab(tabhost.newTabSpec(TAB_AD).setIndicator(TAB_AD).setContent(i_advertising));
        tabhost.addTab(tabhost.newTabSpec(TAB_CART).setIndicator(TAB_CART).setContent(shopping_cart));
        tabhost.addTab(tabhost.newTabSpec(TAB_PERSONAL).setIndicator(TAB_PERSONAL).setContent(i_mine));
        mBarTintManager.setStatusBarAlpha(0);
        homeRadioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_tab_main:
                        //唯信
                        tabhost.setCurrentTabByTag(TAB_MAIN);
                        mBarTintManager.setStatusBarAlpha(0);
                        mBarTintManager.setStatusBarTintResource(R.color.black);
                        break;
                    case R.id.home_tab_local:
                        //本地
                        tabhost.setCurrentTabByTag(TAB_LOCAL);
                        mBarTintManager.setStatusBarAlpha(1);
                        mBarTintManager.setStatusBarTintResource(R.color.transparent);
                        break;
                    case R.id.home_tab_ad:
                        //广告
                        tabhost.setCurrentTabByTag(TAB_AD);
                        mBarTintManager.setStatusBarAlpha(0);
                        mBarTintManager.setStatusBarTintResource(R.color.black);
                        break;
                    case R.id.home_tab_shopping_cart:
                        //扫码
                            tabhost.setCurrentTabByTag(TAB_CART);
                            mBarTintManager.setStatusBarAlpha(0);
                            mBarTintManager.setStatusBarTintResource(R.color.black);
                        break;
                    case R.id.home_tab_user:
                        //我的
                        if (caches.get("access_token").equals("null")) {
                            goHome();
                        } else {
                            tabhost.setCurrentTabByTag(TAB_PERSONAL);
                            mBarTintManager.setStatusBarAlpha(1);
                            mBarTintManager.setStatusBarTintResource(R.color.black);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 自动登录
     */
    public void goHome() {
        if (Network.isNetworkAvailable(this)){
            SharedPreferences share2    = getSharedPreferences("mobile", 0);
            String mobile  = share2.getString("mobile", "null");
            SharedPreferences share    = getSharedPreferences("password", 0);
            String password  = share.getString("password", "null");
            if(mobile.equals("null")||password.equals("null")){
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            }else {
                OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                        .addHeader("Authorization", Ips.AUTHORIZATION)
                        .addParams("username", mobile)
                        .addParams("password", password)
                        .addParams("grant_type", "password").build()
                        .execute(new LoginCallBack());
            }
        }
    }
    private class LoginCallBack extends Callback<LoginEntity> {
        @Override
        public LoginEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginEntity loginEntity = new Gson().fromJson(s, new TypeToken<LoginEntity>() {
            }.getType());
            return loginEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if(e == null||e.getMessage() == null){
                return;
            }
            if(e.getMessage().contains("403")||e.getMessage().contains("400")){
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            caches.put("access_token", Config.TOKENHEADER+response.accessToken);
            caches.put("php_token",response.accessToken);
            caches.put("refresh_token",response.refreshToken);
            caches.put("token_type",response.tokenType);
            caches.put("expires_in",response.expiresIn+"");
            caches.put("scope",response.scope);
            caches.put("token",response.accessToken);
            caches.put("page","0");
            sp.edit().putString("access_token",Config.TOKENHEADER+ response.accessToken).commit();
            sp.edit().putString("refresh_token",response.refreshToken).commit();
            sp.edit().putString("page","0").commit();
            getUserInfo();
        }
    }

    private void getUserInfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int ids) {
                caches.put("userinfo", response);
                sp.edit().putString("userinfo",response).commit();
                tabhost.setCurrentTabByTag(TAB_PERSONAL);
                mBarTintManager.setStatusBarAlpha(1);
                mBarTintManager.setStatusBarTintResource(R.color.black);
            }
        });
    }

    private static Boolean isExit = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            exitBy2Click();
        }
        return true;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次返回,退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            AppManager.getInstance().AppExit(this);
        }
    }

}
