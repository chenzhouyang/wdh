package com.yskj.welcomeorchard.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.home.HomeActivity;
import com.yskj.welcomeorchard.register.RegisterActivity;
import com.yskj.welcomeorchard.ui.verify.VerifyActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MobileEncryption;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Created by YSKJ-02 on 2017/1/13.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.login_phone)
    EditText loginPhone;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.login_forget_password)
    TextView loginForgetPassword;
    @Bind(R.id.login_register)
    TextView loginRegister;
    @Bind(R.id.login_btn)
    Button loginBtn;
    private LoadingCaches caches = LoadingCaches.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        txtTitle.setText("登录");
        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(loginPassword.length()!=0){
                    loginBtn.setBackgroundResource(R.drawable.login_btn_true);
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.login_forget_password, R.id.login_register, R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                startActivity(new Intent(context,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.login_forget_password:
                caches.put("type","login");
                startActivity(new Intent(context, VerifyActivity.class).putExtra("type","2").putExtra("code","0"));
                break;
            case R.id.login_register:
                startActivity(new Intent(context, RegisterActivity.class).putExtra("type","0").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.login_btn:

                boolean isPhoneNum = MobileEncryption.isMobileNO(loginPhone.getText().toString());
                if (!isPhoneNum){
                    showToast("请输入正确的手机号");
                }else if(loginPassword.length()==0){
                    showToast("请输入密码");
                }else {
                    findmoblie();
                    SharedPreferences share2 = getSharedPreferences("mobile", 0);
                    SharedPreferences.Editor editor2 = share2.edit();
                    editor2.putString("mobile", loginPhone.getText().toString());
                    editor2.commit();
                    SharedPreferences share = getSharedPreferences("password", 0);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("password", loginPassword.getText().toString());
                    editor.commit();
                    login();

                }
                break;
        }
    }
    private void findmoblie(){
        OkHttpUtils.get().url(Urls.FINDBYMOBILE)
                .addParams("mobile",loginPhone.getText().toString())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(e == null||e.getMessage() == null){
                    return;
                }
                if (e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("401")){
                    goHome();
                }else if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("500")){
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
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    login();
                }else {
                    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                            .setMessage("该手机还没有注册，是否前往注册？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    startActivity(new Intent(context, RegisterActivity.class).putExtra("type","0").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            dialog.dismiss();
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
            }
        });
    }

//登录请求方法
    private void login() {
        OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                .addHeader("Authorization", Ips.AUTHORIZATION)
                .addParams("username", loginPhone.getText().toString())
                .addParams("password", loginPassword.getText().toString())
                .addParams("grant_type", "password").build()
                .execute(new LoginCallBack());
    }

    private class LoginCallBack extends Callback<LoginEntity> {


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
        public LoginEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginEntity loginEntity = new Gson().fromJson(s, new TypeToken<LoginEntity>() {
            }.getType());
            return loginEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (e.getMessage()==null){
                return;
            }
            if(e.getMessage().contains("401")||e.getMessage().contains("403")||e.getMessage().contains("400")){
                showToast("帐号或密码错误");
            }else if(e.getMessage().contains("502")){
                showToast("服务器维护中");
            }else {
                showToast("网络链接错误");
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            SharedPreferences share2 = getSharedPreferences("mobile", 0);
            SharedPreferences.Editor editor2 = share2.edit();
            editor2.putString("mobile", loginPhone.getText().toString());
            editor2.commit();
            SharedPreferences share = getSharedPreferences("password", 0);
            SharedPreferences.Editor editor = share.edit();
            editor.putString("password", loginPassword.getText().toString());
            editor.commit();
            SharedPreferences share3 = getSharedPreferences("isFirstIn", 0);
            SharedPreferences.Editor editor3 = share3.edit();
            editor3.putString("isFirstIn", "0");
            editor3.commit();
            caches.put("access_token", Config.TOKENHEADER+response.accessToken);
            caches.put("php_token",response.accessToken);
            caches.put("refresh_token",response.refreshToken);
            caches.put("token_type",response.tokenType);
            caches.put("expires_in",response.expiresIn+"");
            caches.put("scope",response.scope);
            sp.edit().putString("access_token",Config.TOKENHEADER+ response.accessToken).commit();
            sp.edit().putString("refresh_token",response.refreshToken).commit();
            sp.edit().putString("page","0").commit();
            sp.edit().putString("mobile",loginPhone.getText().toString()).commit();
            sp.edit().putString("password",loginPassword.getText().toString()).commit();
            startActivity(new Intent(context, HomeActivity.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            startActivity(new Intent(context,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return false;

    }
}
