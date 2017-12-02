package com.yskj.welcomeorchard.ui.verify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LoginPassWordEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.accounts.AccountsActivity;
import com.yskj.welcomeorchard.ui.advertising.SendAdvertisingActivity;
import com.yskj.welcomeorchard.ui.deposit.DepositToBankActivity;
import com.yskj.welcomeorchard.ui.setting.SettingActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/13.
 * 设置密码界面
 */

public class SettingPasswordActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.login_phone)
    EditText loginPhone;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.save_password)
    TextView savePassword;
    @Bind(R.id.setpassword_tishi)
    TextView setpasswordTishi;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token, type, mobile, code;
    private UserInfoEntity infoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpasseword);
        ButterKnife.bind(this);
        token = caches.get("access_token");
        infoEntity = new Gson().fromJson(caches.get("userinfo"), UserInfoEntity.class);
        type = getIntent().getStringExtra("type");
        if (type.equals("2") && token.equals("null")) {
            txtTitle.setText("修改登录密码");
        } else if (type.equals("2") && !token.equals("null")) {
            txtTitle.setText("修改登录密码");
        } else {
            if (infoEntity!=null&&infoEntity.data.accountPasswordExist) {
                txtTitle.setText("修改支付密码");
            } else {
                txtTitle.setText("设置支付密码");
            }

        }
        if (type.equals("2")) {
            mobile = getIntent().getStringExtra("mobile");
        } else {
            setpasswordTishi.setVisibility(View.VISIBLE);
            SharedPreferences share2 = getSharedPreferences("mobile", 0);
            mobile = share2.getString("mobile", "null");
            loginPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            loginPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            loginPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            loginPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        code = getIntent().getStringExtra("code");


    }

    //更改未登陆的登陆密码
    private void setloginpasswore() {
        OkHttpUtils.post().url(Urls.NOTLOGINPW)
                .addParams("mobile", mobile)
                .addParams("password", loginPassword.getText().toString()).build().execute(new PassWordCall());
    }

    //更改登陆的登陆密码
    private void setloginpasswores() {
        OkHttpUtils.post().url(Urls.UPDATEPASSWORD)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("password", loginPassword.getText().toString())
                .build().execute(new PassWordCall());
    }

    //更改支付密码
    private void setupdateAccount() {
        OkHttpUtils.post().url(Urls.UPDATEACCOUNT)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("password", loginPassword.getText().toString())
                .build().execute(new PassWordCall());
    }

    //设置支付密码
    private void addupdateAccount() {
        OkHttpUtils.post().url(Urls.UPDATEACCOUNT)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("password", loginPassword.getText().toString())
                .build().execute(new PassWordCall());
    }

    @OnClick({R.id.img_back, R.id.save_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.save_password:
                if (loginPhone.length() == 0) {
                    showToast("输入新密码");
                } else if (loginPassword.length() == 0) {
                    showToast("输入确认新密码");
                } else if(!loginPhone.getText().toString().equals(loginPassword.getText().toString())){
                    showToast("两次输入的密码不一致，请确认");
                }else {
                    if (type.equals("2") && token.equals("null")) {
                        //更改未登陆的登陆密码
                        setloginpasswore();
                    } else if (type.equals("2") && !token.equals("null")) {
                        setloginpasswores();
                    } else {
                        if (infoEntity.data.accountPasswordExist) {
                            setupdateAccount();
                        } else {
                            addupdateAccount();
                        }

                    }
                }

                break;
        }
    }

    public class PassWordCall extends Callback<LoginPassWordEntity> {
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
        public LoginPassWordEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginPassWordEntity passwordentity = new Gson().fromJson(s, new TypeToken<LoginPassWordEntity>() {
            }.getType());
            return passwordentity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
isLogin(e);
        }

        @Override
        public void onResponse(LoginPassWordEntity response, int id) {
            if (response.code == 0) {
                showToast("密码修改成功");
                if(type.equals("2")){
                    SharedPreferences share = getSharedPreferences("password", 0);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("password", loginPassword.getText().toString());
                    editor.commit();
                    SharedPreferences share2    = getSharedPreferences("mobile", 0);
                    String mobile  = share2.getString("mobile", "null");
                    if(caches.get("type").equals("login")){
                        finish();
                        AppManager.getInstance().killActivity(VerifyActivity.class);
                    }else if(mobile.equals("null")){
                        startActivity(new Intent(context, LoginActivity.class));
                    }else {
                        startActivity(new Intent(context, SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                }else {
                    if (!code.equals("0")) {
                        getuserinfo();
                    } else {
                        if(caches.get("type").equals("account")){
                            startActivity(new Intent(context, AccountsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }else if(caches.get("type").equals("tobank")){
                            startActivity(new Intent(context, DepositToBankActivity.class).putExtra("cardid",caches.get("cardid")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }else if(caches.get("type").equals("setting")){
                            startActivity(new Intent(context, SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }else if(caches.get("type").equals("SendAdvertisingActivity")){
                            finish();
                            AppManager.getInstance().killActivity(VerifyActivity.class);
                        }else if(caches.get("type").equals("login")){
                            finish();
                            AppManager.getInstance().killActivity(VerifyActivity.class);
                        }
                    }
                }

            }else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }

    //更新个人信息
    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                caches.put("userinfo", response);
                if(caches.get("type").equals("account")){
                    startActivity(new Intent(context, AccountsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else if(caches.get("type").equals("tobank")){
                    startActivity(new Intent(context, DepositToBankActivity.class).putExtra("cardid",caches.get("cardid")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else if(caches.get("type").equals("setting")){
                    startActivity(new Intent(context, SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else if(caches.get("type").equals("SendAdvertisingActivity")){
                    startActivity(new Intent(context, SendAdvertisingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else if(caches.get("type").equals("login")){
                    goHome();
                }else {
                    AppManager.getInstance().killActivity(VerifyActivity.class);
                    AppManager.getInstance().killActivity(SettingPasswordActivity.class);

                }

            }
        });
    }
}
