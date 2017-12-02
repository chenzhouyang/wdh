package com.yskj.welcomeorchard.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.GameActivity;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.supply.SourcelistActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Network;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


public class ExessiveActivity extends BaseActivity {


    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.orchard_shopping)
    Button orchardShopping;
    @Bind(R.id.orchard_game)
    Button orchardGame;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private boolean isStartUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exessive);
        //取消电池栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//禁止所有activity横屏
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.orchard_shopping, R.id.orchard_game})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                startActivity(new Intent(context, com.yskj.welcomeorchard.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.orchard_shopping:
                if(caches.get("access_token").equals("null")){
                    isStartUp = false;
                    goHome();
                }else {
                    startActivity(new Intent(context, SourcelistActivity.class));
                }

                break;
            case R.id.orchard_game:
                if(caches.get("access_token").equals("null")){
                    isStartUp = true;
                    goHome();
                }else {
                    startActivity(new Intent(context, GameActivity.class));
                }
                break;
        }
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
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage("您还没有登录，是否登录？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                startActivity(new Intent(context,LoginActivity.class));
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                    }
                }).show();//在按键响应事件中显示此对话框
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
            if(e.getMessage().contains("401")){
                goHome();
            }else if(e.getMessage().contains("403")||e.getMessage().contains("400")){
                startActivity(new Intent(context, LoginActivity.class));
            }else  if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("500")){
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage(R.string.string_error)//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
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
            getUserInfo( Config.TOKENHEADER+response.accessToken);
        }
    }
    private void getUserInfo(String token) {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int ids) {
                caches.put("userinfo", response);
                if(isStartUp){
                    startActivity(new Intent(context, GameActivity.class));
                }else {
                    startActivity(new Intent(context, SourcelistActivity.class));
                }

            }
        });
    }
}
