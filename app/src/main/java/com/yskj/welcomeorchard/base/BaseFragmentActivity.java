package com.yskj.welcomeorchard.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.MyLoading;
import com.yskj.welcomeorchard.dialog.VersionDialog;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Network;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/12.
 */

public class BaseFragmentActivity extends FragmentActivity{
    public SharedPreferences sp;
    private MyLoading myloading;
    public Context context;
    private LoadingCaches aCache = LoadingCaches.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        sp = getSharedPreferences("UserInfor", 0);
        context = getApplicationContext();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public String getToken() {
        Long token = Long.valueOf(sp.getString("token", "0")) + Long.valueOf(sp.getString("server_salt", "0"));
        return String.valueOf(token);
    }

    /**
     * 判断是否登录
     */
    public void isLogin(Exception e) {
        if (e==null||e.getMessage() == null){
            return;
        }
        if (e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("401")){
            refresh();
        }else if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("500")){
            showToast(R.string.string_error);
        }else  if(e.getMessage().contains("502")){
            showToast("服务器维护中");
        }else  {
            showToast("网络链接错误 ");
        }
    }

    public void onBack(View v) {

        dismissInputMethod();
        finish();
    }

    public void rightTextview(View v) {

    }

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (context==null){
            return;
        }
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(context);
        }
        if (!isFinishing()) {
            myloading.show();
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            if (!isFinishing()){
                myloading.dismiss();
            }
            myloading = null;
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int strId) {
        Toast.makeText(getApplicationContext(), strId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示框
     * @param title 标题
     * @param msg 内容
     * @param detailMsg 详细内容
     * @param iconId 内容 图标 没有就传0
     * @param gravity 图标和内容位置
     * @param positiveText 确定字样
     * @param negativetext 取消字样
     * @param cancelable 点击屏幕意外或者返回键是否消失
     * @param listener 按键监听
     */
    public void showDialog(String title, String msg, String detailMsg,
                           int iconId, int gravity, String positiveText, String negativetext,
                           boolean cancelable, DialogInterface.OnClickListener listener, Context context
    ) {
        VersionDialog.Builder builder = new VersionDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg,iconId,gravity);
        builder.setDetailMessage(detailMsg);
        builder.setPositiveButton(positiveText, listener);
        builder.setNegativeButton(negativetext, listener);
        builder.setCancelable(cancelable);
        if (!isFinishing()) {
            builder.create().show();
        }
    }

    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseFragmentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseFragmentActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseFragmentActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

    }

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseFragmentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseFragmentActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(BaseFragmentActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 字体大小不随系统改变而变
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    public void  refresh() {
        if (Network.isNetworkAvailable(this)) {
                OkHttpUtils.post().url(Urls.REFRE)
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .addHeader("Authorization",aCache.get("access_token"))
                        .addParams("refresh_token",aCache.get("refresh_token"))
                        .build().execute(new LoginCallBack());
        }else {
            showToast("请检查您的网络");
        }

    }
    private void goHome() {
        if (Network.isNetworkAvailable(this)){
            SharedPreferences share2    = getSharedPreferences("mobile", 0);
            String mobile  = share2.getString("mobile", "null");
            SharedPreferences share    = getSharedPreferences("password", 0);
            String password  = share.getString("password", "null");
            if(mobile.equals("null")||password.equals("null")){
                return;
            }
            OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                    .addHeader("Authorization", Ips.AUTHORIZATION)
                    .addParams("username", mobile)
                    .addParams("password", password)
                    .addParams("grant_type", "password").build()
                    .execute(new LoginCallBack());
        }else {
            showToast("请检查您的网络设置");
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
                showToast("帐号或密码错误");
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
            }else if(e.getMessage().contains("502")){
                showToast("服务器维护中");
            }else {
                showToast("网络链接错误 ");
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            aCache.put("access_token", Config.TOKENHEADER+response.accessToken);
            aCache.put("php_token",response.accessToken);
            aCache.put("refresh_token",response.refreshToken);
            aCache.put("token_type",response.tokenType);
            aCache.put("expires_in",response.expiresIn+"");
            aCache.put("scope",response.scope);
            showToast("请再次请求数据");
        }
    }
}
