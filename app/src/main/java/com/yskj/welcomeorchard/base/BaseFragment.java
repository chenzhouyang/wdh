package com.yskj.welcomeorchard.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.MyLoading;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Network;
import com.yskj.welcomeorchard.widget.SimplexToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/6/3.
 */
public abstract class BaseFragment extends Fragment {

    public SharedPreferences sp;
    private MyLoading myloading;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return sp.getBoolean("state", false);
    }
    /**
     * 判断是否登录
     */
    public void isLogin(Exception e) {
        if(e == null||e.getMessage() == null){
            return;
        }
        if (e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("401")){
            refresh();
        }else  if(e.getMessage().toString().contains("500")){
            new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                    .setMessage(R.string.string_error)//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        }else {
            showToast("网络链接错误 ");
        }
    }
    public void  refresh(){
        if(Network.isNetworkAvailable(getActivity())){
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
            SharedPreferences share2    = getContext().getSharedPreferences("mobile", 0);
            String mobile  = share2.getString("mobile", "null");
            SharedPreferences share    = getContext().getSharedPreferences("password", 0);
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
            }else if(e.getMessage( ).contains("403")||e.getMessage().contains("400")){
                showToast("帐号或密码错误");
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }else  if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("500")){
                new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
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
    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (getActivity()==null){
            return;
        }
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(getActivity());
        }
        if (!getActivity().isFinishing()) {
            myloading.show();
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            if (!getActivity().isFinishing()){
                myloading.dismiss();
            }
            myloading = null;
        }
    }

    public void showToast(String msg) {
        SimplexToast.show(getActivity(),msg);
    }

    public void showToast(int strId) {
        SimplexToast.show(getActivity(),strId);
    }

}
